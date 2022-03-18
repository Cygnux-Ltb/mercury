package io.mercury.common.collections.group;

import java.util.Set;

import javax.annotation.Nonnull;

/**
 * @param <K>
 * @param <V>
 * @author yellow013
 */
public interface Group<K, V> {

    @Nonnull
    V getMember(@Nonnull K k) throws MemberNotExistException;

    @Nonnull
    Set<K> getKeys();

    class MemberNotExistException extends RuntimeException {

        /**
         *
         */
        private static final long serialVersionUID = -6229705929334610236L;

        public MemberNotExistException(String msg) {
            super(msg);
        }

    }

}
