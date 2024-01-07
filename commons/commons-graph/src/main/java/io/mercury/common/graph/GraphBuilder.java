package io.mercury.common.graph;

import io.mercury.common.log4j2.Log4j2LoggerFactory;
import org.jgrapht.Graph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.slf4j.Logger;

import java.util.function.Supplier;

public final class GraphBuilder {

    private static final Logger log = Log4j2LoggerFactory.getLogger(GraphBuilder.class);

    /**
     * @param <V>    V
     * @param <E>    E
     * @param vClass Class<V>
     * @param eClass Class<E>
     * @return Graph<V, E>
     */
    public static <V, E extends Edge> Graph<V, E> directed(Class<V> vClass,
                                                           Class<E> eClass) {
        return directed(vClass, eClass, defaultOptions);
    }

    /**
     * @param <V>     V
     * @param <E>     E
     * @param vClass  Class<V>
     * @param eClass  Class<E>
     * @param options GraphOptions
     * @return Graph<V, E>
     */
    public static <V, E extends Edge> Graph<V, E> directed(Class<V> vClass,
                                                           Class<E> eClass,
                                                           GraphOptions options) {
        GraphTypeBuilder<V, E> builder = GraphTypeBuilder.directed().vertexClass(vClass).edgeClass(eClass)
                .allowingMultipleEdges(options.isAllowingMultipleEdges())
                .allowingSelfLoops(options.isAllowingSelfLoops()).weighted(options.isWeighted());
        log.info("GraphBuilder -> {}", builder.buildType());
        return builder.buildGraph();
    }

    /**
     * @param <V>         V
     * @param vertexClass Class<V>
     * @return Graph<V, Edge>
     */
    public static <V> Graph<V, Edge> directed(Class<V> vertexClass) {
        return directed(vertexClass, defaultOptions);
    }

    /**
     * @param <V>     V
     * @param vClass  Class<V>
     * @param options GraphOptions
     * @return Graph<V, Edge>
     */
    public static <V> Graph<V, Edge> directed(Class<V> vClass,
                                              GraphOptions options) {
        return directed(vClass, Edge::new, options);
    }

    /**
     * @param <V>          V
     * @param <E>          E
     * @param vClass       Class<V>
     * @param edgeSupplier Supplier<E>
     * @return Graph<V, E>
     */
    public static <V, E extends Edge> Graph<V, E> directed(Class<V> vClass,
                                                           Supplier<E> edgeSupplier) {
        return directed(vClass, edgeSupplier, defaultOptions);
    }

    /**
     * @param <V>          V
     * @param <E>          E
     * @param vClass       Class<V>
     * @param edgeSupplier Supplier<E>
     * @param options      GraphOptions
     * @return Graph<V, E>
     */
    public static <V, E extends Edge> Graph<V, E> directed(Class<V> vClass,
                                                           Supplier<E> edgeSupplier,
                                                           GraphOptions options) {
        GraphTypeBuilder<V, E> builder = GraphTypeBuilder.directed().vertexClass(vClass).edgeSupplier(edgeSupplier)
                .allowingMultipleEdges(options.isAllowingMultipleEdges())
                .allowingSelfLoops(options.isAllowingSelfLoops()).weighted(options.isWeighted());
        log.info("GraphBuilder -> {}", builder.buildType());
        return builder.buildGraph();
    }

    private final static GraphOptions defaultOptions = GraphOptions.defaultOptions();

    /**
     * @author yellow013
     */
    public static class GraphOptions {

        private boolean allowingMultipleEdges = false;
        private boolean allowingSelfLoops = false;
        private boolean weighted = false;

        private GraphOptions() {
        }

        public static GraphOptions defaultOptions() {
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
