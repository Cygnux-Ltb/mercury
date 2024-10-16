package io.mercury.common.sequence;

@FunctionalInterface
public interface OrderedObject<O extends OrderedObject<O>> extends Comparable<O> {

    long orderNum();

    @Override
    default int compareTo(O o) {
        return Long.compare(orderNum(), o.orderNum());
    }

}
