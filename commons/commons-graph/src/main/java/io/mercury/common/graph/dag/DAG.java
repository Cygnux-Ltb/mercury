package io.mercury.common.graph.dag;

/*
 * Copyright The Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DAG = Directed Acyclic Graph
 *
 * @author <a href="michal.maczka@dimatics.com">Michal Maczka</a>
 * @version $Id$ TODO this class should be renamed from DAG to Dag
 */
public class DAG implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8639632777005175375L;

	// ------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------
	/**
	 * Nodes will be kept in two data structures at the same time for faster
	 * processing
	 */
	/**
	 * Maps vertex's label to vertex
	 */
	private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();

	/**
	 * Conatin list of all vertices
	 */
	private List<Vertex> vertexList = new ArrayList<Vertex>();

	// ------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------

	/**
	 *
	 */
	public DAG() {
	}

	// ------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------

	/**
	 * @return
	 */
	public List<Vertex> getVertices() {
		return vertexList;
	}

	public Set<String> getLabels() {
		return vertexMap.keySet();
	}

	// ------------------------------------------------------------
	// Implementation
	// ------------------------------------------------------------

	/**
	 * Adds vertex to DAG. If vertex of given label already exist in DAG no vertex
	 * is added
	 *
	 * @param label The label of the Vertex
	 * @return New vertex if vertex of given label was not present in the DAG or
	 *         existing vertex if vertex of given label was already added to DAG
	 */
	public Vertex addVertex(final String label) {
		Vertex retValue = null;
		// check if vertex is already in DAG
		if (vertexMap.containsKey(label)) {
			retValue = vertexMap.get(label);
		} else {
			retValue = new Vertex(label);
			vertexMap.put(label, retValue);
			vertexList.add(retValue);
		}
		return retValue;
	}

	public void addEdge(String from, String to) throws CycleDetectedException {
		addEdge(addVertex(from), addVertex(to));
	}

	public void addEdge(final Vertex from, final Vertex to) throws CycleDetectedException {

		from.addEdgeTo(to);
		to.addEdgeFrom(from);

		List<String> cycle = CycleDetector.introducesCycle(to);

		if (cycle != null) {
			// remove edge which introduced cycle
			removeEdge(from, to);
			throw new CycleDetectedException(
					"Edge between '" + from + "' and '" + to + "' introduces to cycle in the graph", cycle);
		}
	}

	public void removeEdge(final String from, final String to) {
		removeEdge(addVertex(from), addVertex(to));
	}

	public void removeEdge(Vertex from, Vertex to) {
		from.removeEdgeTo(to);
		to.removeEdgeFrom(from);
	}

	public Vertex getVertex(String label) {
		return vertexMap.get(label);
	}

	public boolean hasEdge(String label1, String label2) {
		return getVertex(label1).getChildren().contains(getVertex(label2));
	}

	/**
	 * @param label
	 * @return
	 */
	public List<String> getChildLabels(String label) {
		return getVertex(label).getChildLabels();
	}

	/**
	 * @param label
	 * @return
	 */
	public List<String> getParentLabels(String label) {
		return getVertex(label).getParentLabels();
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		// this is what's failing..
		return super.clone();
	}

	/**
	 * Indicates if there is at least one edge leading to or from vertex of given
	 * label
	 *
	 * @return <code>true</code> if this vertex is connected with other
	 *         vertex,<code>false</code> otherwise
	 */
	public boolean isConnected(final String label) {
		return getVertex(label).isConnected();
	}

	/**
	 * Return the list of labels of successor in order decided by topological sort
	 *
	 * @param label The label of the vertex whose predecessors are searched
	 * @return The list of labels. Returned list contains also the label passed as
	 *         parameter to this method. This label should always be the last item
	 *         in the list.
	 */
	public List<String> getSuccessorLabels(final String label) {
		Vertex vertex = getVertex(label);
		List<String> retValue;
		// optimization.
		if (vertex.isLeaf()) {
			retValue = new ArrayList<String>(1);
			retValue.add(label);
		} else {
			retValue = TopologicalSorter.sort(vertex);
		}
		return retValue;
	}

}
