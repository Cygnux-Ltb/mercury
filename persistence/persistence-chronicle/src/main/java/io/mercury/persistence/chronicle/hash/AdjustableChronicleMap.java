package io.mercury.persistence.chronicle.hash;

import net.openhft.chronicle.map.ChronicleMap;

public final class AdjustableChronicleMap<K, V> implements AdjustableChronicle<ChronicleMap<K, V>> {

	private ChronicleMap<K, V> entity;

	public AdjustableChronicleMap(ChronicleMap<K, V> entity) {
		this.entity = entity;
	}

	@Override
	public ChronicleMap<K, V> entity() {
		return entity;
	}

}
