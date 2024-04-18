package io.mercury.common.concurrent.ring;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.mercury.common.concurrent.ring.RingEventbus.MpRingEventbus.MpWizard;
import io.mercury.common.concurrent.ring.RingEventbus.SpRingEventbus.SpWizard;
import io.mercury.common.concurrent.ring.base.EventHandlerWrapper;
import io.mercury.common.concurrent.ring.base.EventPublisher;
import io.mercury.common.concurrent.ring.base.HandlerGraph;
import io.mercury.common.functional.Processor;
import io.mercury.common.thread.RunnableComponent;
import org.slf4j.Logger;

import java.time.LocalDateTime;

import static com.lmax.disruptor.dsl.ProducerType.MULTI;
import static com.lmax.disruptor.dsl.ProducerType.SINGLE;
import static io.mercury.common.concurrent.ring.base.ReflectionEventFactory.newFactory;
import static io.mercury.common.concurrent.ring.base.WaitStrategyOption.Yielding;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_L_HHMMSSSSS;
import static io.mercury.common.log4j2.Log4j2LoggerFactory.getLogger;
import static io.mercury.common.thread.ThreadFactoryImpl.ofPlatform;
import static io.mercury.common.thread.ThreadPriority.MAX;
import static io.mercury.common.util.BitOperator.minPow2;
import static java.util.Objects.requireNonNullElse;

/**
 * @param <E>
 * @author yellow013
 * <p>
 * 扩展多写和单写
 */
public abstract class RingEventbus<E> extends RunnableComponent {

    private static final Logger log = getLogger(RingEventbus.class);

    protected final Disruptor<E> disruptor;

    protected final HandlerGraph<E> handleGraph;

    protected final RingBuffer<E> ringBuffer;

    protected final boolean isMultiProducer;

    protected RingEventbus(String name, int size, StartMode startMode,
                           ProducerType type, EventFactory<E> eventFactory,
                           WaitStrategy waitStrategy, HandlerGraph<E> handlerGraph) {
        this.name = requireNonNullElse(name,
                "RingEventbus-[" + YYMMDD_L_HHMMSSSSS.fmt(LocalDateTime.now()) + "]");
        final ProducerType producerType = requireNonNullElse(type, MULTI);
        this.disruptor = new Disruptor<>(
                // EventFactory, 队列容量
                eventFactory, adjustSize(size),
                // ThreadFactory
                ofPlatform(this.name + "-worker").priority(MAX).build(),
                // 生产者策略, Waiting策略
                producerType, waitStrategy);
        this.handleGraph = handlerGraph;
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
    public static <E> MpWizard<E> multiProducer(Class<E> eventType) {
        return multiProducer(newFactory(eventType, log));
    }

    /**
     * @param factory EventFactory<E>
     * @param <E>     Class type
     * @return Wizard<E>
     */
    public static <E> MpWizard<E> multiProducer(EventFactory<E> factory) {
        return new MpWizard<>(factory);
    }

    /**
     * @param eventType EventFactory<E>
     * @param <E>       Class type
     * @return Wizard<E>
     */
    public static <E> SpWizard<E> singleProducer(Class<E> eventType) {
        return singleProducer(newFactory(eventType, log));
    }

    /**
     * @param factory EventFactory<E>
     * @param <E>     Class type
     * @return Wizard<E>
     */
    public static <E> SpWizard<E> singleProducer(EventFactory<E> factory) {
        return new SpWizard<>(factory);
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
     * @param translator EventTranslator<E>
     */
    public void publish(EventTranslator<E> translator) {
        ringBuffer.publishEvent(translator);
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
     * @param translator EventTranslatorThreeArg<E, A0, A1, A2>
     * @param arg0       A0
     * @param arg1       A1
     * @param arg2       A2
     * @param <A0>       A0 Type
     * @param <A1>       A1 Type
     * @param <A2>       A2 Type
     */
    public <A0, A1, A2> void publish(EventTranslatorThreeArg<E, A0, A1, A2> translator, A0 arg0, A1 arg1, A2 arg2) {
        ringBuffer.publishEvent(translator, arg0, arg1, arg2);
    }

    protected static class Wizard<E> {
        protected final EventFactory<E> eventFactory;

        protected String name = null;
        protected int size = 128;
        protected StartMode startMode = StartMode.auto();
        protected WaitStrategy waitStrategy = Yielding.get();

        private Wizard(EventFactory<E> eventFactory) {
            this.eventFactory = eventFactory;
        }
    }

    /**
     * 多生产者实现
     *
     * @param <E>
     */
    public static class MpRingEventbus<E> extends RingEventbus<E> {

        private MpRingEventbus(String name, int size, StartMode startMode, EventFactory<E> eventFactory, WaitStrategy waitStrategy, HandlerGraph<E> handleGraph) {
            super(name, size, startMode, MULTI, eventFactory, waitStrategy, handleGraph);
        }

        public static class MpWizard<E> extends Wizard<E> {

            private MpWizard(EventFactory<E> eventFactory) {
                super(eventFactory);
            }

            public MpWizard<E> name(String name) {
                this.name = name;
                return this;
            }

            public MpWizard<E> size(int size) {
                this.size = size;
                return this;
            }

            public MpWizard<E> waitStrategy(WaitStrategy strategy) {
                this.waitStrategy = strategy;
                return this;
            }

            public MpWizard<E> startMode(StartMode startMode) {
                this.startMode = startMode;
                return this;
            }

            public MpRingEventbus<E> process(Processor<E> processor) {
                return process(new EventHandlerWrapper<>(processor, log));
            }

            public MpRingEventbus<E> process(EventHandler<E> handler) {
                return process(HandlerGraph.single(handler));
            }

            public MpRingEventbus<E> process(HandlerGraph<E> handlerGraph) {
                return new MpRingEventbus<>(name, size, startMode,
                        eventFactory, waitStrategy, handlerGraph);
            }
        }
    }


    public static class SpRingEventbus<E> extends RingEventbus<E> {

        private SpRingEventbus(String name, int size, StartMode startMode, EventFactory<E> eventFactory, WaitStrategy waitStrategy, HandlerGraph<E> handleGraph) {
            super(name, size, startMode, SINGLE, eventFactory, waitStrategy, handleGraph);
        }

        /**
         * 单生产者实现
         *
         * @param <E>
         */
        public static class SpWizard<E> extends Wizard<E> {

            private SpWizard(EventFactory<E> eventFactory) {
                super(eventFactory);
            }

            public SpWizard<E> name(String name) {
                this.name = name;
                return this;
            }

            public SpWizard<E> size(int size) {
                this.size = size;
                return this;
            }

            public SpWizard<E> waitStrategy(WaitStrategy strategy) {
                this.waitStrategy = strategy;
                return this;
            }

            public SpWizard<E> startMode(StartMode startMode) {
                this.startMode = startMode;
                return this;
            }

            public SpRingEventbus<E> process(Processor<E> processor) {
                return process(new EventHandlerWrapper<>(processor, log));
            }

            public SpRingEventbus<E> process(EventHandler<E> handler) {
                return process(HandlerGraph.single(handler));
            }

            public SpRingEventbus<E> process(HandlerGraph<E> handlerGraph) {
                return new SpRingEventbus<>(name, size, startMode,
                        eventFactory, waitStrategy, handlerGraph);
            }
        }
    }


}
