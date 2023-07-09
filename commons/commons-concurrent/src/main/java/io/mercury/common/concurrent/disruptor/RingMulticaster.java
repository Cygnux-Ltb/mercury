package io.mercury.common.concurrent.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.functional.Processor;
import io.mercury.common.lang.Asserter;
import io.mercury.common.lang.Throws;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.util.StringSupport;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static io.mercury.common.collections.CollectionUtil.toArray;
import static io.mercury.common.concurrent.disruptor.CommonWaitStrategy.Sleeping;
import static io.mercury.common.concurrent.disruptor.CommonWaitStrategy.Yielding;
import static io.mercury.common.concurrent.disruptor.ReflectionEventFactory.newFactory;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSS;
import static io.mercury.common.sys.CurrentRuntime.availableProcessors;

/**
 * @param <E> 进行处理的类型
 * @param <I> 发布的数据类型
 * @author yellow013
 */
public final class RingMulticaster<E, I> extends AbstractRingBuffer<E, I> {

    private static final Logger log = Log4j2LoggerFactory.getLogger(RingMulticaster.class);

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
     * @param name       Multicaster name
     * @param size       RingBuffer size
     * @param factory    EventFactory
     * @param type       EventType
     * @param strategy   WaitStrategy
     * @param mode       StartMode
     * @param translator EventTranslator
     * @param handlers   EventHandler collection
     */
    private RingMulticaster(
            @Nonnull String name, int size, @Nonnull EventFactory<E> factory,
            @Nonnull ProducerType type, @Nonnull WaitStrategy strategy,
            @Nonnull StartMode mode, @Nonnull EventTranslatorOneArg<E, I> translator,
            @Nonnull Collection<EventHandler<E>> handlers) {
        super(name, size, factory, type, strategy, translator);
        Asserter.requiredLength(handlers, 1, "handlers");
        // 将处理器添加进Disruptor中, 各个处理器进行并行处理
        disruptor.handleEventsWith(toArray(handlers, EventHandler[]::new));
        log.info(
                "Initialized RingMulticaster -> {}, size -> {}, WaitStrategy -> {}, StartMode -> {}, EventHandler count -> {}",
                this.name, size, strategy, mode, handlers.size());
        startWith(mode);
    }

    // **************** 单生产者广播器 ****************//
    public static <E, I> Builder<E, I> withSingleProducer(
            @Nonnull Class<E> eventType,
            @Nonnull RingEventPublisher<E, I> publisher) {
        return withSingleProducer(
                // 使用反射EventFactory
                newFactory(eventType, log), publisher);
    }

    public static <E, I> Builder<E, I> withSingleProducer(
            @Nonnull Class<E> eventType,
            @Nonnull EventTranslatorOneArg<E, I> translator) {
        return withSingleProducer(
                // 使用反射EventFactory
                newFactory(eventType, log), translator);
    }

    public static <E, I> Builder<E, I> withSingleProducer(
            @Nonnull EventFactory<E> eventFactory,
            @Nonnull RingEventPublisher<E, I> publisher) {
        return withSingleProducer(eventFactory,
                // EventTranslator实现函数, 负责调用处理In对象到Event对象之间的转换
                (event, sequence, in) -> publisher.accept(event, in));
    }

    public static <E, I> Builder<E, I> withSingleProducer(
            @Nonnull EventFactory<E> eventFactory,
            @Nonnull EventTranslatorOneArg<E, I> translator) {
        return new Builder<>(ProducerType.SINGLE, eventFactory, translator);
    }

    // **************** 多生产者广播器 ****************//
    public static <E, I> Builder<E, I> withMultiProducer(
            @Nonnull Class<E> eventType,
            @Nonnull RingEventPublisher<E, I> publisher) {
        return withMultiProducer(
                // 使用反射EventFactory
                newFactory(eventType, log), publisher);
    }

    public static <E, I> Builder<E, I> withMultiProducer(
            @Nonnull Class<E> eventType,
            @Nonnull EventTranslatorOneArg<E, I> translator) {
        return withMultiProducer(
                // 使用反射EventFactory
                newFactory(eventType, log), translator);
    }

    public static <E, I> Builder<E, I> withMultiProducer(
            @Nonnull EventFactory<E> eventFactory,
            @Nonnull RingEventPublisher<E, I> publisher) {
        return withMultiProducer(eventFactory,
                // EventTranslator实现函数, 负责调用处理In对象到Event对象之间的转换
                (event, sequence, in) -> publisher.accept(event, in));
    }

    public static <E, I> Builder<E, I> withMultiProducer(
            @Nonnull EventFactory<E> eventFactory,
            @Nonnull EventTranslatorOneArg<E, I> translator) {
        return new Builder<>(ProducerType.MULTI, eventFactory, translator);
    }


    public static class Builder<E, I> {

        private String name;
        private int size = 64;
        private StartMode mode = StartMode.auto();
        private WaitStrategy waitStrategy;
        private final ProducerType producerType;
        private final EventFactory<E> eventFactory;
        private final EventTranslatorOneArg<E, I> eventTranslator;
        private final List<EventHandler<E>> handlers = MutableLists.newFastList();

        private Builder(ProducerType producerType, EventFactory<E> eventFactory,
                        EventTranslatorOneArg<E, I> translator) {
            this.producerType = producerType;
            this.eventFactory = eventFactory;
            this.eventTranslator = translator;
        }

        public Builder<E, I> addProcessor(@Nonnull Processor<E> processor) {
            Asserter.nonNull(processor, "processor");
            return addHandler(
                    // 将Processor实现加载到HandlerWrapper中
                    new EventHandlerWrapper<>(processor, log));
        }

        public Builder<E, I> addHandler(@Nonnull EventHandler<E> handler) {
            Asserter.nonNull(handler, "handler");
            this.handlers.add(handler);
            return this;
        }

        public Builder<E, I> setName(String name) {
            this.name = name;
            return this;
        }

        public Builder<E, I> setWaitStrategy(CommonWaitStrategy waitStrategy) {
            return setWaitStrategy(waitStrategy.get());
        }

        public Builder<E, I> setWaitStrategy(WaitStrategy waitStrategy) {
            this.waitStrategy = waitStrategy;
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
            if (handlers.isEmpty())
                Throws.illegalArgument("handlers");
            if (waitStrategy == null)
                waitStrategy = handlers.size() > availableProcessors() ? Sleeping.get() : Yielding.get();
            if (StringSupport.isNullOrEmpty(name))
                name = "RingMulticaster-" + YYYYMMDD_L_HHMMSSSSS.fmt(LocalDateTime.now());
            return new RingMulticaster<>(name, size, eventFactory, producerType,
                    waitStrategy, mode, eventTranslator, handlers);
        }

    }

}
