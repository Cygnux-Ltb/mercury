package io.mercury.common.collections.queue;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.set.sorted.MutableSortedSet;

import io.mercury.common.collections.MutableSets;

@NotThreadSafe
public class PriorityQueue<E> {

	/**
	 * 优先集合
	 */
	private MutableSortedSet<E> prioritySet = MutableSets.newTreeSortedSet();
	/**
	 * 次级集合
	 */
	private MutableSortedSet<E> secondarySet = MutableSets.newTreeSortedSet();

	/**
	 * 
	 * @param collection
	 * @return
	 */
	public boolean addPriority(Collection<E> collection) {
		return prioritySet.addAll(collection);
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public boolean addPriority(E e) {
		return prioritySet.add(e);
	}

	/**
	 * 
	 * @param collection
	 * @return
	 */
	public boolean addSecondary(Collection<E> collection) {
		return secondarySet.addAll(collection);
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public boolean addSecondary(E e) {
		return secondarySet.add(e);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return prioritySet.isEmpty() && secondarySet.isEmpty();
	}

	/**
	 * 
	 * @return
	 */
	public Optional<E> next() {
		return prioritySet.notEmpty() ? Optional.of(prioritySet.first())
				: secondarySet.notEmpty() ? Optional.of(secondarySet.first()) : Optional.empty();
	}

}
