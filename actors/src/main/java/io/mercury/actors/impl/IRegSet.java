package io.mercury.actor.impl;

import java.util.Collection;

public interface IRegSet<T> {

    interface IRegistration {
        void remove();
    }

    IRegistration add(T element);

    Collection<T> copy();
}
