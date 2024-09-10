package io.mercury.common.sequence;

@FunctionalInterface
public interface SerialObject<S extends SerialObject<S>> extends Comparable<S> {

    long serialId();

    @Override
    default int compareTo(S o) {
        return Long.compare(serialId(), o.serialId());
    }

}
