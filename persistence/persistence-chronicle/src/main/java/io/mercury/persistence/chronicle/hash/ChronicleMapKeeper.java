package io.mercury.persistence.chronicle.hash;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import io.mercury.common.collections.keeper.KeeperBaseImpl;
import io.mercury.common.util.Assertor;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

@ThreadSafe
public class ChronicleMapKeeper<K, V> extends KeeperBaseImpl<String, ChronicleMap<K, V>> implements Closeable {

	private ChronicleMapConfigurator<K, V> configurator;

	public ChronicleMapKeeper(@Nonnull ChronicleMapConfigurator<K, V> configurator) {
		Assertor.nonNull(configurator, "configurator");
		this.configurator = configurator;
	}

	private final Object lock = new Object();

	@Override
	public ChronicleMap<K, V> acquire(String filename) throws ChronicleIOException {
		Assertor.nonEmpty(filename, "filename");
		synchronized (lock) {
			return super.acquire(filename);
		}
	}

	@Override
	protected ChronicleMap<K, V> createWithKey(String filename) throws ChronicleIOException {
		// 构建器
		ChronicleMapBuilder<K, V> builder = ChronicleMapBuilder
				// 设置KeyClass, ValueClass
				.of(configurator.keyClass(), configurator.valueClass())
				// 设置put函数是否返回null
				.putReturnsNull(configurator.putReturnsNull())
				// 设置remove函数是否返回null
				.removeReturnsNull(configurator.removeReturnsNull())
				// 设置名称
				.name(configurator.fullInfo())
				// 设置条目总数
				.entries(configurator.entries());
		// 设置块大小
		if (configurator.actualChunkSize() > 0)
			builder.actualChunkSize(configurator.actualChunkSize());
		
		// 设置关闭操作
		// builder.setPreShutdownAction(null);
		// 设置条目数校验
		//builder.checksumEntries(false);
		
		// 基于Key值设置平均长度
		if (configurator.averageKey() != null)
			builder.averageKey(configurator.averageKey());
		// 基于Value值设置平均长度
		if (configurator.averageValue() != null)
			builder.averageValue(configurator.averageValue());
		// 持久化选项
		if (configurator.persistent()) {
			File persistentFile = new File(configurator.savePath(), filename);
			try {
				if (!persistentFile.exists()) {
					// 创建文件目录
					File folder = persistentFile.getParentFile();
					if (!folder.exists())
						folder.mkdirs();
					return builder.createPersistedTo(persistentFile);
				} else {
					// Is recover data
					if (configurator.recover())
						return builder.createOrRecoverPersistedTo(persistentFile);
					else
						return builder.createPersistedTo(persistentFile);
				}
			} catch (IOException e) {
				throw new ChronicleIOException(e);
			}
		} else
			return builder.create();
	}

	@Override
	public void close() throws IOException {
		synchronized (lock) {
			Set<String> keySet = savedMap.keySet();
			for (String key : keySet) {
				ChronicleMap<K, V> map = savedMap.get(key);
				if (map.isOpen()) {
					map.close();
				}
				savedMap.remove(key);
			}
		}
	}

}
