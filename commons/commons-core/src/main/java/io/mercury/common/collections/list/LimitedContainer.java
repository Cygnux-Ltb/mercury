package io.mercury.common.collections.list;

public abstract class LimitedContainer<E> {

	private int head = 0;
	private int tail = 0;
	private int count = 0;

	private int capacity;

	protected LimitedContainer(int capacity) {
		this.capacity = capacity;
	}

	protected int getHeadIndex() {
		return head;
	}

	protected int getTailIndex() {
		return tail;
	}

	public int getCount() {
		return count;
	}

	public void add(E e) {
		updateTail();
		setTail(tail, e);
		updateHead();
		updateCount();
	}

	private void updateTail() {
		if (++tail == capacity)
			tail = 0;
	}

	private void updateHead() {
		if (count == capacity) {
			if (++head == capacity)
				head = 0;
		}
	}

	private void updateCount() {
		if (count < capacity)
			count++;
	}

	abstract protected void setTail(int tail, E e);

	abstract public E getTail();

	abstract public E getHead();

}
