package io.mercury.persistence.chronicle.hash;

import net.openhft.chronicle.set.ChronicleSet;

public final class AdjustableChronicleSet<K> {

    private final ChronicleSet<K> entity;

    public AdjustableChronicleSet(ChronicleSet<K> entity) {
        this.entity = entity;
    }

    public ChronicleSet<K> entity() {
        return entity;
    }

}
