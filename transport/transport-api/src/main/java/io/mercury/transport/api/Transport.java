package io.mercury.transport.api;

import java.io.Closeable;
import java.io.IOException;

public interface Transport extends Closeable {

	/**
	 * 
	 * @return Name
	 */
	String getName();

	/**
	 * 
	 * @return Start Time
	 */
	long getStartTime();

	/**
	 * 
	 * @return End Time
	 */
	long getEndTime();

	/**
	 * 
	 * @return Running Duration
	 */
	long getRunningDuration();

	/**
	 * 
	 * @return Is connected
	 */
	boolean isConnected();

	/**
	 * 
	 * @return Is destroy
	 */
	boolean closeIgnoreException();

	@Override
	default void close() throws IOException {
		boolean isClosed;
		try {
			isClosed = closeIgnoreException();
		} catch (Exception e) {
			throw new IOException(getName() + " -> " + e.getMessage(), e);
		}
		if (!isClosed)
			throw new IOException(getName() + " -> Close failed");
	}

}
