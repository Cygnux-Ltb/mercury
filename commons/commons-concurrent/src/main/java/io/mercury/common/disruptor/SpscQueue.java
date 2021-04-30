package io.mercury.common.disruptor;

import org.slf4j.Logger;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.queue.LoadContainer;
import io.mercury.common.concurrent.queue.QueueStyle;
import io.mercury.common.concurrent.queue.SingleConsumerQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
public class SpscQueue<T> extends SingleConsumerQueue<T> {

	private static final Logger log = CommonLoggerFactory.getLogger(SpscQueue.class);

	private final Disruptor<LoadContainer<T>> disruptor;

	private final LoadContainerEventProducer producer;

	public SpscQueue(String queueName, Capacity bufferSize) {
		this(queueName, bufferSize, false, null);
	}

	public SpscQueue(String queueName, Capacity bufferSize, boolean autoRun, Processor<T> processor) {
		this(queueName, bufferSize, autoRun, processor, WaitStrategyOption.BusySpin);
	}

	public SpscQueue(String queueName, Capacity bufferSize, boolean autoRun, Processor<T> processor,
			WaitStrategyOption option) {
		super(processor);
		if (queueName != null)
			super.queueName = queueName;
		// if (queueSize == 0 || queueSize % 2 != 0)
		// throw new IllegalArgumentException("queueSize set error...");
		this.disruptor = new Disruptor<>(
				// 实现EventFactory<LoadContainer<>>的Lambda
				LoadContainer::new,
				// 队列容量
				bufferSize.value(),
				// 实现ThreadFactory的Lambda
				(Runnable runnable) -> Threads.newMaxPriorityThread("Disruptor-" + super.queueName + "-WorkingThread",
						runnable),
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

		private EventTranslatorOneArg<LoadContainer<T>, T> eventTranslator = (event, sequence, t) -> event.loading(t);

		private LoadContainerEventProducer(RingBuffer<LoadContainer<T>> ringBuffer) {
			this.ringBuffer = ringBuffer;
		}

		private void onEvent(T t) {
			ringBuffer.publishEvent(eventTranslator, t);
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
		while (disruptor.getBufferSize() != 0)
			Threads.sleep(1);
		disruptor.shutdown();
		log.info("Call shutdown() function success, disruptor is shutdown.");
	}

	public static void main(String[] args) {

		SpscQueue<Integer> queue = new SpscQueue<>("Test-Queue", Capacity.L06_SIZE, true, in -> System.out.println(in));

		Threads.startNewThread(() -> {
			int i = 0;
			for (;;)
				queue.enqueue(++i);
		});

		Threads.sleep(10000);

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
