package io.mercury.common.concurrent.queue;

import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSS;

import java.time.LocalDateTime;

import io.mercury.common.collections.queue.Queue;
import io.mercury.common.functional.Processor;
import io.mercury.common.lang.Asserter;
import io.mercury.common.thread.RunnableComponent;

/**
 * @author yellow013
 *
 * @param <E> Single Consumer Queue base implements
 */
public abstract class AbstractSingleConsumerQueue<E> extends RunnableComponent implements Queue<E> {

	/**
	 * Processor Function
	 */
	protected final Processor<E> processor;

	protected AbstractSingleConsumerQueue(Processor<E> processor) {
		Asserter.nonNull(processor, "processor");
		this.processor = processor;
		this.name = "queue-" + "[" + YYYYMMDD_L_HHMMSSSSS.format(LocalDateTime.now()) + "]";
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
