package io.mercury.common.concurrent.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.mercury.common.collections.queue.EventContainer;
import io.mercury.common.concurrent.queue.ScQueue;
import io.mercury.common.concurrent.disruptor.base.CommonStrategy;
import io.mercury.common.functional.Processor;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.thread.Sleep;
import io.mercury.common.thread.ThreadSupport;
import org.slf4j.Logger;

import static io.mercury.common.thread.ThreadFactoryImpl.ofPlatform;
import static io.mercury.common.thread.ThreadPriority.MAX;
import static java.lang.System.currentTimeMillis;

/**
 * @param <E>
 * @author yellow013
 */
public class RingQueue<E> extends ScQueue<E> {

    private static final Logger log = Log4j2LoggerFactory.getLogger(RingQueue.class);

    private final Disruptor<EventContainer<E>> disruptor;

    private final InnerEventPublisher publisher;

    private final QueueType queueType;

    private RingQueue(String name, int size, StartMode mode,
                      ProducerType type, WaitStrategy strategy, Processor<E> processor) {
        super(processor);
        if (name != null)
            super.name = name;
        queueType = switch (type) {
            case SINGLE -> QueueType.SPSC;
            case MULTI -> QueueType.MPSC;
        };
        this.disruptor = new Disruptor<>(
                // 实现EventFactory<LoadContainer<E>>的Lambda
                EventContainer::new,
                // 队列容量
                size,
                // 实现ThreadFactory的Lambda
                // DaemonThreadFactory.INSTANCE,
                // (Runnable runnable) -> newMaxPriorityThread(this.name + "-worker", runnable),
                ofPlatform(this.name + "-worker").priority(MAX).build(),
                // 生产者策略, Waiting策略
                type, strategy);
        this.disruptor.handleEventsWith(this::process);
        // TODO 异常处理
        // this.disruptor.setDefaultExceptionHandler(null);
        this.publisher = new InnerEventPublisher(disruptor.getRingBuffer());
        startWith(mode);
    }

    private void process(EventContainer<E> container, long sequence, boolean endOfBatch) {
        try {
            processor.process(container.unloading());
        } catch (Exception e) {
            log.error("processor::process throw exception -> [{}]", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * LoadContainerEventProducer
     *
     * @author yellow013
     */
    private class InnerEventPublisher {

        private final RingBuffer<EventContainer<E>> ringBuffer;

        private final EventTranslatorOneArg<EventContainer<E>, E> translator = (container, sequence, event) ->
                container.loading(event);

        private InnerEventPublisher(RingBuffer<EventContainer<E>> ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        private void onEvent(E e) {
            ringBuffer.publishEvent(translator, e);
        }
    }

    @Override
    public boolean enqueue(E e) {
        if (!isRunning())
            return false;
        try {
            publisher.onEvent(e);
            return true;
        } catch (Exception ex) {
            log.error("producer.onEvent(e) throw exception -> [{}]", ex.getMessage(), ex);
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

    public static Builder singleProducer() {
        return new Builder(ProducerType.SINGLE);
    }

    public static Builder multiProducer() {
        return new Builder(ProducerType.MULTI);
    }

    public static class Builder {

        private final ProducerType type;
        private String name = "ring-" + currentTimeMillis();
        private int size = 32;
        private StartMode mode = StartMode.auto();
        private WaitStrategy strategy = CommonStrategy.Sleeping.get();

        private Builder(ProducerType type) {
            this.type = type;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder startMode(StartMode mode) {
            this.mode = mode;
            return this;
        }

        public Builder waitStrategy(CommonStrategy strategy) {
            this.strategy = strategy.get();
            return this;
        }

        public Builder waitStrategy(WaitStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public <E> RingQueue<E> process(Processor<E> processor) {
            return new RingQueue<>(name, size, mode, type, strategy, processor);
        }

    }

    public static void main(String[] args) {

        RingQueue<Integer> queue = RingQueue.singleProducer().name("Test-Queue")
                .process(System.out::println);

        ThreadSupport.startNewThread(() -> {
            int i = 0;
            for (; ; )
                queue.enqueue(++i);
        });

        Sleep.millis(10000);
        queue.stop();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public QueueType getQueueType() {
        return queueType;
    }

}
