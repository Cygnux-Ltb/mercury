package io.mercury.persistence.chronicle.hash;

import static io.mercury.common.util.StringUtil.fixPath;

import java.io.File;

import javax.annotation.Nonnull;

import io.mercury.common.collections.Capacity;
import io.mercury.common.config.Configurator;
import io.mercury.common.datetime.DateTimeUtil;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.util.Assertor;
import lombok.Getter;

public final class ChronicleSetConfigurator<E> implements Configurator {

	@Getter
	private final Class<E> elementClass;
	@Getter
	private final E averageElement;

	@Getter
	private final boolean recover;
	@Getter
	private final boolean persistent;

	@Getter
	private final long entries;
	@Getter
	private final int actualChunkSize;

	@Getter
	private final String rootPath;
	@Getter
	private final String folder;

	// final save path
	@Getter
	private final File savePath;

	private final String cfgInfo;

	private ChronicleSetConfigurator(Builder<E> builder) {
		this.elementClass = builder.elementClass;
		this.averageElement = builder.averageElement;
		this.recover = builder.recover;
		this.persistent = builder.persistent;
		this.entries = builder.entries;
		this.actualChunkSize = builder.actualChunkSize;
		this.rootPath = builder.rootPath;
		this.folder = builder.folder;
		this.savePath = new File(rootPath + FixedFolder + folder);
		this.cfgInfo = "[SaveTo->" + savePath.getAbsolutePath() + "]:[ElementType==" + elementClass.getSimpleName()
				+ "]";
	}

	private static final String FixedFolder = "chronicle-set/";

	public static <E> Builder<E> builder(@Nonnull Class<E> keyClass) {
		Assertor.nonNull(keyClass, "keyClass");
		return new Builder<>(keyClass, SysProperties.JAVA_IO_TMPDIR, "auto-create-" + DateTimeUtil.datetimeOfSecond());
	}

	public static <E> Builder<E> builder(@Nonnull Class<E> keyClass, @Nonnull String folder) {
		Assertor.nonNull(keyClass, "keyClass");
		Assertor.nonNull(folder, "folder");
		return new Builder<>(keyClass, SysProperties.JAVA_IO_TMPDIR, folder);
	}

	public static <E> Builder<E> builder(@Nonnull Class<E> keyClass, @Nonnull String rootPath, @Nonnull String folder) {
		Assertor.nonNull(keyClass, "keyClass");
		Assertor.nonNull(rootPath, "rootPath");
		Assertor.nonNull(folder, "folder");
		return new Builder<>(keyClass, rootPath, folder);
	}

	@Override
	public String getCfgInfo() {
		return cfgInfo;
	}

	/**
	 * 
	 * @author yellow013
	 *
	 * @param <E>
	 */
	public static class Builder<E> {

		private Class<E> elementClass;
		private String rootPath;
		private String folder;

		private E averageElement;

		private boolean recover = false;
		private boolean persistent = true;

		private long entries = Capacity.L16_SIZE.value();
		private int actualChunkSize;

		private Builder(@Nonnull Class<E> elementClass, @Nonnull String rootPath, @Nonnull String folder) {
			this.elementClass = elementClass;
			this.rootPath = fixPath(rootPath);
			this.folder = fixPath(rootPath);
		}

		public Builder<E> averageElement(E averageElement) {
			this.averageElement = averageElement;
			return this;
		}

		public Builder<E> enableRecover() {
			this.recover = true;
			return this;
		}

		public Builder<E> persistent(boolean persistent) {
			this.persistent = persistent;
			return this;
		}

		public Builder<E> actualChunkSize(int actualChunkSize) {
			this.actualChunkSize = actualChunkSize;
			return this;
		}

		public Builder<E> entries(long entries) {
			this.entries = entries;
			return this;
		}

		public Builder<E> entriesOfPow2(Capacity capacity) {
			this.entries = capacity.value();
			return this;
		}

		public ChronicleSetConfigurator<E> build() {
			return new ChronicleSetConfigurator<>(this);
		}
	}

}
