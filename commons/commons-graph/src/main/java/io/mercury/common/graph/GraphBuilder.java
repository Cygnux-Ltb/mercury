package io.mercury.common.graph;

import static io.mercury.common.collections.ImmutableSets.newImmutableSet;
import static io.mercury.common.collections.MutableSets.newUnifiedSet;

import java.util.function.Supplier;

import org.eclipse.collections.api.set.ImmutableSet;
import org.jgrapht.Graph;
import org.jgrapht.GraphType;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.traverse.BreadthFirstIterator;

public final class GraphBuilder {

	/**
	 * 
	 * @param <V>
	 * @param vClass
	 * @return
	 */
	public static <V> Graph<V, Edge> directed(Class<V> vClass) {
		return GraphTypeBuilder.directed()

				.vertexClass(vClass)

				.edgeSupplier(Edge::new)

				.buildGraph();
	}

	public static class Builder {

	}

}
