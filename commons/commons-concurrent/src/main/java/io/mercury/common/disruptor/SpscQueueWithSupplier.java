package io.mercury.common.disruptor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.slf4j.Logger;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.collections.Capacity;
import io.mercury.common.concurrent.queue.QueueType;
import io.mercury.common.concurrent.queue.AbstractSingleConsumerQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;

public class SpscQueueWithSupplier<T> extends AbstractSingleConsumerQueue<T> {

	private static final Logger log = CommonLoggerFactory.getLogger(SpscQueueWithSupplier.class);

	private final Disruptor<T> disruptor;

	private final LoadContainerEventProducer producer;

	private final AtomicBoolean isStop = new AtomicBoolean(false);

	public SpscQueueWithSupplier(Capacity capacity, boolean autoStart, WaitStrategyOption option, Supplier<T> supplier,
			Processor<T> processor) {
		super(processor);
		// if (queueSize == 0 || queueSize % 2 != 0)
		// throw new IllegalArgumentException("queueSize set error...");
		this.disruptor = new Disruptor<>(
				// 实现EventFactory的Lambda
				() -> supplier.get(),
				// 队列容量
				capacity.value(),
				// 实现ThreadFactory的Lambda
				(Runnable runnable) -> Threads.newMaxPriorityThread(super.name + "-WorkingThread", runnable),
				// DaemonThreadFactory.INSTANCE,
				// 生产者策略, 使用单生产者
				ProducerType.SINGLE,
				// Waiting策略
				WaitStrategyFactory.getStrategy(option));
		this.disruptor.handleEventsWith((event, sequence, endOfBatch) -> tryCallProcessor(event));
		this.producer = new LoadContainerEventProducer(disruptor.getRingBuffer());
		if (autoStart)
			start();
	}

	private void tryCallProcessor(T t) {
		try {
			processor.process(t);
		} catch (Exception e) {
			log.error("processor throw exception -> [{}]", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private class LoadContainerEventProducer {

		private final RingBuffer<T> ringBuffer;

		private EventTranslatorOneArg<T, T> translator = (T event, long sequence, T t) -> {
			event = t;
		};

		private LoadContainerEventProducer(RingBuffer<T> ringBuffer) {
			this.ringBuffer = ringBuffer;
		}

		public void onData(T t) {
			ringBuffer.publishEvent(translator, t);
		}
	}

	@Override
	public boolean enqueue(T t) {
		try {
			if (isStop.get())
				return false;
			producer.onData(t);
			return true;
		} catch (Exception e) {
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
		log.info("Call stop0() function success, disruptor is shutdown.");
	}

	@Override
	public String getQueueName() {
		return name;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public QueueType getQueueType() {
		return QueueType.SPSC;
	}

	public static void main(String[] args) {

		SpscQueueWithSupplier<Integer> queue = new SpscQueueWithSupplier<>(Capacity.L10_SIZE, true,
				WaitStrategyOption.BusySpin, () -> Integer.valueOf(0), in -> System.out.println(in));

		Threads.startNewThread(() -> {
			int i = 0;
			for (;;) {
				queue.enqueue(++i);
				SleepSupport.sleep(5000);
			}
		});

		SleepSupport.sleep(10000);

	}

}
