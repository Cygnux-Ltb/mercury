/*
 * Copyright 2019-2020 Maksim Zheravin
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
package io.mercury.common.collections.art;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The largest node type is simply an array of 256 pointers and is used for
 * storing between 49 and 256 entries. With this representation, the next node
 * can be found very efficiently using a single lookup of the key byte in that
 * array. No additional indirection is necessary. If most entries are not null,
 * this representation is also very space efficient because only pointers need
 * to be stored.
 */
public final class ArtNode256<V> implements IArtNode<V> {

	private static final int NODE48_SWITCH_THRESHOLD = 37;

	// direct addressing
	final Object[] nodes = new Object[256];

	long nodeKey;
	int nodeLevel;
	short numChildren;

	private final ObjectsPool objectsPool;

	public ArtNode256(ObjectsPool objectsPool) {
		this.objectsPool = objectsPool;
	}

	void initFromNode48(ArtNode48<V> artNode48, short subKey, Object newElement) {

		this.nodeLevel = artNode48.nodeLevel;
		this.nodeKey = artNode48.nodeKey;
		final int sourceSize = 48;
		for (short i = 0; i < 256; i++) {
			final byte index = artNode48.indexes[i];
			if (index != -1) {
				this.nodes[i] = artNode48.nodes[index];
			}
		}
		this.nodes[subKey] = newElement;
		this.numChildren = sourceSize + 1;

		Arrays.fill(artNode48.nodes, null);
		Arrays.fill(artNode48.indexes, (byte) -1);
		objectsPool.put(ObjectsPool.ART_NODE_48, artNode48);
	}

