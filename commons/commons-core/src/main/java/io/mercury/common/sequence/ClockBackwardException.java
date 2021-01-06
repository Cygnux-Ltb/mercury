package io.mercury.common.sequence;

/**
 * 时钟回退抛出此异常
 * 
 * @author yellow013
 *
 */
public final class ClockBackwardException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5012855755917563428L;

	private final long backwardMillis;

	ClockBackwardException(long backwardMillis) {
		super(String.format("The clock moved backwards, Refusing to generate seq for %d millis", backwardMillis));
		this.backwardMillis = backwardMillis;
	}

	public long getBackwardMillis() {
		return backwardMillis;
	}

}
