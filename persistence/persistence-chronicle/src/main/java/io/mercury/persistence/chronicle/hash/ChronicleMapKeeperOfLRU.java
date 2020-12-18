package io.mercury.persistence.chronicle.hash;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import io.mercury.common.datetime.EpochTime;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

@ThreadSafe
public class ChronicleMapKeeperOfLRU<K, V> extends ChronicleMapKeeper<K, V> {

	private final Duration expiration;

	private final ChronicleMap<String, Long> lastUsedLog;
	
	private final File savePath;

	public ChronicleMapKeeperOfLRU(@Nonnull ChronicleMapConfigurator<K, V> configurator, Duration expiration) {
		super(configurator);
		this.expiration = expiration;
		this.savePath = configurator.savePath();
		// 构建器
		ChronicleMapBuilder<String, Long> builder = ChronicleMapBuilder
				// 设置KeyClass, ValueClass
				.of(String.class, Long.class)
				// 设置put函数是否返回null
				.putReturnsNull(true)
				// 设置remove函数是否返回null
				.removeReturnsNull(true)
				// 设置LRU名称
				.name(configurator.fullInfo() + "-last-used-log")
				// 设置LRU条目总数
				.entries(65536);
		File persistentFile = new File(configurator.savePath(), ".last-used-log");
		try {
			if (!persistentFile.exists()) {
				// 创建文件目录
				File folder = persistentFile.getParentFile();
				if (!folder.exists()) {
					folder.mkdirs();
				}
				this.lastUsedLog = builder.createPersistedTo(persistentFile);
			} else {
				// Is recover data
				if (configurator.recover())
					this.lastUsedLog = builder.createOrRecoverPersistedTo(persistentFile);
				else
					this.lastUsedLog = builder.createPersistedTo(persistentFile);
			}
		} catch (IOException e) {
			throw new ChronicleIOException(e);
		}
	}

	@Override
	public ChronicleMap<K, V> acquire(String filename) throws ChronicleIOException {
		ChronicleMap<K, V> acquire = super.acquire(filename);
		lastUsedLog.put(filename, EpochTime.millis());
		return acquire;
	}

}
