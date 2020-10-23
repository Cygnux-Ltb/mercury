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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Adaptive Radix Tree (ART) Java implementation
 * <p>
 * based on original paper:
 * <p>
 * The Adaptive Radix Tree: ARTful Indexing for Main-Memory Databases
 * <p>
 * Viktor Leis, Alfons Kemper, Thomas Neumann Fakultat fur Informatik Technische
 * Universitat Munchen Boltzmannstrae 3, D-85748 Garching
 * <p>
 * https://db.in.tum.de/~leis/papers/ART.pdf
 * <p>
 * Target operations: - GET or (PUT + GET_LOWER/HIGHER) -
 * placing/moving/bulkload order - often GET, more rare PUT ??cache - REMOVE -
 * cancel or move - last order in the bucket - TRAVERSE from LOWER - filling L2
 * market data, in hot area (Node256 or Node48). - REMOVE price during matching
 * - !! can use RANGE removal operation - rare, but latency critical - GET or
 * PUT if not exists - inserting back own orders, very rare
 */
public final class LongAdaptiveRadixTreeMap<V> {

	private static final int INITIAL_LEVEL = 56;

	private IArtNode<V> root = null;

	private final ObjectsPool objectsPool;

	public LongAdaptiveRadixTreeMap(ObjectsPool objectsPool) {
		this.objectsPool = objectsPool;
	}

	public LongAdaptiveRadixTreeMap() {
		objectsPool = ObjectsPool.createDefaultPool();
	}

	public V get(final long key) {
		return root != null ? root.getValue(key, INITIAL_LEVEL) : null;
	}

	public void put(final long key, final V value) {
		if (root == null) {
			final ArtNode4<V> node = objectsPool.get(ObjectsPool.ART_NODE_4, ArtNode4::new);
			node.initFirstKey(key, value);
			root = node;
		} else {
			final IArtNode<V> upSizedNode = root.put(key, INITIAL_LEVEL, value);
			if (upSizedNode != null) {
				// TODO put old into the pool
				root = upSizedNode;
			}
		}
	}

	public V getOrInsert(final long key, Supplier<V> supplier) {
		// TODO implement
		return null;
	}

	public void getOrInsertFromNode(final IArtNode<V> node, Supplier<V> supplier) {
		// TODO implement
	}

	public void remove(final long key) {
		if (root != null) {
			final IArtNode<V> downSizeNode = root.remove(key, INITIAL_LEVEL);
			// ignore null because can not remove root
			if (downSizeNode != root) {
				// TODO put old into the pool
				root = downSizeNode;
			}
		}
	}

	public void clear() {
		// produces garbage
		root = null;
	}

	/**
	 * remove keys range
	 *
	 * @param keyFromInclusive from key inclusive
	 * @param keyToExclusive   to key exclusive
	 */
	public void removeRange(final long keyFromInclusive, final long keyToExclusive) {
		// TODO
		throw new UnsupportedOperationException();
	}

	// TODO putAndGetHigherValue
	// TODO putAndGetLowerValue

	// TODO moveToAnotherKey(long oldKey, long newKey) - throw exception if not
	// found

	public V getHigherValue(long key) {
		if (root != null && key != Long.MAX_VALUE) {
			return root.getCeilingValue(key + 1, INITIAL_LEVEL);
		} else {
			return null;
		}
	}

	public V getLowerValue(long key) {
		if (root != null && key != 0) {
			return root.getFloorValue(key - 1, INITIAL_LEVEL);
		} else {
			return null;
		}
	}

	public int forEach(LongObjConsumer<V> consumer, int limit) {
		if (root != null) {
			return root.forEach(consumer, limit);
		} else {
			return 0;
		}
	}

	public int forEachDesc(LongObjConsumer<V> consumer, int limit) {
		if (root != null) {
			return root.forEachDesc(consumer, limit);
		} else {
			return 0;
		}
	}

	public int size(int limit) {
		if (root != null) {
			return Math.min(root.size(limit), limit);
		} else {
			return 0;
		}
	}

	public List<Map.Entry<Long, V>> entriesList() {
		if (root != null) {
			return root.entries();
		} else {
			return Collections.emptyList();
		}
	}

