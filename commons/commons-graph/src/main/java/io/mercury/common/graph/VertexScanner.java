package io.mercury.common.graph;

import static io.mercury.common.collections.MutableSets.newUnifiedSet;

import org.eclipse.collections.api.set.ImmutableSet;
import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

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
		return newUnifiedSet(new BreadthFirstIterator<>(graph, start)).toImmutable();
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
		return newUnifiedSet(new DepthFirstIterator<>(graph, start)).toImmutable();
	}

}
