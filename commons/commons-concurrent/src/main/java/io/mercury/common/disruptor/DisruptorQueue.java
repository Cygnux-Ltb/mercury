package io.mercury.common.disruptor;

import java.util.function.Supplier;

import org.slf4j.Logger;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.collections.Capacity;
import io.mercury.common.concurrent.queue.QueueStyle;
import io.mercury.common.concurrent.queue.SingleConsumerQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.StringUtil;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
public class DisruptorQueue<T extends RingBufferEvent<T>> extends SingleConsumerQueue<T> {

	private static final Logger log = CommonLoggerFactory.getLogger(DisruptorQueue.class);

	private final Disruptor<T> disruptor;

	private final LoadContainerEventProducer producer;

	public DisruptorQueue(Capacity bufferSize, Supplier<T> eventFactory, Processor<T> processor) {
		this("", bufferSize, false, eventFactory, processor);
	}

	public DisruptorQueue(String queueName, Capacity bufferSize, Supplier<T> eventFactory, Processor<T> processor) {
		this(queueName, bufferSize, false, eventFactory, processor);
	}

	public DisruptorQueue(String queueName, Capacity bufferSize, boolean autoRun, Supplier<T> eventFactory,
			Processor<T> processor) {
		this(queueName, bufferSize, autoRun, eventFactory, processor, WaitStrategyOption.BusySpin);
	}

	public DisruptorQueue(String queueName, Capacity bufferSize, boolean autoRun, Supplier<T> eventFactory,
			Processor<T> processor, WaitStrategyOption option) {
		super(processor);
		if (StringUtil.nonEmpty(queueName))
			super.queueName = queueName;
		// if (queueSize == 0 || queueSize % 2 != 0)
		// throw new IllegalArgumentException("queueSize set error...");
		this.disruptor = new Disruptor<>(
				// 实现EventFactory<LoadContainer<>>的Lambda
				() -> eventFactory.get(),
				// 队列容量
				bufferSize.value(),
				// 实现ThreadFactory的Lambda
				(Runnable runnable) -> Threads
						.newMaxPriorityThread("disruptor-" + super.queueName + "-WorkingThread", runnable),
				// DaemonThreadFactory.INSTANCE,
				// 生产者策略, 使用单生产者
				ProducerType.SINGLE,
				// Waiting策略
				WaitStrategyFactory.getStrategy(option));
		this.disruptor.handleEventsWith(this::callProcessor);
		this.producer = new LoadContainerEventProducer(disruptor.getRingBuffer());
		if (autoRun)
			start();
	}

	private void callProcessor(T event, long sequence, boolean endOfBatch) {
		try {
			processor.process(event);
		} catch (Exception e) {
			log.error("processor.process(t) throw exception -> [{}]", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private class LoadContainerEventProducer {

		private final RingBuffer<T> ringBuffer;

		private EventTranslatorOneArg<T, T> eventTranslator = (event, sequence, t) -> event = t;

		private LoadContainerEventProducer(RingBuffer<T> ringBuffer) {
			this.ringBuffer = ringBuffer;
		}

		private void onEvent(T t) {
			ringBuffer.publishEvent(eventTranslator, t);
		}
	}

	@Override
	public boolean enqueue(T t) {
		try {
			if (isClosed.get())
				return false;
			producer.onEvent(t);
			return true;
		} catch (Exception e) {
			log.error("producer.onEvent(t) throw exception -> [{}]", e.getMessage(), e);
			return false;
		}
	}

	@Override
	protected void start0() {
		disruptor.start();
	}

	@Override
	protected void stop0() {
		while (disruptor.getBufferSize() != 0)
			SleepSupport.sleep(1);
		disruptor.shutdown();
		log.info("Call stop0() function success, disruptor is shutdown");
	}

	public static void main(String[] args) {

		DisruptorQueue<Integer> queue = new DisruptorQueue<>("Test-Queue", Capacity.L06_SIZE, true, null,
				(integer) -> System.out.println(integer));

		Threads.startNewThread(() -> {
			int i = 0;
			for (;;)
				queue.enqueue(++i);
		});

		SleepSupport.sleep(10000);

		queue.stop();

	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public QueueStyle getQueueStyle() {
		return QueueStyle.SPSC;
	}

}
