package io.mercury.common.concurrent.disruptor;

import org.slf4j.Logger;

import com.lmax.disruptor.EventHandler;

import io.mercury.common.functional.Processor;

/**
 * 包装处理器的事件处理代理
 * 
 * @author yellow013
 */
public class EventHandlerProxy<E> implements EventHandler<E> {

	private final Processor<E> processor;

	private final Logger log;

	public EventHandlerProxy(Processor<E> processor, Logger log) {
		this.processor = processor;
		this.log = log;
	}

	@Override
	public void onEvent(E event, long sequence, boolean endOfBatch) throws Exception {
		try {
			processor.process(event);
		} catch (Exception e) {
			log.error("process event -> {} throw exception -> [{}]", event, e.getMessage(), e);
			throw e;
		}
	}
}
