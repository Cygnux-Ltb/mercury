package io.mercury.common.graph;

import java.util.function.Supplier;

import org.jgrapht.Graph;
import org.jgrapht.graph.builder.GraphTypeBuilder;

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

	/**
	 * 
	 * @param <V>
	 * @param <E>
	 * @param vClass
	 * @param edgeSupplier
	 * @return
	 */
	public static <V, E extends Edge> Graph<V, E> directed(Class<V> vClass, Supplier<E> edgeSupplier) {
		return GraphTypeBuilder.directed()

				.vertexClass(vClass)

				.edgeSupplier(edgeSupplier)
				
				.buildGraph();
	}

	public static class Builder {

	}

}