	public void validateInternalState() {
		if (root != null) {
			// TODO initial level
			root.validateInternalState(INITIAL_LEVEL);
		}
	}

	public String outputDiagram() {
		if (root != null) {
			return root.outputDiagram("", INITIAL_LEVEL);
		} else {
			return "";
		}
	}

	static <V> IArtNode<V> branchIfRequired(final long key, final V value, final long nodeKey, final int nodeLevel,
			final IArtNode<V> caller) {

		final long keyDiff = key ^ nodeKey;

		// check if there is common part
		if ((keyDiff & (-1L << nodeLevel)) == 0) {
			return null;
		}

		// on which level
		final int newLevel = (63 - Long.numberOfLeadingZeros(keyDiff)) & 0xF8;
		if (newLevel == nodeLevel) {
			return null;
		}

		final ObjectsPool objectsPool = caller.getObjectsPool();
		final ArtNode4<V> newSubNode = objectsPool.get(ObjectsPool.ART_NODE_4, ArtNode4::new);
		newSubNode.initFirstKey(key, value);

		final ArtNode4<V> newNode = objectsPool.get(ObjectsPool.ART_NODE_4, ArtNode4::new);
		newNode.initTwoKeys(nodeKey, caller, key, newSubNode, newLevel);

		return newNode;
	}

//    static boolean keyNotMatches(long key, int level, long nodeKey, int nodeLevel) {
//        return (level != nodeLevel && ((key ^ nodeKey) & (-1L << (nodeLevel + 8))) != 0);
//    }
	// TODO remove based on leaf (having reference) ?

	static String outputDiagram(String prefix, int level, int nodeLevel, long nodeKey, short numChildren,
			Function<Short, Short> subKeys, Function<Short, Object> nodes) {

		final String baseKeyPrefix;
		final String baseKeyPrefix1;
		final int lvlDiff = level - nodeLevel;
//        log.debug("nodeKey={} level={} nodeLevel={} lvlDiff={}", String.format("%X", nodeKey), level, nodeLevel, lvlDiff);

		if (lvlDiff != 0) {
			int chars = lvlDiff >> 2;
//            baseKeyPrefix = String.format("[%0" + chars + "X]", nodeKey & ((1L << lvlDiff) - 1L) << nodeLevel);
			long mask = ((1L << lvlDiff) - 1L);
//            log.debug("mask={}", String.format("%X", mask));
//            log.debug("nodeKey >> level = {}", String.format("%X", nodeKey >> (nodeLevel + 8)));
//            log.debug("nodeKey >> level  & mask= {}", String.format("%X", (nodeKey >> (nodeLevel + 8)) & mask));
			baseKeyPrefix = charRepeat('─', chars - 2)
					+ String.format("[%0" + chars + "X]", (nodeKey >> (nodeLevel + 8)) & mask);
			baseKeyPrefix1 = charRepeat(' ', chars * 2);
		} else {
			baseKeyPrefix = "";
			baseKeyPrefix1 = "";
		}
		// log.debug("baseKeyPrefix={}", baseKeyPrefix);

		StringBuilder sb = new StringBuilder();
		for (short i = 0; i < numChildren; i++) {
			Object node = nodes.apply(i);
			String key = String.format("%s%02X", baseKeyPrefix, subKeys.apply(i));
			String x = (i == 0 ? (numChildren == 1 ? "──" : "┬─")
					: (i + 1 == numChildren ? (prefix + "└─") : (prefix + "├─")));

			if (nodeLevel == 0) {
				sb.append(x + key + " = " + node);
			} else {
				sb.append(x + key + "" + (((IArtNode<?>) node).outputDiagram(
						prefix + (i + 1 == numChildren ? "    " : "│   ") + baseKeyPrefix1, nodeLevel - 8)));
			}
			if (i < numChildren - 1) {
				sb.append("\n");
			} else if (nodeLevel == 0) {
				sb.append("\n" + prefix);
			}
		}
		return sb.toString();
	}

	private static String charRepeat(char x, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(x);
		}
		return sb.toString();
	}

	public static final class Entry<V> implements Map.Entry<Long, V> {

		final long key;

		V value;

		public Entry(long key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public Long getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			final V v = this.value;
			this.value = value;
			return v;
		}
	}

}
