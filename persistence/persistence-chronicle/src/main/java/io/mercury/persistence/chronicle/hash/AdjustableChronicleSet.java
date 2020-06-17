package io.mercury.persistence.chronicle.hash;

import net.openhft.chronicle.set.ChronicleSet;

public class AdjustableChronicleSet<K> implements AdjustableChronicle<ChronicleSet<K>> {

	private ChronicleSet<K> entity;

	@Override
	public ChronicleSet<K> entity() {
		return entity;
	}

}
