package io.mercury.persistence.chronicle.hash;

import net.openhft.chronicle.set.ChronicleSet;

public final class AdjustableChronicleSet<K> implements AdjustableChronicle<ChronicleSet<K>> {

	private final ChronicleSet<K> entity;

	public AdjustableChronicleSet(ChronicleSet<K> entity) {
		this.entity = entity;
	}

	@Override
	public ChronicleSet<K> entity() {
		return entity;
	}

}
