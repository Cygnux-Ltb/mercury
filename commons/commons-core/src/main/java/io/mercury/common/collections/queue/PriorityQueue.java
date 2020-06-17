package io.mercury.common.collections.queue;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.set.sorted.MutableSortedSet;

import io.mercury.common.collections.MutableSets;

@NotThreadSafe
public class PriorityQueue<E> {

	private MutableSortedSet<E> prioritySet = MutableSets.newTreeSortedSet();
	private MutableSortedSet<E> secondarySet = MutableSets.newTreeSortedSet();

	public boolean addAllToPrioritySet(Collection<E> collection) {
		return prioritySet.addAll(collection);
	}

	public boolean addToPrioritySet(E e) {
		return prioritySet.add(e);
	}

	public boolean addAllToSecondarySet(Collection<E> collection) {
		return secondarySet.addAll(collection);
	}

	public boolean addToSecondarySet(E e) {
		return secondarySet.add(e);
	}

	public boolean isEmpty() {
		return prioritySet.isEmpty() && secondarySet.isEmpty();
	}

	public Optional<E> next() {
		return prioritySet.notEmpty() ? Optional.of(prioritySet.first())
				: secondarySet.notEmpty() ? Optional.of(secondarySet.first()) : Optional.empty();
	}

}
