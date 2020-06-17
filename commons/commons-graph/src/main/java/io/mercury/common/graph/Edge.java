package io.mercury.common.graph;

import java.util.function.Supplier;

import org.jgrapht.graph.DefaultEdge;

public class Edge extends DefaultEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1497450312630076892L;

	public static final Supplier<Edge> EdgeSupplier = Edge::new;

}
