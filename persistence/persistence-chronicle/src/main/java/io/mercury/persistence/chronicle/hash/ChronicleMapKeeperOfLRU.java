package io.mercury.persistence.chronicle.hash;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import io.mercury.common.datetime.EpochTime;
import io.mercury.common.thread.Threads;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

@ThreadSafe
public class ChronicleMapKeeperOfLRU<K, V> extends ChronicleMapKeeper<K, V> {

	// 过期毫秒数
	private final long expireMillis;

	// 最后使用记录
	private final ChronicleMap<String, Long> lastUsedLog;

	// 存储路径
	private final File savePath;

	public ChronicleMapKeeperOfLRU(@Nonnull ChronicleMapConfigurator<K, V> cfg, @Nonnull Duration expire) {
		this(cfg, expire, 65536);
	}

	public ChronicleMapKeeperOfLRU(@Nonnull ChronicleMapConfigurator<K, V> cfg, @Nonnull Duration expire,
			int fileTotal) {
		super(cfg);
		final long millis = expire.toMillis();
		// 定义最小过期时间为两小时
		final long minExpireMillis = 2 * 60 * 60 * 1000;
		this.expireMillis = millis < minExpireMillis ? minExpireMillis : millis;
		this.savePath = cfg.getSavePath();
		// LRU记录构建器
		final ChronicleMapBuilder<String, Long> builder = ChronicleMapBuilder
				// 设置KeyClass, ValueClass
				.of(String.class, Long.class)
				// 设置Key的平均大小
				.averageKeySize(16)
				// 设置put函数是否返回null
				.putReturnsNull(true)
				// 设置remove函数是否返回null
				.removeReturnsNull(true)
				// 设置LRU名称
				.name(cfg.getCfgInfo() + "-last-used-log")
				// 设置LRU记录条目总数, 最小65536条
				.entries(fileTotal < 65536 ? 65536 : fileTotal);
		File persistentFile = new File(cfg.getSavePath(), ".last-used-log");
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
				if (cfg.isRecover())
					this.lastUsedLog = builder.createOrRecoverPersistedTo(persistentFile);
				else
					this.lastUsedLog = builder.createPersistedTo(persistentFile);
			}
		} catch (IOException e) {
			throw new ChronicleIOException(e);
		}
		this.cleanupThread = Threads.startNewThread("Keeper-<" + cfg.getCfgInfo() + ">-Cleanup-Thread",
				this::cleanupFunc);
	}

	/**
	 * 定时运行的线程
	 */
	private void cleanupFunc() {
		System.out.println("启动清理线程");
		do {
			try {
				Thread.sleep(expireMillis);
				System.out.println("执行清理.......");
				Set<String> filenames = lastUsedLog.keySet();
				long now = EpochTime.millis();
				for (String filename : filenames) {
					long lastUsed = lastUsedLog.get(filename);
					System.out.println("文件 -> " + filename + "最后使用时间 -> " + lastUsed);
					if (now > lastUsed) {
						File savedFile = new File(savePath, filename);
						System.out.println("删除文件 -> " + filename);
						if (savedFile.exists()) {
							if (savedFile.delete()) {
								System.out.println("删除成功");
								lastUsedLog.remove(filename);
							} else {
								System.out.println("删除失败");
							}
						}
					}
				}
			} catch (InterruptedException e) {

			}
		} while (!isClosed);
	}

	private final Thread cleanupThread;

	@Override
	public void close() throws IOException {
		super.close();
		try {
			cleanupThread.interrupt();
		} catch (SecurityException e) {
			// TODO log ignored
		}
	}

	@Override
	public ChronicleMap<K, V> acquire(String filename) throws ChronicleIOException {
		ChronicleMap<K, V> acquire = super.acquire(filename);
		// 存储文件名和到期时间
		long expireEpoch = EpochTime.millis() + expireMillis;
		System.out.println("分配新文件 -> " + filename + ", 过期时间为 -> " + expireEpoch);
		lastUsedLog.put(filename, expireEpoch);
		return acquire;
	}

}
