package io.mercury.common.concurrent.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.mercury.common.collections.queue.LoadContainer;
import io.mercury.common.concurrent.queue.SingleConsumerQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.thread.MaxPriorityThreadFactory;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.ThreadSupport;
import org.slf4j.Logger;

import static io.mercury.common.concurrent.disruptor.CommonWaitStrategy.Sleeping;

/**
 * @param <E>
 * @author yellow013
 */
public class RingQueue<E> extends SingleConsumerQueue<E> {

    private static final Logger log = Log4j2LoggerFactory.getLogger(RingQueue.class);

    private final Disruptor<LoadContainer<E>> disruptor;

    private final LoadContainerEventProducer producer;

    private RingQueue(String name, int size, StartMode mode, ProducerType type, WaitStrategy strategy,
                      Processor<E> processor) {
        super(processor);
        if (name != null)
            super.name = name;
        this.disruptor = new Disruptor<>(
                // 实现EventFactory<LoadContainer<>>的Lambda
                LoadContainer::new,
                // 队列容量
                size,
                // 实现ThreadFactory的Lambda
                // DaemonThreadFactory.INSTANCE,
                // (Runnable runnable) -> newMaxPriorityThread(this.name + "-worker", runnable),
                new MaxPriorityThreadFactory(this.name + "-worker"),
                // 生产者策略, 使用单生产者
                type,
                // Waiting策略
                strategy);
        this.disruptor.handleEventsWith(this::process);
        // TODO 异常处理
        // this.disruptor.setDefaultExceptionHandler(null);
        this.producer = new LoadContainerEventProducer(disruptor.getRingBuffer());
        startWith(mode);
    }

    private void process(LoadContainer<E> container, long sequence, boolean endOfBatch) {
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
    private class LoadContainerEventProducer {

        private final RingBuffer<LoadContainer<E>> ringBuffer;

        private final EventTranslatorOneArg<LoadContainer<E>, E> translator = (container, sequence, e) -> container
                .loading(e);

        private LoadContainerEventProducer(RingBuffer<LoadContainer<E>> ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        private void onEvent(E t) {
            ringBuffer.publishEvent(translator, t);
        }
    }

    @Override
    public boolean enqueue(E in) {
        if (!isRunning())
            return false;
        try {
            producer.onEvent(in);
            return true;
        } catch (Exception e) {
            log.error("producer.onEvent(in) throw exception -> [{}]", e.getMessage(), e);
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

    public static Builder withMultiProducer() {
        return new Builder(ProducerType.MULTI);
    }

    public static Builder withSingleProducer() {
        return new Builder(ProducerType.SINGLE);
    }

    public static class Builder {

        private final ProducerType type;
        private String name = "ring-" + System.currentTimeMillis();
        private int size = 32;
        private StartMode mode = StartMode.auto();
        private WaitStrategy strategy = Sleeping.get();

        private Builder(ProducerType type) {
            this.type = type;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder setStartMode(StartMode mode) {
            this.mode = mode;
            return this;
        }

        public Builder setWaitStrategy(CommonWaitStrategy strategy) {
            this.strategy = strategy.get();
            return this;
        }

        public Builder setWaitStrategy(WaitStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public <E> RingQueue<E> process(Processor<E> processor) {
            return new RingQueue<>(name, size, mode, type, strategy, processor);
        }

    }

    public static void main(String[] args) {

        RingQueue<Integer> queue = RingQueue.withSingleProducer().setName("Test-Queue")
                .process(System.out::println);

        ThreadSupport.startNewThread(() -> {
            int i = 0;
            for (; ; )
                queue.enqueue(++i);
        });

        SleepSupport.sleep(10000);
        queue.stop();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.OneToOne;
    }

}
