package io.mercury.persistence.chronicle.hash;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import io.mercury.common.collections.customize.BaseKeeper;
import io.mercury.common.util.Assertor;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

@ThreadSafe
public class ChronicleMapKeeper<K, V> extends BaseKeeper<String, ChronicleMap<K, V>> implements Closeable {

	private ChronicleMapConfigurator<K, V> configurator;

	public ChronicleMapKeeper(@Nonnull ChronicleMapConfigurator<K, V> configurator) {
		this.configurator = Assertor.nonNull(configurator, "configurator");
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
		ChronicleMapBuilder<K, V> builder = ChronicleMapBuilder.of(configurator.keyClass(), configurator.valueClass())
				.putReturnsNull(configurator.putReturnsNull()).removeReturnsNull(configurator.removeReturnsNull())
				.entries(configurator.entries());
		// 设置块大小
		if (configurator.actualChunkSize() > 0)
			builder.actualChunkSize(configurator.actualChunkSize());
		// 基于Key值设置平均长度
		if (configurator.averageKey() != null)
			builder.averageKey(configurator.averageKey());
		// 基于Value值设置平均长度
		if (configurator.averageValue() != null)
			builder.averageValue(configurator.averageValue());
		// 持久化选项
		if (configurator.persistent()) {
			File persistedFile = new File(configurator.savePath(), filename);
			try {
				if (!persistedFile.exists()) {
					File parentFile = persistedFile.getParentFile();
					if (!parentFile.exists())
						parentFile.mkdirs();
					return builder.createPersistedTo(persistedFile);
				} else {
					// Is recover data
					if (configurator.recover())
						return builder.createOrRecoverPersistedTo(persistedFile);
					else
						return builder.createPersistedTo(persistedFile);
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
