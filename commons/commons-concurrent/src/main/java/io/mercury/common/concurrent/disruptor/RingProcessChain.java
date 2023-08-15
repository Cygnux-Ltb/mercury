package io.mercury.common.concurrent.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import io.mercury.common.collections.CollectionUtil;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.functional.Processor;
import io.mercury.common.lang.Throws;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.util.StringSupport;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.time.LocalDateTime;
import java.util.List;

import static io.mercury.common.concurrent.disruptor.CommonWaitStrategy.Sleeping;
import static io.mercury.common.concurrent.disruptor.CommonWaitStrategy.Yielding;
import static io.mercury.common.concurrent.disruptor.ReflectionEventFactory.newFactory;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSS;
import static io.mercury.common.lang.Asserter.nonNull;
import static io.mercury.common.sys.CurrentRuntime.availableProcessors;

/**
 * @param <E>
 * @author yellow013
 */
public class RingProcessChain<E, I> extends AbstractRingBuffer<E, I> {

    private static final Logger log = Log4j2LoggerFactory.getLogger(RingProcessChain.class);

    /**
     * @param name        String
     * @param size        int
     * @param factory     EventFactory<E>
     * @param type        ProducerType
     * @param strategy    WaitStrategy
     * @param mode        StartMode
     * @param translator  EventTranslatorOneArg<E, I>
     * @param handlersMap MutableIntObjectMap<List<EventHandler<E>>>
     */
    private RingProcessChain(@Nonnull String name, int size, @Nonnull EventFactory<E> factory,
                             @Nonnull ProducerType type, @Nonnull StartMode mode,
                             @Nonnull WaitStrategy strategy,
                             @Nonnull EventTranslatorOneArg<E, I> translator,
                             @Nonnull MutableIntObjectMap<List<EventHandler<E>>> handlersMap) {
        super(name, size, factory, type, strategy, translator);
        int[] keys = handlersMap.keySet().toSortedArray();
        List<EventHandler<E>> handlers0 = handlersMap.get(keys[0]);
        if (keys.length == 1) {
            disruptor.handleEventsWith(CollectionUtil
                    .<EventHandler<E>>toArray(handlers0, EventHandler[]::new));
        } else {
            EventHandlerGroup<E> handlerGroup = disruptor.handleEventsWith(CollectionUtil
                    .<EventHandler<E>>toArray(handlers0, EventHandler[]::new));

            for (int i = 1; i < keys.length; i++) {
                // 将处理器以处理链的方式添加进Disruptor
                List<EventHandler<E>> handlers = handlersMap.get(keys[i]);
                handlerGroup.then(CollectionUtil
                        .<EventHandler<E>>toArray(handlers, EventHandler[]::new));
            }
        }
        log.info(
                "Initialized RingProcessChain -> {}, size -> {}, WaitStrategy -> {}, StartMode -> {}, EventHandler china level count -> {}",
                this.name, size, strategy, mode, handlersMap.size());
        startWith(mode);
    }

    // **************** 单生产者处理链 ****************//

    public static <E, I> Builder<E, I> withSingleProducer(
            @Nonnull Class<E> eventType,
            @Nonnull RingEventPublisher<E, I> publisher) {
        return withSingleProducer(
                // 使用反射EventFactory
                newFactory(eventType, log), publisher);
    }

    /**
     * @param eventType       Class<E>
     * @param eventTranslator EventTranslatorOneArg<E, I>
     * @param <E>             handle type
     * @param <I>             input type
     * @return Builder<E, I>
     */
    public static <E, I> Builder<E, I> withSingleProducer(
            @Nonnull Class<E> eventType,
            @Nonnull EventTranslatorOneArg<E, I> eventTranslator) {
        return withSingleProducer(
                // 使用反射EventFactory
                newFactory(eventType, log), eventTranslator);
    }

    public static <E, I> Builder<E, I> withSingleProducer(@Nonnull EventFactory<E> eventFactory,
                                                          @Nonnull RingEventPublisher<E, I> publisher) {
        return withSingleProducer(eventFactory,
                // EventTranslator实现函数, 负责调用处理In对象到Event对象之间的转换
                (event, sequence, in) -> publisher.accept(event, in));
    }

    public static <E, I> Builder<E, I> withSingleProducer(@Nonnull EventFactory<E> eventFactory,
                                                          @Nonnull EventTranslatorOneArg<E, I> eventTranslator) {
        return new Builder<>(ProducerType.SINGLE, eventFactory, eventTranslator);
    }

