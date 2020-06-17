package io.mercury.persistence.chronicle.hash;

import static io.mercury.common.util.StringUtil.fixPath;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import io.mercury.common.annotation.lang.ThrowsRuntimeException;
import io.mercury.common.collections.Capacity;
import io.mercury.common.config.Configurator;
import io.mercury.common.datetime.DateTimeUtil;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.util.Assertor;

@Immutable
public final class ChronicleMapConfigurator<K, V> implements Configurator {

	private final Class<K> keyClass;
	private final Class<V> valueClass;
	private final K averageKey;
	private final V averageValue;

	private final boolean putReturnsNull;
	private final boolean removeReturnsNull;
	private final boolean recover;
	private final boolean persistent;

	private final long entries;
	private final int actualChunkSize;

	private final String rootPath;
	private final String folder;

	// final save path
	private final File savePath;

	private final String fullInfo;

	// extended use
	private Builder<K, V> builder;

	private ChronicleMapConfigurator(Builder<K, V> builder) {
		this.builder = builder;
		this.keyClass = builder.keyClass;
		this.valueClass = builder.valueClass;
		this.averageKey = builder.averageKey;
		this.averageValue = builder.averageValue;
		this.putReturnsNull = builder.putReturnsNull;
		this.removeReturnsNull = builder.removeReturnsNull;
		this.recover = builder.recover;
		this.persistent = builder.persistent;
		this.entries = builder.entries;
		this.actualChunkSize = builder.actualChunkSize;
		this.rootPath = builder.rootPath;
		this.folder = builder.folder;
		this.savePath = new File(rootPath + FixedFolder + folder);
		this.fullInfo = buildFullInfo();
	}

	private String buildFullInfo() {
		return "[SaveTo->" + savePath.getAbsolutePath() + "]:[Key==" + keyClass.getSimpleName() + ",Value=="
				+ valueClass.getSimpleName() + "]";
	}

	private static final String FixedFolder = "chronicle-map/";

	@ThrowsRuntimeException(NullPointerException.class)
	public static <K, V> Builder<K, V> builder(@Nonnull Class<K> keyClass, @Nonnull Class<V> valueClass)
			throws NullPointerException {
		return new Builder<>(Assertor.nonNull(keyClass, "keyClass"), Assertor.nonNull(valueClass, "valueClass"),
				SysProperties.JAVA_IO_TMPDIR + "/", "auto-create-" + DateTimeUtil.datetimeOfSecond() + "/");
	}

	@ThrowsRuntimeException(NullPointerException.class)
	public static <K, V> Builder<K, V> builder(@Nonnull Class<K> keyClass, @Nonnull Class<V> valueClass, String folder)
			throws NullPointerException {
		return new Builder<>(Assertor.nonNull(keyClass, "keyClass"), Assertor.nonNull(valueClass, "valueClass"),
				SysProperties.JAVA_IO_TMPDIR + "/", Assertor.nonNull(folder, "folder"));
	}

	@ThrowsRuntimeException(NullPointerException.class)
	public static <K, V> Builder<K, V> builder(@Nonnull Class<K> keyClass, @Nonnull Class<V> valueClass,
			String rootPath, String folder) throws NullPointerException {
		return new Builder<>(Assertor.nonNull(keyClass, "keyClass"), Assertor.nonNull(valueClass, "valueClass"),
				Assertor.nonNull(rootPath, "rootPath"), Assertor.nonNull(folder, "folder"));
	}

	@ThrowsRuntimeException(NullPointerException.class)
	public static <K, V> Builder<K, V> reset(@Nonnull ChronicleMapConfigurator<K, V> original)
			throws NullPointerException {
		return Assertor.nonNull(original, "original").builder;
	}

	@Override
	public String fullInfo() {
		return fullInfo;
	}

	public Class<K> keyClass() {
		return keyClass;
	}

	public Class<V> valueClass() {
		return valueClass;
	}

	public K averageKey() {
		return averageKey;
	}

	public V averageValue() {
		return averageValue;
	}

	public boolean putReturnsNull() {
		return putReturnsNull;
	}

	public boolean removeReturnsNull() {
		return removeReturnsNull;
	}

	public boolean recover() {
		return recover;
	}

	public boolean persistent() {
		return persistent;
	}

	public int actualChunkSize() {
		return actualChunkSize;
	}

	public long entries() {
		return entries;
	}

	public String rootPath() {
		return rootPath;
	}

	public String folder() {
		return folder;
	}

	public File savePath() {
		return savePath;
	}

	public static class Builder<K, V> {

		private Class<K> keyClass;
		private Class<V> valueClass;
		private String rootPath;
		private String folder;

		private K averageKey;
		private V averageValue;

		private boolean putReturnsNull = false;
		private boolean removeReturnsNull = false;
		private boolean recover = false;
		private boolean persistent = true;

		private long entries = Capacity.L16_SIZE_65536.size();
		private int actualChunkSize;

		private Builder(Class<K> keyClass, Class<V> valueClass, String rootPath, String folder) {
			this.keyClass = keyClass;
			this.valueClass = valueClass;
			this.rootPath = fixPath(rootPath);
			this.folder = fixPath(folder);
		}

		public Builder<K, V> averageKey(K averageKey) {
			this.averageKey = averageKey;
			return this;
		}

		public Builder<K, V> averageValue(V averageValue) {
			this.averageValue = averageValue;
			return this;
		}

		public Builder<K, V> putReturnsNull(boolean putReturnsNull) {
			this.putReturnsNull = putReturnsNull;
			return this;
		}

		public Builder<K, V> removeReturnsNull(boolean removeReturnsNull) {
			this.removeReturnsNull = removeReturnsNull;
			return this;
		}

		public Builder<K, V> recover(boolean recover) {
			this.recover = recover;
			return this;
		}

		public Builder<K, V> persistent(boolean persistent) {
			this.persistent = persistent;
			return this;
		}

		public Builder<K, V> actualChunkSize(int actualChunkSize) {
			this.actualChunkSize = actualChunkSize;
			return this;
		}

		public Builder<K, V> entries(long entries) {
			this.entries = entries;
			return this;
		}

		public Builder<K, V> entriesOfPow2(Capacity capacity) {
			this.entries = capacity.size();
			return this;
		}

		public ChronicleMapConfigurator<K, V> build() {
			return new ChronicleMapConfigurator<>(this);
		}
	}

}
