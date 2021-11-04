package io.mercury.common.concurrent.queue;

import static io.mercury.common.datetime.pattern.spec.DateTimePattern.YYYY_MM_DD_HH_MM_SS_SSS;

import java.time.LocalDateTime;

import io.mercury.common.functional.Processor;
import io.mercury.common.thread.RunnableComponent;
import io.mercury.common.util.Assertor;

/**
 * @author yellow013
 *
 * @param <T> Single Consumer Queue base implements
 */
public abstract class AbstractSingleConsumerQueue<E> extends RunnableComponent implements Queue<E> {

	/**
	 * Processor Function
	 */
	protected final Processor<E> processor;

	protected AbstractSingleConsumerQueue(Processor<E> processor) {
		Assertor.nonNull(processor, "processor");
		this.processor = processor;
		this.name = "Queue-" + "[" + YYYY_MM_DD_HH_MM_SS_SSS.format(LocalDateTime.now()) + "]";
	}

	@Override
	public String getQueueName() {
		return name;
	}

	@Override
	protected String getComponentType() {
		return "ScQueue";
	}

}
