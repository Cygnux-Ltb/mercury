package io.mercury.common.graph;

import java.util.function.Supplier;

import org.jgrapht.Graph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;

public final class GraphBuilder {

	private static final Logger log = Log4j2LoggerFactory.getLogger(GraphBuilder.class);

	/**
	 * 
	 * @param <V>
	 * @param <E>
	 * @param vertexClass
	 * @param edgeClass
	 * @return
	 */
	public static <V, E extends Edge> Graph<V, E> directed(Class<V> vertexClass, Class<E> edgeClass) {
		return directed(vertexClass, edgeClass, defaultOptions);
	}

	/**
	 * 
	 * @param <V>
	 * @param <E>
	 * @param vertexClass
	 * @param edgeClass
	 * @param options
	 * @return
	 */
	public static <V, E extends Edge> Graph<V, E> directed(Class<V> vertexClass, Class<E> edgeClass,
			GraphOptions options) {
		GraphTypeBuilder<V, E> builder = GraphTypeBuilder.directed().vertexClass(vertexClass).edgeClass(edgeClass)
				.allowingMultipleEdges(options.isAllowingMultipleEdges())
				.allowingSelfLoops(options.isAllowingSelfLoops()).weighted(options.isWeighted());
		log.info("GraphBuilder -> {}", builder.buildType());
		return builder.buildGraph();
	}

	/**
	 * 
	 * @param <V>
	 * @param vertexClass
	 * @return
	 */
	public static <V> Graph<V, Edge> directed(Class<V> vertexClass) {
		return directed(vertexClass, defaultOptions);
	}

	/**
	 * 
	 * @param <V>
	 * @param vertexClass
	 * @param options
	 * @return
	 */
	public static <V> Graph<V, Edge> directed(Class<V> vertexClass, GraphOptions options) {
		return directed(vertexClass, Edge::new, options);
	}

	/**
	 * 
	 * @param <V>
	 * @param <E>
	 * @param vertexClass
	 * @param edgeSupplier
	 * @return
	 */
	public static <V, E extends Edge> Graph<V, E> directed(Class<V> vertexClass, Supplier<E> edgeSupplier) {
		return directed(vertexClass, edgeSupplier, defaultOptions);
	}

	/**
	 * 
	 * @param <V>
	 * @param <E>
	 * @param vertexClass
	 * @param edgeSupplier
	 * @param options
	 * @return
	 */
	public static <V, E extends Edge> Graph<V, E> directed(Class<V> vertexClass, Supplier<E> edgeSupplier,
			GraphOptions options) {
		GraphTypeBuilder<V, E> builder = GraphTypeBuilder.directed().vertexClass(vertexClass).edgeSupplier(edgeSupplier)
				.allowingMultipleEdges(options.isAllowingMultipleEdges())
				.allowingSelfLoops(options.isAllowingSelfLoops()).weighted(options.isWeighted());
		log.info("GraphBuilder -> {}", builder.buildType());
		return builder.buildGraph();
	}

	private final static GraphOptions defaultOptions = GraphOptions.defaultOptions();

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static class GraphOptions {

		private boolean allowingMultipleEdges = false;
		private boolean allowingSelfLoops = false;
		private boolean weighted = false;

		private GraphOptions() {
		}

		public static final GraphOptions defaultOptions() {
			return new GraphOptions();
		}

		public boolean isAllowingMultipleEdges() {
			return allowingMultipleEdges;
		}

		public boolean isAllowingSelfLoops() {
			return allowingSelfLoops;
		}

		public boolean isWeighted() {
			return weighted;
		}

		public GraphOptions allowingMultipleEdges() {
			this.allowingMultipleEdges = true;
			return this;
		}

		public GraphOptions allowingSelfLoops() {
			this.allowingSelfLoops = true;
			return this;
		}

		public GraphOptions enableWeighted() {
			this.weighted = true;
			return this;
		}

	}

}
