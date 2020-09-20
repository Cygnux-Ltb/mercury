package io.mercury.common.graph;

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

	public static class Builder {

	}

}
