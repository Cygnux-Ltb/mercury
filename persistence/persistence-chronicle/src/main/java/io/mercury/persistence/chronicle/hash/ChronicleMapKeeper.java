package io.mercury.persistence.chronicle.hash;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import io.mercury.common.collections.keeper.AbstractKeeper;
import io.mercury.common.lang.Assertor;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

@ThreadSafe
public class ChronicleMapKeeper<K, V> extends AbstractKeeper<String, ChronicleMap<K, V>> implements Closeable {

	private ChronicleMapConfigurator<K, V> cfg;

	public ChronicleMapKeeper(@Nonnull ChronicleMapConfigurator<K, V> cfg) {
		Assertor.nonNull(cfg, "cfg");
		this.cfg = cfg;
	}

	public ChronicleMapConfigurator<K, V> getConfigurator() {
		return cfg;
	}

	protected final Object lock = new Object();

	// 关闭状态
	protected volatile boolean isClosed = false;

	@Nonnull
	@Override
	public ChronicleMap<K, V> acquire(@Nonnull String filename) throws ChronicleIOException {
		Assertor.nonEmpty(filename, "filename");
		synchronized (lock) {
			if (isClosed) {
				throw new IllegalStateException(
						"ChronicleMapKeeper configurator of -> {" + cfg.getConfigInfo() + "} is closed");
			}
			return super.acquire(filename);
		}
	}

	@Override
	protected ChronicleMap<K, V> createWithKey(String filename) throws ChronicleIOException {
		// 构建器
		ChronicleMapBuilder<K, V> builder = ChronicleMapBuilder
				// 设置KeyClass, ValueClass
				.of(cfg.getKeyClass(), cfg.getValueClass())
				// 设置put函数是否返回null
				.putReturnsNull(cfg.isPutReturnsNull())
				// 设置remove函数是否返回null
				.removeReturnsNull(cfg.isRemoveReturnsNull())
				// 设置名称
				.name(cfg.getConfigInfo())
				// 设置条目总数
				.entries(cfg.getEntries());
		// 设置块大小
		if (cfg.getActualChunkSize() > 0)
			builder.actualChunkSize(cfg.getActualChunkSize());

		// 设置关闭操作
		// builder.setPreShutdownAction(null);
		// 设置条目数校验
		// builder.checksumEntries(false);

		// 基于Key值设置平均长度
		if (cfg.getAverageKey() != null)
			builder.averageKey(cfg.getAverageKey());
		// 基于Value值设置平均长度
		if (cfg.getAverageValue() != null)
			builder.averageValue(cfg.getAverageValue());
		// 持久化选项
		if (cfg.isPersistent()) {
			File persistentFile = new File(cfg.getSavePath(), filename);
			try {
				if (!persistentFile.exists()) {
					// 创建文件目录
					File folder = persistentFile.getParentFile();
					if (!folder.exists())
						folder.mkdirs();
					return builder.createPersistedTo(persistentFile);
				} else {
					// Is recover data
					if (cfg.isRecover())
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
			this.isClosed = true;
		}
	}

}
