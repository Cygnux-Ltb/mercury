package io.mercury.common.concurrent.disruptor;

import org.slf4j.Logger;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.queue.LoadContainer;
import io.mercury.common.concurrent.queue.QueueType;
import io.mercury.common.concurrent.queue.AbstractSingleConsumerQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
public class RingQueue<T> extends AbstractSingleConsumerQueue<T> {

	private static final Logger log = CommonLoggerFactory.getLogger(RingQueue.class);

	private final Disruptor<LoadContainer<T>> disruptor;

	private final LoadContainerEventProducer producer;

	public RingQueue(String queueName, Capacity size) {
		this(queueName, size, false, null);
	}

	public RingQueue(String queueName, Capacity bufferSize, boolean autoRun, Processor<T> processor) {
		this(queueName, bufferSize, autoRun, processor, WaitStrategyOption.BusySpin);
	}

	public RingQueue(String queueName, Capacity bufferSize, boolean autoRun, Processor<T> processor,
			WaitStrategyOption option) {
		super(processor);
		if (queueName != null)
			super.name = queueName;
		// if (queueSize == 0 || queueSize % 2 != 0)
		// throw new IllegalArgumentException("queueSize set error...");
		this.disruptor = new Disruptor<>(
				// 实现EventFactory<LoadContainer<>>的Lambda
				LoadContainer::new,
				// 队列容量
				bufferSize.value(),
				// 实现ThreadFactory的Lambda
				(Runnable runnable) -> Threads.newMaxPriorityThread(this.name + "-WorkingThread", runnable),
				// DaemonThreadFactory.INSTANCE,
				// 生产者策略, 使用单生产者
				ProducerType.SINGLE,
				// Waiting策略
				WaitStrategyFactory.getStrategy(option));
		this.disruptor.handleEventsWith((event, sequence, endOfBatch) -> this.callProcessor(event.unloading()));
		this.producer = new LoadContainerEventProducer(disruptor.getRingBuffer());
		if (autoRun)
			start();
	}

	private void callProcessor(T t) {
		try {
			processor.process(t);
		} catch (Exception e) {
			log.error("processor.process(t) throw exception -> [{}]", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private class LoadContainerEventProducer {

		private final RingBuffer<LoadContainer<T>> ringBuffer;

		private final EventTranslatorOneArg<LoadContainer<T>, T> translator = (container, sequence, t) -> container
				.loading(t);

		private LoadContainerEventProducer(RingBuffer<LoadContainer<T>> ringBuffer) {
			this.ringBuffer = ringBuffer;
		}

		private void onEvent(T t) {
			ringBuffer.publishEvent(translator, t);
		}
	}

	@Override
	public boolean enqueue(T t) {
		try {
			if (!isRunning())
				return false;
			producer.onEvent(t);
			return true;
		} catch (Exception e) {
			log.error("producer.onData(t) throw exception -> [{}]", e.getMessage(), e);
			return false;
		}
	}

	@Override
	protected void start0() {
		disruptor.start();
	}

	@Override
	protected void stop0() {
		disruptor.shutdown();
		log.info("Call shutdown() function success, disruptor is shutdown.");
	}

	public static void main(String[] args) {

		RingQueue<Integer> queue = new RingQueue<>("Test-Queue", Capacity.L06_SIZE, true, in -> System.out.println(in));

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
	public QueueType getQueueType() {
		return QueueType.SPSC;
	}

}
