package io.mercury.persistence.chronicle.hash;

import net.openhft.chronicle.map.ChronicleMap;

public final class AdjustableChronicleMap<K, V> implements AdjustableChronicle<ChronicleMap<K, V>> {

	private ChronicleMap<K, V> entity;

	@Override
	public ChronicleMap<K, V> entity() {
		return entity;
	}

}
