package io.mercury.persistence.rocksdb.map;

import org.rocksdb.Options;

import io.mercury.common.thread.Threads;
import io.mercury.persistence.rocksdb.map.entity.RocksReversibleKey;
import io.mercury.persistence.rocksdb.map.entity.RocksValue;

public class RocksReversibleMap<K extends RocksReversibleKey, V extends RocksValue> {

	

	public static void main(String[] args) {

		Options options = new Options();

		Runtime.getRuntime().addShutdownHook(Threads.newThread(() -> options.close(), "RocksContainerCloseThread"));

	}

	public V get(K key) {

		return null;
	}

	public V get(byte[] key) {

		return null;
	}

	public V put(K key, V value) {
		return value;
	}

	public void scan() {

	}

}