	@Override
	@SuppressWarnings("unchecked")
	public V getValue(final long key, final int level) {
		if (level != nodeLevel && ((key ^ nodeKey) & (-1L << (nodeLevel + 8))) != 0) {
			return null;
		}
		final short idx = (short) ((key >>> nodeLevel) & 0xFF);
		final Object node = nodes[idx];
		if (node != null) {
			return nodeLevel == 0 ? (V) node : ((IArtNode<V>) node).getValue(key, nodeLevel - 8);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public IArtNode<V> put(final long key, final int level, final V value) {
		if (level != nodeLevel) {
			final IArtNode<V> branch = LongAdaptiveRadixTreeMap.branchIfRequired(key, value, nodeKey, nodeLevel, this);
			if (branch != null) {
				return branch;
			}
		}
		final short idx = (short) ((key >>> nodeLevel) & 0xFF);
		if (nodes[idx] == null) {
			// new object will be inserted
			numChildren++;
		}

		if (nodeLevel == 0) {
			nodes[idx] = value;
		} else {
			IArtNode<V> node = (IArtNode<V>) nodes[idx];
			if (node != null) {
				final IArtNode<V> resizedNode = node.put(key, nodeLevel - 8, value);
				if (resizedNode != null) {
					// TODO put old into the pool
					// update resized node if capacity has increased
					nodes[idx] = resizedNode;
				}
			} else {
				final ArtNode4<V> newSubNode = objectsPool.get(ObjectsPool.ART_NODE_4, ArtNode4::new);
				newSubNode.initFirstKey(key, value);
				nodes[idx] = newSubNode;
			}
		}

		// never need to increase the size
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public IArtNode<V> remove(long key, int level) {
		if (level != nodeLevel && ((key ^ nodeKey) & (-1L << (nodeLevel + 8))) != 0) {
			return this;
		}
		final short idx = (short) ((key >>> nodeLevel) & 0xFF);
		if (nodes[idx] == null) {
			return this;
		}

		if (nodeLevel == 0) {
			nodes[idx] = null;
			numChildren--;
		} else {
			final IArtNode<V> node = (IArtNode<V>) nodes[idx];
			final IArtNode<V> resizedNode = node.remove(key, nodeLevel - 8);
			if (resizedNode != node) {
				// TODO put old into the pool
				// update resized node if capacity has decreased
				nodes[idx] = resizedNode;
				if (resizedNode == null) {
					numChildren--;
				}
			}
		}

		if (numChildren == NODE48_SWITCH_THRESHOLD) {
			final ArtNode48<V> newNode = objectsPool.get(ObjectsPool.ART_NODE_48, ArtNode48::new);
			newNode.initFromNode256(this);
			return newNode;
		} else {
			return this;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public V getCeilingValue(long key, int level) {
//        log.debug("key = {}", String.format("%Xh", key));
		// special processing for compacted nodes
		if ((level != nodeLevel)) {
			// try first
			final long mask = -1L << (nodeLevel + 8);
//            log.debug("key & mask = {} > nodeKey & mask = {}", String.format("%Xh", key & mask), String.format("%Xh", nodeKey & mask));
			final long keyWithMask = key & mask;
			final long nodeKeyWithMask = nodeKey & mask;
			if (nodeKeyWithMask < keyWithMask) {
				// compacted part is lower - no need to search for ceiling entry here
				return null;
			} else if (keyWithMask != nodeKeyWithMask) {
				// find first lowest key, because compacted nodekey is higher
				key = 0;
			}
		}

		short idx = (short) ((key >>> nodeLevel) & 0xFF);
		Object node = nodes[idx];
		if (node != null) {
			// if exact key found
			final V res = nodeLevel == 0 ? (V) node : ((IArtNode<V>) node).getCeilingValue(key, nodeLevel - 8);
			if (res != null) {
				// return if found ceiling, otherwise will try next one
				return res;
			}
		}

		// if exact key not found - searching for first higher key
		while (++idx < 256) {
//            log.debug("idx+ = {}", String.format("%Xh", idx));
			node = nodes[idx];
			if (node != null) {
				return (nodeLevel == 0) ? (V) node : ((IArtNode<V>) node).getCeilingValue(0, nodeLevel - 8);// find
																											// first
																											// lowest
																											// key
			}
		}
		// no keys found
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public V getFloorValue(long key, int level) {
		// log.debug("key = {}", String.format("%Xh", key));
		// special processing for compacted nodes
		if ((level != nodeLevel)) {
			// try first
			final long mask = -1L << (nodeLevel + 8);
//            log.debug("key & mask = {} > nodeKey & mask = {}",
//                    String.format("%Xh", key & mask), String.format("%Xh", nodeKey & mask));
			final long keyWithMask = key & mask;
			final long nodeKeyWithMask = nodeKey & mask;
			if (nodeKeyWithMask > keyWithMask) {
				// compacted part is higher - no need to search for floor entry here
				return null;
			} else if (keyWithMask != nodeKeyWithMask) {
				// find highest value, because compacted nodekey is lower
				key = Long.MAX_VALUE;
			}
		}

		short idx = (short) ((key >>> nodeLevel) & 0xFF);
		Object node = nodes[idx];
		if (node != null) {
			// if exact key found
			final V res = nodeLevel == 0 ? (V) node : ((IArtNode<V>) node).getFloorValue(key, nodeLevel - 8);
			if (res != null) {
				// return if found floor, otherwise will try prev one
				return res;
			}
		}

		// if exact key not found - searching for first lower key
		while (--idx >= 0) {
//            log.debug("idx+ = {}", String.format("%Xh", idx));
			node = nodes[idx];
			if (node != null) {
				return (nodeLevel == 0) ? (V) node : ((IArtNode<V>) node).getFloorValue(Long.MAX_VALUE, nodeLevel - 8);// find
																														// first
																														// highest
																														// key
			}
		}
		// no keys found
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int forEach(LongObjConsumer<V> consumer, int limit) {
		if (nodeLevel == 0) {
			final long keyBase = (nodeKey >>> 8) << 8;
			int numFound = 0;
			for (short i = 0; i < 256; i++) {
				if (numFound == limit) {
					return numFound;
				}
				final V node = (V) nodes[i];
				if (node != null) {
					consumer.accept(keyBase + i, node);
					numFound++;
				}
			}
			return numFound;
		} else {
			int numLeft = limit;
			for (short i = 0; i < 256 && numLeft > 0; i++) {
				final IArtNode<V> node = (IArtNode<V>) nodes[i];
				if (node != null) {
					numLeft -= node.forEach(consumer, numLeft);
				}
			}
			return limit - numLeft;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public int forEachDesc(LongObjConsumer<V> consumer, int limit) {
		if (nodeLevel == 0) {
			final long keyBase = (nodeKey >>> 8) << 8;
			int numFound = 0;
			for (short i = 255; i >= 0; i--) {
				if (numFound == limit) {
					return numFound;
				}
				final V node = (V) nodes[i];
				if (node != null) {
					consumer.accept(keyBase + i, node);
					numFound++;
				}
			}
			return numFound;
		} else {
			int numLeft = limit;
			for (short i = 255; i >= 0 && numLeft > 0; i--) {
				final IArtNode<V> node = (IArtNode<V>) nodes[i];
				if (node != null) {
					numLeft -= node.forEachDesc(consumer, numLeft);
				}
			}
			return limit - numLeft;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public int size(int limit) {
		if (nodeLevel == 0) {
			return numChildren;
		} else {
			int numLeft = limit;
			for (short i = 0; i < 256 && numLeft > 0; i++) {
				final IArtNode<V> node = (IArtNode<V>) nodes[i];
				if (node != null) {
					numLeft -= node.size(numLeft);
				}
			}
			return limit - numLeft;
		}
	}

	@Override
	public void validateInternalState(int level) {
		if (nodeLevel > level)
			throw new IllegalStateException("unexpected nodeLevel");
		int found = 0;
		for (int i = 0; i < 256; i++) {
			Object node = nodes[i];
			if (node != null) {
				if (node instanceof IArtNode) {
					if (nodeLevel == 0)
						throw new IllegalStateException("unexpected node type");
					IArtNode<?> artNode = (IArtNode<?>) node;
					artNode.validateInternalState(nodeLevel - 8);
				} else {
					if (nodeLevel != 0)
						throw new IllegalStateException("unexpected node type");
				}
				found++;
			}
		}
		if (found != numChildren) {
			throw new IllegalStateException("wrong numChildren");
		}
		if (numChildren <= NODE48_SWITCH_THRESHOLD || numChildren > 256) {
			throw new IllegalStateException("unexpected numChildren");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map.Entry<Long, V>> entries() {
		final long keyPrefix = nodeKey & (-1L << 8);
		final List<Map.Entry<Long, V>> list = new ArrayList<>();
		final short[] keys = createKeysArray();
		for (int i = 0; i < numChildren; i++) {
			if (nodeLevel == 0) {
				list.add(new LongAdaptiveRadixTreeMap.Entry<>(keyPrefix + keys[i], (V) nodes[keys[i]]));
			} else {
				list.addAll(((IArtNode<V>) nodes[keys[i]]).entries());
			}
		}
		return list;
	}

	@Override
	public String outputDiagram(String prefix, int level) {
		final short[] keys = createKeysArray();
		return LongAdaptiveRadixTreeMap.outputDiagram(prefix, level, nodeLevel, nodeKey, numChildren, idx -> keys[idx],
				idx -> nodes[keys[idx]]);
	}

	@Override
	public ObjectsPool getObjectsPool() {
		return objectsPool;
	}

	@Override
	public String toString() {
		return "ArtNode256{" + "nodeKey=" + nodeKey + ", nodeLevel=" + nodeLevel + ", numChildren=" + numChildren + '}';
	}

	private short[] createKeysArray() {
		short[] keys = new short[numChildren];
		int j = 0;
		for (short i = 0; i < 256; i++) {
			if (nodes[i] != null) {
				keys[j++] = i;
			}
		}
		return keys;
	}
}