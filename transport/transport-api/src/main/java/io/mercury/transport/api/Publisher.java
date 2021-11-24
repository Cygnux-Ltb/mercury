package io.mercury.transport.api;

import javax.annotation.Nonnull;

import io.mercury.transport.exception.PublishFailedException;

/**
 * 
 * @author yellow013
 *
 * @param <T> target type
 * @param <M> message type
 */
public interface Publisher<T, M> extends Transport {

	/**
	 * Publish to default location
	 * 
	 * @param msg
	 * @throws PublishFailedException
	 */
	void publish(@Nonnull M msg) throws PublishFailedException;

	/**
	 * Publish to target location
	 * 
	 * @param target
	 * @param msg
	 * @throws PublishFailedException
	 */
	void publish(@Nonnull T target, @Nonnull M msg) throws PublishFailedException;

}
