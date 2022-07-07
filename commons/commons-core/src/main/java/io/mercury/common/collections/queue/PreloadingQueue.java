package io.mercury.common.collections.queue;

import java.util.LinkedList;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class PreloadingQueue<T> {

    private final LinkedList<T> list = new LinkedList<>();

    public T next() {
        return list.removeFirst();
    }

    public void add(T content) {
        list.addLast(content);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean notEmpty() {
        return !isEmpty();
    }

    public LinkedList<T> getLinkedList() {
        return list;
    }

}