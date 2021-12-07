package io.mercury.common.concurrent.disruptor;

import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSS;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.lang.Assertor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.MaxPriorityThreadFactory;
import io.mercury.common.thread.RunnableComponent;
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
abstract class SingleProducerRingBuffer<E, I> extends RunnableComponent {

	private static final Logger log = CommonLoggerFactory.getLogger(SingleProducerRingBuffer.class);

	protected final Disruptor<E> disruptor;

	protected final EventPublisherWrapper<E, I> publisherWrapper;

	protected SingleProducerRingBuffer(String name, int size, @Nonnull EventFactory<E> eventFactory,
			@Nonnull WaitStrategy waitStrategy, @Nonnull EventTranslatorOneArg<E, I> translator) {
		Assertor.nonNull(eventFactory, "eventFactory");
		Assertor.nonNull(waitStrategy, "waitStrategy");
		Assertor.nonNull(translator, "translator");
		if (StringSupport.nonEmpty(name))
			super.name = name;
		else
			super.name = "SP-RingBuffer-" + YYYYMMDD_L_HHMMSSSSS.format(LocalDateTime.now());
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
				waitStrategy);
		this.publisherWrapper = new EventPublisherWrapper<>(disruptor.getRingBuffer(), translator);
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
			publisherWrapper.handle(in);
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

}
