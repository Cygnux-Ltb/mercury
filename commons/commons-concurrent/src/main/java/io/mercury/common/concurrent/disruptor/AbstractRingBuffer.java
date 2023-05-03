package io.mercury.common.concurrent.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.thread.MaxPriorityThreadFactory;
import io.mercury.common.thread.RunnableComponent;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

import static io.mercury.common.util.BitOperator.minPow2;

/**
 * 抽象Disruptor实现
 *
 * @param <E> 事件处理类型
 * @param <I> 发布类型
 * @author yellow013
 */
public abstract class AbstractRingBuffer<E, I> extends RunnableComponent {

    private static final Logger log = Log4j2LoggerFactory.getLogger(AbstractRingBuffer.class);

    protected final Disruptor<E> disruptor;

    protected final EventPublisherWrapper<E, I> publisherWrapper;

    /**
     * @param name       String
     * @param size       int
     * @param factory    EventFactory<E>
     * @param type       ProducerType
     * @param strategy   WaitStrategy
     * @param translator EventTranslatorOneArg<E, I>
     */
    protected AbstractRingBuffer(@Nonnull String name, int size, @Nonnull EventFactory<E> factory,
                                 @Nonnull ProducerType type, @Nonnull WaitStrategy strategy,
                                 @Nonnull EventTranslatorOneArg<E, I> translator) {
        Asserter.nonNull(factory, "EventFactory");
        Asserter.nonNull(type, "ProducerType");
        Asserter.nonNull(strategy, "WaitStrategy");
        Asserter.nonNull(translator, "EventTranslator");
        this.name = name;
        this.disruptor = new Disruptor<>(
                // 事件工厂
                factory,
                // 调整并设置队列容量
                adjustSize(size),
                // 使用最高优先级的线程工厂
                new MaxPriorityThreadFactory(this.name + "-worker"),
                // 生产者策略
                type,
                // Waiting策略
                strategy);
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
     * @param in input event
     * @return boolean
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
     * @param size buffer size
     * @return int
     */
    private int adjustSize(int size) {
        return size < 16 ? 16 : size > 65536 ? 65536 : minPow2(size);
    }

}
