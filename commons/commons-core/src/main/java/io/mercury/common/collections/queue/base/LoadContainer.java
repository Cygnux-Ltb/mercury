package io.mercury.common.collections.queue.base;

/**
 * 
 * @author yellow013
 *
 * @param <E>
 */
public class LoadContainer<E> {

	private int type;
	private E e;

	/**
	 * 
	 * @param e
	 */
	public void loading(E e) {
		this.type = 0;
		this.e = e;
	}

	/**
	 * 
	 * @param type
	 * @param e
	 */
	public void loading(int type, E e) {
		this.type = type;
		this.e = e;
	}

	public int type() {
		return type;
	}

	public E unloading() {
		return e;
	}

}