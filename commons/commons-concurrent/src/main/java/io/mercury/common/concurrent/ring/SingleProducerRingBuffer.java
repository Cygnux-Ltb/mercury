package io.mercury.common.concurrent.ring;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.mercury.common.concurrent.ring.base.EventPublisher;
import io.mercury.common.concurrent.ring.base.WaitStrategyOption;
import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.thread.RunnableComponent;
import io.mercury.common.util.BitOperator;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Objects;

import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSS;
import static io.mercury.common.thread.ThreadFactoryImpl.ofPlatform;
import static io.mercury.common.thread.ThreadPriority.MAX;
import static io.mercury.common.util.StringSupport.requireNonEmptyElse;

/**
 * 单生产者Disruptor实现
 *
 * @param <E> 事件处理类型
 * @param <I> 发布类型
 * @author yellow013
 * @deprecated io.mercury.common.concurrent.disruptor.AbstractRingBuffer
 */
@Deprecated
public abstract class SingleProducerRingBuffer<E, I> extends RunnableComponent {

    private static final Logger log = Log4j2LoggerFactory.getLogger(SingleProducerRingBuffer.class);

    protected final Disruptor<E> disruptor;

    protected final EventPublisher<E, I> publisherWrapper;

    protected SingleProducerRingBuffer(String name, int size,
                                       @Nullable WaitStrategy strategy,
                                       @Nonnull EventFactory<E> factory,
                                       @Nonnull EventTranslatorOneArg<E, I> translator) {
        super(requireNonEmptyElse(name,
                STR."SP-Ring-\{YYYYMMDD_L_HHMMSSSSS.fmt(LocalDateTime.now())}"));
        Asserter.nonNull(factory, "factory");
        Asserter.nonNull(translator, "translator");
        this.disruptor = new Disruptor<>(
                // 事件工厂
                factory,
                // 调整并设置队列容量
                adjustSize(size),
                // 使用最高优先级的线程工厂
                ofPlatform(STR."\{this.name}-worker").priority(MAX).build(),
                // 生产者策略, 使用单生产者
                ProducerType.SINGLE,
                // Waiting策略
                Objects.requireNonNullElse(strategy, WaitStrategyOption.Sleeping.get())
        );
        this.publisherWrapper = new EventPublisher<>(disruptor.getRingBuffer(), translator);
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
     * @param in I
     * @return boolean
     */
    public boolean publishEvent(I in) {
        try {
            if (isClosed.get())
                return false;
            publisherWrapper.publish(in);
            return true;
        } catch (Exception e) {
            log.error("{} -> EventPublisher::handle(in) func throws exception -> [{}]", name, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 调整队列容量, 最小16, 最大65536, 其他输入参数自动调整为最接近的2次幂
     *
     * @param size int
     * @return int
     */
    private int adjustSize(int size) {
        return size < 16 ? 16 : size > 65536 ? 65536 : BitOperator.minPow2(size);
    }

}
