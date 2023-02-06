package io.mercury.common.collections.list;

import io.mercury.common.annotation.AbstractFunction;
import org.eclipse.collections.impl.list.mutable.FastList;

import java.util.ArrayList;
import java.util.List;

public abstract class LimitedList<L extends List<E>, E> extends LimitedContainer<E> {

    private final L list;

    protected LimitedList(int capacity) {
        super(capacity);
        this.list = newList(capacity);
    }

    /**
     * @param capacity int
     * @return LimitedFastList<E>
     */
    public static <E> LimitedFastList<E> newLimitedFastList(int capacity) {
        return new LimitedFastList<>(capacity);
    }

    /**
     * @param capacity int
     * @return LimitedArrayList<E>
     */
    public static <E> LimitedArrayList<E> newLimitedArrayList(int capacity) {
        return new LimitedArrayList<>(capacity);
    }

    @AbstractFunction
    protected abstract L newList(int capacity);

    @Override
    protected void setTail(int tail, E e) {
        list.set(tail, e);
    }

    @Override
    public E getTail() {
        return list.get(tailIndex());
    }

    @Override
    public E getHead() {
        return list.get(headIndex());
    }

    /**
     * @param <E>
     * @author yellow013
     */
    public static class LimitedFastList<E> extends LimitedList<FastList<E>, E> {

        private LimitedFastList(int capacity) {
            super(capacity);
        }

        @Override
        protected FastList<E> newList(int capacity) {
            return new FastList<>(capacity);
        }

    }

    /**
     * @param <E>
     * @author yellow013
     */
    public static class LimitedArrayList<E> extends LimitedList<ArrayList<E>, E> {

        private LimitedArrayList(int capacity) {
            super(capacity);
        }

        @Override
        protected ArrayList<E> newList(int capacity) {
            return new ArrayList<>(capacity);
        }

    }

}
