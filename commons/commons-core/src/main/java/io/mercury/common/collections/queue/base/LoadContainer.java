package io.mercury.common.collections.queue.base;

public class LoadContainer<E> {

	private E e;

	public void loading(E e) {
		this.e = e;
	}

	public E unloading() {
		return e;
	}

}