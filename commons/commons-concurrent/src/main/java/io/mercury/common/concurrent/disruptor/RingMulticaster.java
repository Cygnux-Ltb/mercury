package io.mercury.common.disruptor;

import org.slf4j.Logger;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.RunnableComponent;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;
import io.mercury.common.thread.Threads.ThreadPriority;
import io.mercury.common.util.BitOperator;
import io.mercury.common.util.StringSupport;

public final class RingMulticaster<T extends RingEvent> extends RunnableComponent {

	private static final Logger log = CommonLoggerFactory.getLogger(RingMulticaster.class);

	private final Disruptor<T> disruptor;

	private final LoadContainerEventProducer producer;

	@SafeVarargs
	public RingMulticaster(String queueName, Class<T> eventType, int size, WaitStrategyOption option, StartMode mode,
			Processor<T>... processor) {
		if (StringSupport.nonEmpty(queueName))
			super.name = queueName;
		// if (queueSize == 0 || queueSize % 2 != 0)
		// throw new IllegalArgumentException("queueSize set error...");
		this.disruptor = new Disruptor<>(
				// 使用反射EventFactory
				ReflectionEventFactory.with(eventType),
				// 设置队列容量, 最小16
				size < 16 ? 16 : BitOperator.minPow2(size),
				// 使用最高优先级线程工厂
				Threads.newThreadFactory(super.name + "-Worker", ThreadPriority.MAX),
				// 生产者策略, 使用单生产者
				ProducerType.SINGLE,
				// Waiting策略
				WaitStrategyFactory.getStrategy(option));
		this.disruptor.handleEventsWith(this::callProcessor);
		this.producer = new LoadContainerEventProducer(disruptor.getRingBuffer());
		startWith(mode);
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

	public boolean publishEvent(T t) {
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
		log.info("Disruptor::start() func execution succeed, {} is start", name);
	}

	@Override
	protected void stop0() {
		while (disruptor.getBufferSize() != 0)
			SleepSupport.sleep(1);
		disruptor.shutdown();
		log.info("Disruptor::shutdown() func execution succeed, {} is shutdown", name);
	}

	public static void main(String[] args) {

	}


	@Override
	protected String getComponentType() {
		// TODO Auto-generated method stub
		return null;
	}

}
