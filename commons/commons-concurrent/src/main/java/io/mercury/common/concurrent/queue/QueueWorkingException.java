package io.mercury.common.concurrent.queue;

public class QueueWorkingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -117741434920742324L;

	QueueWorkingException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
