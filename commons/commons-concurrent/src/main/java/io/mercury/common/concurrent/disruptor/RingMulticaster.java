package io.mercury.common.concurrent.disruptor;

import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;

import io.mercury.common.collections.CollectionUtil;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;

/**
 * @author yellow013
 * 
 * @param <E> 进行处理的类型
 * @param <I> 发布的数据类型
 */
public final class RingMulticaster<E, I> extends SingleProducerRingBuffer<E, I> {

	private static final Logger log = CommonLoggerFactory.getLogger(RingMulticaster.class);

//	@SafeVarargs
//	public RingMulticaster(String name, int size, @Nonnull EventFactory<E> eventFactory,
//			@Nonnull WaitStrategyOption option, @Nonnull StartMode mode, @Nonnull RingEventPublisher<E, I> publisher,
//			@Nonnull Processor<E>... processors) {
//		this(name, size, eventFactory, option, mode, publisher,
//				// 通过实现供应器接口进行类型转换, 对泛型进行明确声明.
//				// 避开可变长参数的数组类型自动推导导致的无法正确识别构造函数的问题
//				new Supplier<List<EventHandler<E>>>() {
//					public List<EventHandler<E>> get() {
//						return Stream.of(Assertor.requiredLength(processors, 1, "processors"))
//								.map(EventHandlerProxy::new).collect(Collectors.toList());
//					}
//				}.get());
//	}

	/**
	 * 
	 * @param name
	 * @param size
	 * @param eventFactory
	 * @param option
	 * @param mode
	 * @param translator
	 * @param handlers
	 */
	
	@SuppressWarnings("unchecked")
	public RingMulticaster(String name, int size, @Nonnull EventFactory<E> eventFactory,
			@Nonnull WaitStrategyOption option, @Nonnull StartMode mode,
			@Nonnull EventTranslatorOneArg<E, I> translator, @Nonnull List<EventHandler<E>> handlers) {
		super(name, size, eventFactory, option, translator);
		Assertor.requiredLength(handlers, 1, "handlers");
		// 将处理器添加进Disruptor中, 各个处理器进行并行处理
		super.disruptor.handleEventsWith(CollectionUtil.toArray(handlers, length -> {
			return (EventHandler<E>[]) new EventHandler[length];
		}));
		log.info(
				"Initialize RingMulticaster -> {}, size -> {}, WaitStrategy -> {}, StartMode -> {}, EventHandler count -> {}",
				super.name, size, option, mode, handlers.size());
		startWith(mode);
	}

	public static <E, I> Builder<E, I> newBuilder(@Nonnull Class<E> eventType,
			@Nonnull RingEventPublisher<E, I> publisher) {
		return newBuilder(
				// 使用反射EventFactory
				ReflectionEventFactory.with(eventType, log), publisher);
	}

	public static <E, I> Builder<E, I> newBuilder(@Nonnull Class<E> eventType,
			@Nonnull EventTranslatorOneArg<E, I> translator) {
		return newBuilder(
				// 使用反射EventFactory
				ReflectionEventFactory.with(eventType, log), translator);
	}

	public static <E, I> Builder<E, I> newBuilder(EventFactory<E> eventFactory,
			@Nonnull RingEventPublisher<E, I> publisher) {
		return newBuilder(eventFactory,
				// EventTranslator实现函数, 负责调用处理In对象到Event对象之间的转换
				(event, sequence, in) -> publisher.accept(event, in));
	}

	public static <E, I> Builder<E, I> newBuilder(EventFactory<E> eventFactory,
			@Nonnull EventTranslatorOneArg<E, I> translator) {
		return new Builder<>(eventFactory, translator);
	}

	@Override
	protected String getComponentType() {
		return "RingMulticaster";
	}

	public static class Builder<E, I> {

		private String name;
		private int size = 64;
		private WaitStrategyOption option = WaitStrategyOption.BusySpin;
		private StartMode mode = StartMode.Auto;
		private final EventFactory<E> eventFactory;
		private final EventTranslatorOneArg<E, I> translator;
		private final List<EventHandler<E>> handlers = MutableLists.newFastList();

		private Builder(EventFactory<E> eventFactory, EventTranslatorOneArg<E, I> translator) {
			this.eventFactory = eventFactory;
			this.translator = translator;
		}

		public Builder<E, I> setProcessor(@Nonnull Processor<E> processor) {
			Assertor.nonNull(processor, "processor");
			this.handlers.add(
					// 将Processor实现加载到HandlerProxy中
					new EventHandlerProxy<>(processor, log));
			return this;
		}

		public Builder<E, I> setHandler(@Nonnull EventHandler<E> handler) {
			Assertor.nonNull(handler, "handler");
			this.handlers.add(handler);
			return this;
		}

		public Builder<E, I> name(String name) {
			this.name = name;
			return this;
		}

		public Builder<E, I> setWaitStrategy(WaitStrategyOption option) {
			this.option = option;
			return this;
		}

		public Builder<E, I> setStartMode(StartMode mode) {
			this.mode = mode;
			return this;
		}

		public Builder<E, I> size(int size) {
			this.size = size;
			return this;
		}

		public RingMulticaster<E, I> create() {
			return new RingMulticaster<>(name, size, eventFactory, option, mode, translator, handlers);
		}

	}

}
