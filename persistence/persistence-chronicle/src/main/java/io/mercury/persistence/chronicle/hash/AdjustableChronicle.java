package io.mercury.persistence.chronicle.hash;

import net.openhft.chronicle.hash.ChronicleHash;

public interface AdjustableChronicle<T extends ChronicleHash<?, ?, ?, ?>> {

	T entity();

}