package io.mercury.persistence.rocksdb.map;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.DBOptions;
import org.rocksdb.Holder;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.Statistics;

import io.mercury.common.thread.ShutdownHooks;
import io.mercury.common.thread.Threads;
import io.mercury.persistence.rocksdb.exception.RocksRuntimeException;
import io.mercury.persistence.rocksdb.map.entity.RocksKey;
import io.mercury.persistence.rocksdb.map.entity.RocksValue;

public class RocksMap<K extends RocksKey, V extends RocksValue> implements Closeable {

	private final Options options;
	private final RocksDB rocksdb;

	public RocksMap(String savePath) throws RocksRuntimeException {
		DBOptions dbOptions = new DBOptions();
		ColumnFamilyOptions columnFamilyOptions = new ColumnFamilyOptions();
		this.options = new Options(dbOptions, columnFamilyOptions);
		Statistics statistics = new Statistics();
		options.setStatistics(statistics);
		options.setCreateIfMissing(true);
		try {
			this.rocksdb = RocksDB.open(options, savePath);
		} catch (RocksDBException e) {
			throw new RocksRuntimeException(e);
		}
		ShutdownHooks.closeResourcesWhenShutdown(this);
	}

	public static void main(String[] args) {
		Options options = new Options();
		Runtime.getRuntime().addShutdownHook(Threads.newThread("RocksContainerCloseThread", () -> options.close()));
	}

	public void scan() {

	}

	public boolean notExist(K key) {
		return !rocksdb.keyMayExist(key.key(), new Holder<>());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key) {
		return null;
	}

	/**
	 * 
	 * @param key0
	 * @param key1
	 * @return
	 */
	public Collection<V> scan(K key0, K key1) {
		return null;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public V put(K key, V value) {
		return null;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public V remove(K key) {
		return null;
	}

	/**
	 * 
	 * @param keyValues
	 */
	public void put(MutableSet<Pair<K, V>> keyValues) {

	}

	/**
	 * 
	 */
	public void clear() {

	}

	@Override
	public void close() throws IOException {
		if (options != null)
			options.close();

		if (rocksdb != null)
			rocksdb.close();
	}

}
