package io.mercury.common.concurrent.disruptor;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.MaxPriorityThreadFactory;
import io.mercury.common.thread.RunnableComponent;
import io.mercury.common.util.Assertor;
import io.mercury.common.util.BitOperator;
import io.mercury.common.util.StringSupport;

/**
 * 
 * 单生产者Disruptor实现
 * 
 * @author yellow013
 *
 * @param <E> 事件处理类型
 * @param <I> 发布类型
 */
public abstract class SingleProducerRingBuffer<E, I> extends RunnableComponent {

	private static final Logger log = CommonLoggerFactory.getLogger(SingleProducerRingBuffer.class);

	private final Disruptor<E> disruptor;

	protected final EventPublisherProxy publisherProxy;

	protected SingleProducerRingBuffer(String name, int size, @Nonnull EventFactory<E> eventFactory,
			@Nonnull WaitStrategyOption option, @Nonnull EventTranslatorOneArg<E, I> translator) {
		Assertor.nonNull(eventFactory, "eventFactory");
		Assertor.nonNull(option, "option");
		Assertor.nonNull(translator, "translator");
		if (StringSupport.nonEmpty(name))
			super.name = name;
		this.disruptor = new Disruptor<>(
				// 事件工厂
				eventFactory,
				// 调整并设置队列容量
				regulateSize(size),
				// 使用最高优先级的线程工厂
				new MaxPriorityThreadFactory(super.name + "-worker"),
				// 生产者策略, 使用单生产者
				ProducerType.SINGLE,
				// Waiting策略
				WaitStrategyFactory.getStrategy(option));
		this.publisherProxy = new EventPublisherProxy(disruptor.getRingBuffer(), translator);
	}

	public Disruptor<E> getDisruptor() {
		return disruptor;
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

	/**
	 * 
	 * @param in
	 * @return
	 */
	public boolean publishEvent(I in) {
		try {
			if (isClosed.get())
				return false;
			publisherProxy.handle(in);
			return true;
		} catch (Exception e) {
			log.error("{} -> EventPublisher::handle(in) func throws exception -> [{}]", name, e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 调整队列容量, 最小16, 最大65536, 其他输入参数自动调整为最接近的2次幂
	 * 
	 * @param size
	 * @return
	 */
	private int regulateSize(int size) {
		return size < 16 ? 16 : size > 65536 ? 65536 : BitOperator.minPow2(size);
	}

	/**
	 * 内部发布者, 用于调用RingBuffer对象的publishEvent函数,
	 * 
	 * 并负责传递EventTranslator实现
	 * 
	 * @author yellow013
	 */
	private class EventPublisherProxy {

		private final RingBuffer<E> ringBuffer;

		private final EventTranslatorOneArg<E, I> translator;

		private EventPublisherProxy(RingBuffer<E> ringBuffer, EventTranslatorOneArg<E, I> translator) {
			this.ringBuffer = ringBuffer;
			this.translator = translator;
		}

		private void handle(I in) {
			ringBuffer.publishEvent(translator, in);
		}
	}

	/**
	 * 包装处理器的事件处理代理
	 * 
	 * @author yellow013
	 */
	protected static class EventHandlerProxy<E> implements EventHandler<E> {

		private final Processor<E> processor;

		protected EventHandlerProxy(Processor<E> processor) {
			this.processor = processor;
		}

		@Override
		public void onEvent(E event, long sequence, boolean endOfBatch) throws Exception {
			try {
				processor.process(event);
			} catch (Exception e) {
				log.error("process event -> {} throw exception -> [{}]", event, e.getMessage(), e);
				throw e;
			}
		}
	}

}
