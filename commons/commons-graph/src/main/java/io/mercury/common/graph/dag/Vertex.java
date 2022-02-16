package io.mercury.common.graph.dag;

import static org.eclipse.collections.impl.collector.Collectors2.toImmutableList;

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

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import io.mercury.common.collections.MutableLists;

/**
 * 
 * @author <a href="michal.maczka@dimatics.com">Michal Maczka</a>
 * 
 */
public class Vertex implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6226650981773662643L;

	// ------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------

	private final String label;

	private final MutableList<Vertex> children = MutableLists.newFastList();

	private final MutableList<Vertex> parents = MutableLists.newFastList();

	public String getLabel() {
		return label;
	}

	/**
	 * 
	 * @return
	 */
	public ImmutableList<Vertex> getChildren() {
		return children.toImmutable();
	}

	/**
	 * 
	 * @return
	 */
	public ImmutableList<Vertex> getParents() {
		return parents.toImmutable();
	}

	// ------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------

	/**
	 *
	 */
	public Vertex(String label) {
		this.label = label;
	}

	// ------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------

	/**
	 * @param vertex
	 */
	public void addEdgeTo(Vertex vertex) {
		children.add(vertex);
	}

	/**
	 * @param vertex
	 */
	public void removeEdgeTo(Vertex vertex) {
		children.remove(vertex);
	}

	/**
	 * @param vertex
	 */
	public void addEdgeFrom(Vertex vertex) {
		parents.add(vertex);
	}

	/**
	 * 
	 * @param vertex
	 */
	public void removeEdgeFrom(Vertex vertex) {
		parents.remove(vertex);
	}

	/**
	 * Get the labels used by the most direct children.
	 *
	 * @return the labels used by the most direct children.
	 */
	public ImmutableList<String> getChildLabels() {
		return children.stream().map(v -> v.getLabel()).collect(toImmutableList());
	}

	/**
	 * Get the labels used by the most direct ancestors (parents).
	 *
	 * @return the labels used parents
	 */
	public ImmutableList<String> getParentLabels() {
		return parents.stream().map(v -> v.getLabel()).collect(toImmutableList());
	}

	/**
	 * Indicates if given vertex has no child
	 *
	 * @return <code>true</code> if this vertex has no child, <code>false</code>
	 *         otherwise
	 */
	public boolean isLeaf() {
		return children.size() == 0;
	}

	/**
	 * Indicates if given vertex has no parent
	 *
	 * @return <code>true</code> if this vertex has no parent, <code>false</code>
	 *         otherwise
	 */
	public boolean isRoot() {
		return parents.size() == 0;
	}

	/**
	 * Indicates if there is at least one edee leading to or from given vertex
	 *
	 * @return <code>true</code> if this vertex is connected with other
	 *         vertex,<code>false</code> otherwise
	 */
	public boolean isConnected() {
		return isRoot() || isLeaf();
	}

	public Object clone() throws CloneNotSupportedException {
		// this is what's failing..
		return super.clone();
	}

	public String toString() {
		return "Vertex{" + "label='" + label + "'" + "}";
	}

}
