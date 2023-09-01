package io.mercury.common.concurrent.ring.base;

import com.lmax.disruptor.EventHandler;
import io.mercury.common.functional.Processor;
import org.slf4j.Logger;

/**
 * 事件处理器的包装
 *
 * @author yellow013
 */
public final class EventHandlerWrapper<E> implements EventHandler<E> {

    private final Processor<E> processor;

    private final Logger log;

    private final boolean mayCrash;

    public EventHandlerWrapper(Processor<E> processor, Logger log) {
        this(processor, log, true);
    }

    public EventHandlerWrapper(Processor<E> processor, Logger log, boolean mayCrash) {
        this.processor = processor;
        this.log = log;
        this.mayCrash = mayCrash;
    }

    @Override
    public void onEvent(E event, long sequence, boolean endOfBatch) throws Exception {
        try {
            processor.process(event);
        } catch (Exception e) {
            log.error("process event -> {}, sequence==[{}], endOfBatch==[{}], Throw exception -> [{}]",
                    event, sequence, endOfBatch, e.getMessage(), e);
            if (mayCrash)
                throw e;
        }
    }
}
