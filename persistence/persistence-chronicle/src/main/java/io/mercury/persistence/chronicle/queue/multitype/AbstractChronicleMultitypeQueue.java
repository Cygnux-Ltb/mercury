package io.mercury.persistence.chronicle.queue.multitype;

import io.mercury.common.codec.Envelope;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.persistence.chronicle.queue.AbstractChronicleQueue;
import io.mercury.persistence.chronicle.queue.AbstractChronicleReader;
import org.slf4j.Logger;

import javax.annotation.concurrent.Immutable;

/**
 * @param <E>
 * @param <IN>
 * @param <OUT>
 * @param <AT>
 * @param <RT>
 * @author yellow013
 */
@Immutable
public abstract class AbstractChronicleMultitypeQueue2<
        // 信封类型
        E extends Envelope,
        // 写入类型
        IN,
        // 读取类型
        OUT,
        // 追加器类型
        AT extends AbstractChronicleMultitypeAppender<E, IN>,
        // 读取器类型
        RT extends AbstractChronicleReader<OUT>>
        extends AbstractChronicleQueue<IN, OUT, AT, RT> {

    protected Logger logger = Log4j2LoggerFactory.getLogger(getClass());

    protected AbstractChronicleMultitypeQueue2(AbstractQueueBuilder<?> builder) {
        super(builder);
    }

}
