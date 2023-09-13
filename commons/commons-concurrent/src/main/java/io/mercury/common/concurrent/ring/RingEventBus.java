package io.mercury.common.concurrent.ring;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.mercury.common.concurrent.ring.base.EventHandlerWrapper;
import io.mercury.common.concurrent.ring.base.EventPublisher;
import io.mercury.common.concurrent.ring.base.HandlerGraph;
import io.mercury.common.functional.Processor;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.thread.MaxPriorityThreadFactory;
import io.mercury.common.thread.RunnableComponent;
import org.slf4j.Logger;

import java.time.LocalDateTime;

import static com.lmax.disruptor.dsl.ProducerType.MULTI;
import static io.mercury.common.concurrent.ring.base.ReflectionEventFactory.newFactory;
import static io.mercury.common.concurrent.ring.base.WaitStrategyOption.Yielding;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSS;
import static io.mercury.common.util.BitOperator.minPow2;
import static java.util.Objects.requireNonNullElse;

/**
 * @param <E>
 * @author yellow013
 */
public class RingEventBus<E> extends RunnableComponent {

    private static final Logger log = Log4j2LoggerFactory.getLogger(RingEventBus.class);

    protected final Disruptor<E> disruptor;

    protected final HandlerGraph<E> handleGraph;

    protected final RingBuffer<E> ringBuffer;

    protected final boolean isMultiProducer;

    protected RingEventBus(String name, int size, StartMode startMode,
                           ProducerType type, EventFactory<E> eventFactory,
                           WaitStrategy waitStrategy, HandlerGraph<E> handleGraph) {
        this.name = requireNonNullElse(name,
                "RingEventBus-[" + YYYYMMDD_L_HHMMSSSSS.fmt(LocalDateTime.now()) + "]");
        final ProducerType producerType = requireNonNullElse(type, MULTI);
        this.disruptor = new Disruptor<>(
                // EventFactory, 队列容量
                eventFactory, adjustSize(size),
                // ThreadFactory
                new MaxPriorityThreadFactory(this.name + "-worker"),
                // 生产者策略, Waiting策略
                producerType, waitStrategy);
        this.handleGraph = handleGraph;
        this.handleGraph.deploy(this.disruptor);
        this.ringBuffer = this.disruptor.getRingBuffer();
        this.isMultiProducer = (producerType == MULTI);
        startWith(startMode);
    }

    /**
     * 调整队列容量, 最小16, 最大65536, 其他输入参数自动调整为最接近的2次幂
     *
     * @param size buffer size
     * @return int
     */
    private int adjustSize(int size) {
        return size < 16 ? 16 : size > 65536 ? 65536 : minPow2(size);
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
     * @param eventType EventFactory<E>
     * @param <E>       Class type
     * @return Wizard<E>
     */
    public static <E> Wizard<E> multiProducer(Class<E> eventType) {
        return multiProducer(newFactory(eventType, log));
    }

    /**
     * @param factory EventFactory<E>
     * @param <E>     Class type
     * @return Wizard<E>
     */
    public static <E> Wizard<E> multiProducer(EventFactory<E> factory) {
        return new Wizard<>(MULTI, factory);
    }

    /**
     * @param eventType EventFactory<E>
     * @param <E>       Class type
     * @return Wizard<E>
     */
    public static <E> Wizard<E> singleProducer(Class<E> eventType) {
        return singleProducer(newFactory(eventType, log));
    }

    /**
     * @param factory EventFactory<E>
     * @param <E>     Class type
     * @return Wizard<E>
     */
    public static <E> Wizard<E> singleProducer(EventFactory<E> factory) {
        return new Wizard<>(ProducerType.SINGLE, factory);
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

    /**
     * @param translator EventTranslatorOneArg<E, A>
     * @param arg        A
     * @param <A>        Arg type
     */
    public <A> void publish(EventTranslatorOneArg<E, A> translator, A arg) {
        ringBuffer.publishEvent(translator, arg);
    }

    /**
     * @param translator EventTranslatorTwoArg<E, A0, A1>
     * @param arg0       A0
     * @param arg1       A1
     * @param <A0>       A0 Type
     * @param <A1>       A1 Type
     */
    public <A0, A1> void publish(EventTranslatorTwoArg<E, A0, A1> translator, A0 arg0, A1 arg1) {
        ringBuffer.publishEvent(translator, arg0, arg1);
    }

    /**
     * @param <E>
     */
    public static class Wizard<E> {

        private final ProducerType producerType;
        private final EventFactory<E> eventFactory;

        private String name = null;
        private int size = 128;
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

        public Wizard<E> waitStrategy(WaitStrategy strategy) {
            this.waitStrategy = strategy;
            return this;
        }

        public Wizard<E> startMode(StartMode startMode) {
            this.startMode = startMode;
            return this;
        }

        public RingEventBus<E> process(Processor<E> processor) {
            return process(new EventHandlerWrapper<>(processor, log));
        }

        public RingEventBus<E> process(EventHandler<E> handler) {
            return process(HandlerGraph.single(handler));
        }

        public RingEventBus<E> process(HandlerGraph<E> handlerGraph) {
            return new RingEventBus<>(name, size, startMode, producerType,
                    eventFactory, waitStrategy, handlerGraph);
        }

    }


}
