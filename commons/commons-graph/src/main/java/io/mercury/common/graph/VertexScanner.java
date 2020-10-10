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
	 * @param <V>         startVertex
	 * @param <E>
	 * @param graph
	 * @param startVertex
	 * @return
	 */
	public <V, E extends Edge> ImmutableSet<V> breadthFirst(Graph<V, E> graph, V startVertex) {
		return MutableSets.newUnifiedSet(new BreadthFirstIterator<>(graph, startVertex)).toImmutable();
	}

	/**
	 * 
	 * @param <V>         startVertex
	 * @param <E>
	 * @param graph
	 * @param startVertex
	 * @return
	 */
	public <V, E extends Edge> ImmutableSet<V> depthFirst(Graph<V, E> graph, V startVertex) {
		return MutableSets.newUnifiedSet(new DepthFirstIterator<>(graph, startVertex)).toImmutable();
	}

}
