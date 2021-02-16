package io.mercury.common.collections.queue;

import java.util.Collection;
import java.util.LinkedList;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class PreloadingQueue<T> {

	private LinkedList<T> list = new LinkedList<>();

	public T next() {
		return list.removeFirst();
	}

	public void addLast(T content) {
		list.addLast(content);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean notEmpty() {
		return !isEmpty();
	}

	public Collection<T> getAllElement() {
		return list;
	}

}