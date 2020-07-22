package io.mercury.persistence.chronicle.hash;

import static io.mercury.common.util.StringUtil.fixPath;

import java.io.File;

import javax.annotation.Nonnull;

import io.mercury.common.collections.Capacity;
import io.mercury.common.datetime.DateTimeUtil;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.util.Assertor;

public final class ChronicleSetConfigurator<K> {

	private final Class<K> keyClass;
	private final K averageKey;

	private final boolean recover;
	private final boolean persistent;

	private final long entries;
	private final int actualChunkSize;

	private final String rootPath;
	private final String folder;

	// final save path
	private final File savePath;

	private ChronicleSetConfigurator(Builder<K> builder) {
		this.keyClass = builder.keyClass;
		this.averageKey = builder.averageKey;
		this.recover = builder.recover;
		this.persistent = builder.persistent;
		this.entries = builder.entries;
		this.actualChunkSize = builder.actualChunkSize;
		this.rootPath = builder.rootPath;
		this.folder = builder.folder;
		this.savePath = new File(rootPath + FixedFolder + folder);
	}

	private static final String FixedFolder = "chronicle-set/";

	public static <K> Builder<K> builder(@Nonnull Class<K> keyClass) {
		Assertor.nonNull(keyClass, "keyClass");
		return new Builder<>(keyClass, SysProperties.JAVA_IO_TMPDIR, "auto-create-" + DateTimeUtil.datetimeOfSecond());
	}

	public static <K> Builder<K> builder(@Nonnull Class<K> keyClass, @Nonnull String folder) {
		Assertor.nonNull(keyClass, "keyClass");
		Assertor.nonNull(folder, "folder");
		return new Builder<>(keyClass, SysProperties.JAVA_IO_TMPDIR, folder);
	}

	public static <K> Builder<K> builder(@Nonnull Class<K> keyClass, @Nonnull String rootPath, @Nonnull String folder) {
		Assertor.nonNull(keyClass, "keyClass");
		Assertor.nonNull(rootPath, "rootPath");
		Assertor.nonNull(folder, "folder");
		return new Builder<>(keyClass, rootPath, folder);
	}

	public Class<K> keyClass() {
		return keyClass;
	}

	public K averageKey() {
		return averageKey;
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

	public static class Builder<K> {

		private Class<K> keyClass;
		private String rootPath;
		private String folder;

		private K averageKey;

		private boolean recover = false;
		private boolean persistent = true;

		private long entries = Capacity.L16_SIZE_65536.size();
		private int actualChunkSize;

		private Builder(@Nonnull Class<K> keyClass, @Nonnull String rootPath, @Nonnull String folder) {
			this.keyClass = keyClass;
			this.rootPath = fixPath(rootPath);
			this.folder = fixPath(rootPath);
		}

		public Builder<K> averageKey(K averageKey) {
			this.averageKey = averageKey;
			return this;
		}

		public Builder<K> enableRecover() {
			this.recover = true;
			return this;
		}

		public Builder<K> persistent(boolean persistent) {
			this.persistent = persistent;
			return this;
		}

		public Builder<K> actualChunkSize(int actualChunkSize) {
			this.actualChunkSize = actualChunkSize;
			return this;
		}

		public Builder<K> entries(long entries) {
			this.entries = entries;
			return this;
		}

		public Builder<K> entriesOfPow2(Capacity capacity) {
			this.entries = capacity.size();
			return this;
		}

		public ChronicleSetConfigurator<K> build() {
			return new ChronicleSetConfigurator<>(this);
		}
	}

}
