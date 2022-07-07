package io.mercury.common.concurrent.disruptor;

import org.slf4j.Logger;

import com.lmax.disruptor.EventHandler;

import io.mercury.common.functional.Processor;

/**
 * 事件处理器的包装
 *
 * @author yellow013
 */
public class EventHandlerWrapper<E> implements EventHandler<E> {

    private final Processor<E> processor;

    private final Logger log;

    public EventHandlerWrapper(Processor<E> processor, Logger log) {
        this.processor = processor;
        this.log = log;
    }

    @Override
    public void onEvent(E event, long sequence, boolean endOfBatch) throws Exception {
        try {
            processor.process(event);
        } catch (Exception e) {
            log.error("process event -> {}, sequence==[{}], endOfBatch==[{}], Throw exception -> [{}]", event, sequence,
                    endOfBatch, e.getMessage(), e);
            throw e;
        }
    }
}
