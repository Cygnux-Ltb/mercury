package io.mercury.common.concurrent.ring;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.mercury.common.concurrent.ring.base.EventHandlerWrapper;
import io.mercury.common.concurrent.ring.base.EventPublisher;
import io.mercury.common.functional.Processor;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.thread.MaxPriorityThreadFactory;
import io.mercury.common.thread.RunnableComponent;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.lmax.disruptor.dsl.ProducerType.MULTI;
import static io.mercury.common.concurrent.ring.base.WaitStrategyOption.Yielding;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSS;
import static java.util.Objects.requireNonNullElse;

/**
 * @param <E>
 * @author yellow013
 */
public final class RingEventBus<E> extends RunnableComponent {

    private static final Logger log = Log4j2LoggerFactory.getLogger(RingEventBus.class);

    private final Disruptor<E> disruptor;

    private final EventHandleGraph<E> handleGraph;

    private final boolean isMultiProducer;

    private RingEventBus(String name, int size, StartMode startMode,
                         ProducerType type, EventFactory<E> eventFactory,
                         WaitStrategy waitStrategy, EventHandleGraph<E> handleGraph) {
        this.name = Objects.requireNonNullElse(name,
                "RingEventBus-[" + YYYYMMDD_L_HHMMSSSSS.fmt(LocalDateTime.now()) + "]");
        final ProducerType producerType = requireNonNullElse(type, MULTI);
        this.disruptor = new Disruptor<>(
                // EventFactory, 队列容量
                eventFactory, size,
                // ThreadFactory
                new MaxPriorityThreadFactory(this.name + "-worker"),
                // 生产者策略, Waiting策略
                producerType, waitStrategy);
        this.handleGraph = handleGraph;
        this.handleGraph.deploy(this.disruptor);
        this.isMultiProducer = producerType == MULTI;
        startWith(startMode);
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

    public static <E> Wizard<E> withMultiProducer(EventFactory<E> eventFactory) {
        return new Wizard<>(ProducerType.MULTI, eventFactory);
    }

    public static <E> Wizard<E> withSingleProducer(EventFactory<E> eventFactory) {
        return new Wizard<>(ProducerType.SINGLE, eventFactory);
    }

    /**
     * @param translator EventTranslatorOneArg<E, A>
     * @param <A>        another object type
     * @return the new EventPublisher<E, A> object
     */
    public <A> EventPublisher<E, A> newPublisher(EventTranslatorOneArg<E, A> translator)
            throws IllegalStateException {
        if (isMultiProducer)
            return new EventPublisher<>(disruptor.getRingBuffer(), translator);
        else {
            log.error("RingBuffer -> {} is not multi producer mode", name);
            throw new IllegalStateException("isMultiProducer == false");
        }
    }


    public static class Wizard<E> {

        private final ProducerType producerType;
        private final EventFactory<E> eventFactory;
        private String name;
        private int size = 64;
        private StartMode startMode = StartMode.auto();
        private WaitStrategy waitStrategy = Yielding.get();

        private Wizard(ProducerType producerType, EventFactory<E> eventFactory) {
            this.producerType = producerType;
            this.eventFactory = eventFactory;
        }

        public Wizard<E> name(String name) {
            this.name = name;
            return this;
        }

        public Wizard<E> size(int size) {
            this.size = size;
            return this;
        }

        public Wizard<E> startMode(StartMode startMode) {
            this.startMode = startMode;
            return this;
        }

        public Wizard<E> waitStrategy(WaitStrategy strategy) {
            this.waitStrategy = strategy;
            return this;
        }

        public RingEventBus<E> process(Processor<E> processor) {
            return process(new EventHandlerWrapper<>(processor, log));
        }

        public RingEventBus<E> process(EventHandler<E> handler) {
            return process(EventHandleGraph.single(handler));
        }

        public RingEventBus<E> process(EventHandleGraph<E> handleGraph) {
            return new RingEventBus<>(name, size, startMode, producerType,
                    eventFactory, waitStrategy, handleGraph);
        }

    }


}
