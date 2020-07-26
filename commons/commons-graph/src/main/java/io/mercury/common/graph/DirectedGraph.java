package io.mercury.common.graph;

import static io.mercury.common.collections.ImmutableSets.newImmutableSet;
import static io.mercury.common.collections.MutableSets.newUnifiedSet;

import java.util.function.Supplier;

import org.eclipse.collections.api.set.ImmutableSet;
import org.jgrapht.Graph;
import org.jgrapht.GraphType;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.traverse.BreadthFirstIterator;

public final class DirectedGraph<V, E extends Edge> {

	private final Graph<V, E> savedGraph;

	private DirectedGraph(Class<V> vertexClass, Supplier<E> edgeSupplier) {
		this.savedGraph = GraphTypeBuilder.directed().vertexClass(vertexClass).edgeSupplier(edgeSupplier).buildGraph();
	}

	public static <V> DirectedGraph<V, Edge> newWith(Class<V> vertexClass) {
		return new DirectedGraph<>(vertexClass, Edge::new);
	}

	public static <V, E extends Edge> DirectedGraph<V, E> newWith(Class<V> vertexClass, Supplier<E> edgeSupplier) {
		return new DirectedGraph<>(vertexClass, edgeSupplier);
	}

	public GraphType getType() {
		return savedGraph.getType();
	}

	public DirectedGraph<V, E> addVertex(V vertex) {
		if (vertex != null) {
			savedGraph.addVertex(vertex);
		}
		return this;
	}

	public DirectedGraph<V, E> addEdge(V source, V target) {
		savedGraph.addEdge(source, target);
		return this;
	}

	public DirectedGraph<V, E> addEdge(V source, V target, E edge) {
		savedGraph.addEdge(source, target, edge);
		return this;
	}

	public boolean containsVertex(V v) {
		return savedGraph.containsVertex(v);
	}

	public boolean containsEdge(E e) {
		return savedGraph.containsEdge(e);
	}

	public boolean containsEdge(V source, V target) {
		return savedGraph.containsEdge(source, target);
	}

	public ImmutableSet<V> allVertex() {
		return newImmutableSet(savedGraph.vertexSet());
	}

	public ImmutableSet<E> allEdge() {
		return newImmutableSet(savedGraph.edgeSet());
	}

	public ImmutableSet<V> allChildVertex(V vertex) {
		return newUnifiedSet(new BreadthFirstIterator<>(savedGraph, vertex)).toImmutable();
	}

	public Graph<V, E> savedGraph() {
		return savedGraph;
	}

	public static void main(String[] args) {
		DirectedGraph<Integer, Edge> graph = DirectedGraph.newWith(Integer.class);

		graph.addVertex(1);
		graph.addVertex(11);
		graph.addVertex(12);
		graph.addVertex(13);
		graph.addVertex(22);
		graph.addVertex(221);
		graph.addVertex(222);
		graph.addVertex(223);

		graph.addEdge(1, 11);
		graph.addEdge(1, 12);
		graph.addEdge(12, 1);
		graph.addEdge(1, 13);
		graph.addEdge(12, 22);
		graph.addEdge(22, 221);
		graph.addEdge(22, 222);
		graph.addEdge(22, 223);

		// System.out.println(graph.inDegreeOf(11));
		// System.out.println(graph.outDegreeOf(1));

		System.out.println("===================");

		graph.addVertex(2221);
		graph.addVertex(2222);
		graph.addEdge(22, 221);
		graph.addEdge(222, 2221);
		graph.addEdge(222, 2222);

		ImmutableSet<Integer> allChildVertex = graph.allChildVertex(12);

		System.out.println(allChildVertex);

		System.out.println(graph.savedGraph().getType());

	}

}
