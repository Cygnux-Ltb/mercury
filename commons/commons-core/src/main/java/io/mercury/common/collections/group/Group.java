package io.mercury.common.collections.group;

import java.util.Set;

import javax.annotation.Nonnull;

/**
 * 
 * @author yellow013
 *
 * @param <K>
 * @param <V>
 */
public interface Group<K, V> {

	@Nonnull
	V getMember(@Nonnull K k) throws MemberNotExistException;

	@Nonnull
	Set<K> getKeys();

	public static class MemberNotExistException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6229705929334610236L;

		public MemberNotExistException(String msg) {
			super(msg);
		}

	}

}
