package io.mercury.common.graph;

import org.eclipse.collections.api.set.ImmutableSet;
import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import io.mercury.common.collections.MutableSets;

public final class VertexScanner {

	private VertexScanner() {
	}

	/**
	 * 
	 * @param <V>   start
	 * @param <E>
	 * @param graph
	 * @param start
	 * @return
	 */
	public <V, E extends Edge> ImmutableSet<V> breadthFirst(Graph<V, E> graph, V start) {
		return MutableSets.newUnifiedSet(new BreadthFirstIterator<>(graph, start)).toImmutable();
	}

	/**
	 * 
	 * @param <V>   start
	 * @param <E>
	 * @param graph
	 * @param start
	 * @return
	 */
	public <V, E extends Edge> ImmutableSet<V> depthFirst(Graph<V, E> graph, V start) {
		return MutableSets.newUnifiedSet(new DepthFirstIterator<>(graph, start)).toImmutable();
	}

}
