package io.mercury.common.graph;

import java.util.function.Supplier;

import org.jgrapht.Graph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;

public final class GraphBuilder {

	private static final Logger log = CommonLoggerFactory.getLogger(GraphBuilder.class);

	/**
	 * 有向图
	 * 
	 * @param <V>
	 * @param vClass
	 * @return
	 */
	public static <V> Graph<V, Edge> directed(Class<V> vClass) {
		GraphTypeBuilder<V, Edge> builder = GraphTypeBuilder
				.directed()
				.vertexClass(vClass)
				.edgeSupplier(Edge::new);
		log.info("GraphBuilder -> {}", builder.buildType());
		return builder.buildGraph();
	}

	/**
	 * 有向图
	 * 
	 * @param <V>
	 * @param <E>
	 * @param vClass
	 * @param edgeSupplier
	 * @return
	 */
	public static <V, E extends Edge> Graph<V, E> directed(Class<V> vClass, Supplier<E> edgeSupplier) {
		GraphTypeBuilder<V, E> builder = GraphTypeBuilder
				.directed()
				.vertexClass(vClass)
				.edgeSupplier(edgeSupplier);
		log.info("GraphBuilder -> {}", builder.buildType());
		return builder.buildGraph();
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static class Builder {

	}

}
