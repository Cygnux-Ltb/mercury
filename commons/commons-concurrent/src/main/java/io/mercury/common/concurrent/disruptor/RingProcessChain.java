package io.mercury.common.concurrent.disruptor;

import static io.mercury.common.thread.Threads.newThreadFactory;

import java.util.function.Supplier;

import org.slf4j.Logger;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.concurrent.queue.QueueType;
import io.mercury.common.concurrent.disruptor.RingEvent.RingInt;
import io.mercury.common.concurrent.queue.AbstractSingleConsumerQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;
import io.mercury.common.thread.Threads.ThreadPriority;
import io.mercury.common.util.BitOperator;
import io.mercury.common.util.StringSupport;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
public class RingProcessChain<T extends RingEvent> extends AbstractSingleConsumerQueue<T> {

	private static final Logger log = CommonLoggerFactory.getLogger(RingProcessChain.class);

	private final Disruptor<T> disruptor;

	private final LoadContainerEventProducer producer;

	public RingProcessChain(int size, Supplier<T> eventFactory, Processor<T> processor) {
		this(null, size, false, eventFactory, processor);
	}

	public RingProcessChain(String queueName, int size, Supplier<T> eventFactory, Processor<T> processor) {
		this(queueName, size, false, eventFactory, processor);
	}

	public RingProcessChain(String queueName, int size, boolean autoRun, Supplier<T> eventFactory,
			Processor<T> processor) {
		this(queueName, size, autoRun, eventFactory, processor, WaitStrategyOption.BusySpin);
	}

	public RingProcessChain(String queueName, int size, boolean autoRun, Supplier<T> eventFactory,
			Processor<T> processor, WaitStrategyOption option) {
		super(processor);
		if (StringSupport.nonEmpty(queueName))
			super.name = queueName;
		// if (queueSize == 0 || queueSize % 2 != 0)
		// throw new IllegalArgumentException("queueSize set error...");
		this.disruptor = new Disruptor<>(
				// 实现EventFactory<LoadContainer<>>的Lambda
				() -> eventFactory.get(),
				// 设置队列容量, 最小16
				size < 16 ? 16 : BitOperator.minPow2(size),
				// 实现ThreadFactory的Lambda
				newThreadFactory("Disruptor-" + super.name + "-Worker", ThreadPriority.MAX),
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

		private LoadContainerEventProducer(RingBuffer<T> ringBuffer) {
			this.ringBuffer = ringBuffer;
		}

		private void putEvent(final T newEvent) {
			ringBuffer.publishEvent((event, sequence) -> event = newEvent);
		}
	}

	@Override
	public boolean enqueue(T t) {
		try {
			if (isClosed.get())
				return false;
			producer.putEvent(t);
			return true;
		} catch (Exception e) {
			log.error("producer.onEvent(t) throw exception -> [{}]", e.getMessage(), e);
			return false;
		}
	}

	@Override
	protected void start0() {
		disruptor.start();
		log.info("Disruptor::start() func execution succeed, {} is start", name);
	}

	@Override
	protected void stop0() {
		disruptor.shutdown();
		log.info("Disruptor::shutdown() func execution succeed, {} is shutdown", name);
	}

	public static void main(String[] args) {

		RingProcessChain<RingInt> queue = new RingProcessChain<>("Test-Queue", 16, true, RingInt.getEventSupplier(),
				integer -> System.out.println(integer.getValue()));

		Thread thread = Threads.startNewThread(() -> {
			int i = 0;
			for (;;)
				queue.enqueue(new RingInt().setValue(i));
		});

		SleepSupport.sleep(2000);

		queue.stop();
		thread.interrupt();

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
