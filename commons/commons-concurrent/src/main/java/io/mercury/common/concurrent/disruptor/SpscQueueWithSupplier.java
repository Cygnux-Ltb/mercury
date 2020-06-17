package io.mercury.common.concurrent.disruptor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.slf4j.Logger;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.collections.queue.api.SCQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.ThreadTool;

public class SpscQueueWithSupplier<T> extends SCQueue<T> {

	private Logger log = CommonLoggerFactory.getLogger(SpscQueueWithSupplier.class);

	private Disruptor<T> disruptor;

	private LoadContainerEventProducer producer;

	private AtomicBoolean isStop = new AtomicBoolean(false);

	public SpscQueueWithSupplier(BufferSize bufferSize, boolean autoRun, WaitStrategyOption option,
			Supplier<T> supplier, Processor<T> processor) {
		super(processor);
		// if (queueSize == 0 || queueSize % 2 != 0)
		// throw new IllegalArgumentException("queueSize set error...");
		this.processor = processor;
		this.disruptor = new Disruptor<>(
				// 实现EventFactory的Lambda
				() -> supplier.get(),
				// 队列容量
				bufferSize.value(),
				// 实现ThreadFactory的Lambda
				(Runnable runnable) -> ThreadTool.newMaxPriorityThread(runnable,
						"DisruptorQueue-" + super.queueName + "-WorkingThread"),
				// DaemonThreadFactory.INSTANCE,
				// 生产者策略, 使用单生产者
				ProducerType.SINGLE,
				// Waiting策略
				WaitStrategyFactory.getStrategy(option));
		this.disruptor.handleEventsWith((event, sequence, endOfBatch) -> tryCallProcessor(event));
		this.producer = new LoadContainerEventProducer(disruptor.getRingBuffer());
		if (autoRun)
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
	protected void startProcessThread() {
		disruptor.start();
	}

	@Override
	public void stop() {
		isStop.set(true);
		while (disruptor.getBufferSize() != 0)
			ThreadTool.sleep(10);
		disruptor.shutdown();
		log.info("Call stop() success, disruptor is shutdown.");
	}

	@Override
	public String name() {
		return "";
	}

	public static void main(String[] args) {

		SpscQueueWithSupplier<Integer> queue = new SpscQueueWithSupplier<>(BufferSize.POW2_10, true,
				WaitStrategyOption.BusySpin, () -> Integer.valueOf(0), (integer) -> System.out.println(integer));

		ThreadTool.startNewThread(() -> {
			int i = 0;
			for (;;) {
				queue.enqueue(++i);
				ThreadTool.sleep(5000);
			}
		});

		ThreadTool.sleep(10000);

	}

}
