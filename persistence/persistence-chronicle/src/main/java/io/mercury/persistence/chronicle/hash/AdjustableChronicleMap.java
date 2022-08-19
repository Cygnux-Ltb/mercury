package io.mercury.persistence.chronicle.hash;

import net.openhft.chronicle.map.ChronicleMap;

public final class AdjustableChronicleMap<K, V> {

    private final ChronicleMap<K, V> entity;

    public AdjustableChronicleMap(ChronicleMap<K, V> entity) {
        this.entity = entity;
    }

    public ChronicleMap<K, V> entity() {
        return entity;
    }

}