    // **************** 多生产者处理链 ****************//

    public static <E, I> Builder<E, I> withMultiProducer(@Nonnull Class<E> eventType,
                                                         @Nonnull RingEventPublisher<E, I> publisher) {
        return withMultiProducer(
                // 使用反射EventFactory
                newFactory(eventType, log), publisher);
    }

    public static <E, I> Builder<E, I> withMultiProducer(@Nonnull Class<E> eventType,
                                                         @Nonnull EventTranslatorOneArg<E, I> eventTranslator) {
        return withMultiProducer(
                // 使用反射EventFactory
                newFactory(eventType, log), eventTranslator);
    }

    public static <E, I> Builder<E, I> withMultiProducer(@Nonnull EventFactory<E> eventFactory,
                                                         @Nonnull RingEventPublisher<E, I> publisher) {
        return withMultiProducer(eventFactory,
                // EventTranslator实现函数, 负责调用处理In对象到Event对象之间的转换
                (event, sequence, in) -> publisher.accept(event, in));
    }

    public static <E, I> Builder<E, I> withMultiProducer(@Nonnull EventFactory<E> eventFactory,
                                                         @Nonnull EventTranslatorOneArg<E, I> eventTranslator) {
        return new Builder<>(ProducerType.SINGLE, eventFactory, eventTranslator);
    }

    @NotThreadSafe
    public static class Builder<E, I> {

        private String name;
        private int size = 64;
        private StartMode mode = StartMode.auto();
        private WaitStrategy waitStrategy;
        private final ProducerType producerType;
        private final EventFactory<E> eventFactory;
        private final EventTranslatorOneArg<E, I> eventTranslator;
        private final MutableIntObjectMap<List<EventHandler<E>>> handlersMap = MutableMaps.newIntObjectHashMap();

        private Builder(ProducerType producerType, EventFactory<E> eventFactory,
                        EventTranslatorOneArg<E, I> eventTranslator) {
            this.producerType = producerType;
            this.eventFactory = eventFactory;
            this.eventTranslator = eventTranslator;
        }

        public Builder<E, I> addFirstProcessor(@Nonnull Processor<E> processor) {
            return addProcessor(Integer.MIN_VALUE, processor);
        }

        public Builder<E, I> addSecondProcessor(@Nonnull Processor<E> processor) {
            return addProcessor(Integer.MIN_VALUE + 1, processor);
        }

        public Builder<E, I> addLastProcessor(@Nonnull Processor<E> processor) {
            return addProcessor(Integer.MAX_VALUE, processor);
        }

        public Builder<E, I> addProcessor(int level, @Nonnull Processor<E> processor) {
            nonNull(processor, "processor");
            return addHandler(level,
                    // 将Processor实现加载到HandlerWrapper中
                    new EventHandlerWrapper<>(processor, log));
        }

        public Builder<E, I> addFirstHandler(@Nonnull EventHandler<E> handler) {
            return addHandler(Integer.MIN_VALUE, handler);
        }

        public Builder<E, I> addSecondHandler(@Nonnull EventHandler<E> handler) {
            return addHandler(Integer.MIN_VALUE + 1, handler);
        }

        public Builder<E, I> addLastHandler(@Nonnull EventHandler<E> handler) {
            return addHandler(Integer.MAX_VALUE, handler);
        }

        public Builder<E, I> addHandler(int level, @Nonnull EventHandler<E> handler) {
            nonNull(handler, "handler");
            handlersMap.getIfAbsentPut(level, MutableLists::newFastList).add(handler);
            return this;
        }

        public Builder<E, I> name(String name) {
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

        public RingProcessChain<E, I> create() {
            if (handlersMap.isEmpty())
                Throws.illegalArgument("handlersMap");
            if (waitStrategy == null)
                waitStrategy = handlersMap.stream().mapToInt(List::size).sum() > availableProcessors() ? Sleeping.get()
                        : Yielding.get();
            if (StringSupport.isNullOrEmpty(name))
                name = "RingProcessChain-" + YYYYMMDD_L_HHMMSSSSS.fmt(LocalDateTime.now());
            return new RingProcessChain<>(name, size, eventFactory, producerType,
                    mode, waitStrategy, eventTranslator, handlersMap);
        }

    }

}
