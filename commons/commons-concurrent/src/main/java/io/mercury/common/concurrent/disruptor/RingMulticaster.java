package io.mercury.common.concurrent.disruptor;

import java.util.concurrent.atomic.LongAdder;

import org.slf4j.Logger;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.concurrent.disruptor.example.LongEvent;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.RunnableComponent;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;
import io.mercury.common.thread.Threads.ThreadPriority;
import io.mercury.common.util.BitOperator;
import io.mercury.common.util.StringSupport;

public final class RingMulticaster<T extends RingEvent, A> extends RunnableComponent {

	private static final Logger log = CommonLoggerFactory.getLogger(RingMulticaster.class);

	private final Disruptor<T> disruptor;

	private final EventPublisher publisher;

	/**
	 * 
	 * @param name
	 * @param eventType
	 * @param size
	 * @param option
	 * @param mode
	 * @param setter
	 * @param processors
	 */
	@SafeVarargs
	public RingMulticaster(String name, Class<T> eventType, int size, WaitStrategyOption option, StartMode mode,
			RingEventSetter<T, A> setter, Processor<T>... processors) {
		if (StringSupport.nonEmpty(name))
			super.name = name;
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
		for (var processor : processors)
			this.disruptor.handleEventsWith(new EventHandlerProxy(processor));
		// disruptor.handleExceptionsFor(null)
		this.publisher = new EventPublisher(disruptor.getRingBuffer(), setter);
		log.info("initialize disruptor -> {}, size -> {}, WaitStrategy -> {}, StartMode -> {}, Processor count -> {}",
				super.name, size, option, mode, processors.length);
		startWith(mode);
	}

	/**
	 * 包装处理器的事件处理代理
	 * 
	 * @author yellow013
	 */
	private class EventHandlerProxy implements EventHandler<T> {

		private final Processor<T> processor;

		private EventHandlerProxy(Processor<T> processor) {
			this.processor = processor;
		}

		@Override
		public void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
			try {
				processor.process(event);
			} catch (Exception e) {
				log.error("process event -> {} throw exception -> [{}]", event, e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}

	}

	private class EventPublisher {

		private final RingBuffer<T> ringBuffer;

		private final EventTranslatorOneArg<T, A> translator;

		private EventPublisher(RingBuffer<T> ringBuffer, RingEventSetter<T, A> setter) {
			this.ringBuffer = ringBuffer;
			this.translator = (T event, long sequence, A arg) -> setter.apply(event, arg);
		}

		private void onEvent(A a) {
			ringBuffer.publishEvent(translator, a);
		}
	}

	public boolean publishEvent(A a) {
		try {
			if (isClosed.get())
				return false;
			publisher.onEvent(a);
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

	@Override
	protected String getComponentType() {
		return "RingMulticaster";
	}

	public static void main(String[] args) {
		var p0 = new LongAdder();
		var p1 = new LongAdder();
		var p2 = new LongAdder();
		var multicaster = new RingMulticaster<LongEvent, Long>("Test-Multicaster", LongEvent.class, 32,
				WaitStrategyOption.LiteBlocking, StartMode.Auto, (LongEvent t, Long l) -> {
					return t.set(l);
				}, event -> {
					p0.increment();
				}, event -> {
					p1.increment();
				}, event -> {
					p2.increment();
				});
		Thread thread = Threads.startNewThread(() -> {
			for (long l = 0L; l < 1000; l++)
				multicaster.publishEvent(l);
		});

		SleepSupport.sleep(2000);
		System.out.println(p0.intValue() + " - " + p1.intValue() + " - " + p2.intValue());
		multicaster.stop();
		thread.interrupt();
	}

}
