package io.mercury.common.graph;

import static io.mercury.common.graph.Edge.EdgeSupplier;

import org.eclipse.collections.api.set.ImmutableSet;
import org.jgrapht.Graph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.traverse.BreadthFirstIterator;

import io.mercury.common.collections.ImmutableSets;
import io.mercury.common.collections.MutableSets;

public final class DirectedGraph<V> {

	private final Graph<V, Edge> graph;

	private DirectedGraph(Class<V> vclass) {
		this.graph = GraphTypeBuilder.directed().vertexClass(vclass).edgeSupplier(EdgeSupplier).buildGraph();
	}

	public static <V> DirectedGraph<V> buildOf(Class<V> vertexClass) {
		return new DirectedGraph<>(vertexClass);
	}

	public DirectedGraph<V> addVertex(V vertex) {
		if (vertex != null)
			graph.addVertex(vertex);
		return this;
	}

	public DirectedGraph<V> addEdge(V source, V target) {
		graph.addVertex(source);
		graph.addVertex(target);
		graph.addEdge(source, target);
		return this;
	}

	public boolean containsVertex(V v) {
		return graph.containsVertex(v);
	}

	public boolean containsEdge(Edge e) {
		return graph.containsEdge(e);
	}

	public boolean containsEdge(V source, V target) {
		return graph.containsEdge(source, target);
	}

	public ImmutableSet<V> allVertex() {
		return ImmutableSets.newSet(graph.vertexSet());
	}

	public ImmutableSet<Edge> allEdge() {
		return ImmutableSets.newSet(graph.edgeSet());
	}

	public ImmutableSet<V> allChildVertex(V vertex) {
		return MutableSets.newUnifiedSet(new BreadthFirstIterator<>(graph, vertex)).toImmutable();
	}

	public Graph<V, Edge> internalGraph() {
		return graph;
	}

	public static void main(String[] args) {
		DirectedGraph<Integer> graph = DirectedGraph.buildOf(Integer.class);

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
		// graph.addEdge(12, 1);
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

		System.out.println(graph.internalGraph().getType());

	}

}
