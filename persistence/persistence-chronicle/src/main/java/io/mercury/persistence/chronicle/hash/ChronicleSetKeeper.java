package io.mercury.persistence.chronicle.hash;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import io.mercury.common.collections.customize.BaseKeeper;
import io.mercury.common.util.Assertor;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.set.ChronicleSet;
import net.openhft.chronicle.set.ChronicleSetBuilder;

@ThreadSafe
public class ChronicleSetKeeper<K> extends BaseKeeper<String, ChronicleSet<K>> {

	private ChronicleSetConfigurator<K> configurator;

	public ChronicleSetKeeper(@Nonnull ChronicleSetConfigurator<K> configurator) {
		this.configurator = Assertor.nonNull(configurator, "configurator");
	}

	@Override
	public ChronicleSet<K> acquire(String filename) throws ChronicleIOException {
		return super.acquire(filename);
	}

	@Override
	protected ChronicleSet<K> createWithKey(String filename) {
		ChronicleSetBuilder<K> builder = ChronicleSetBuilder.of(configurator.keyClass())
				.entries(configurator.entries());
		if (configurator.actualChunkSize() > 0)
			builder.actualChunkSize(configurator.actualChunkSize());
		if (configurator.averageKey() != null)
			builder.averageKey(configurator.averageKey());
		if (configurator.persistent()) {
			File persistedFile = new File(configurator.savePath(), filename);
			try {
				if (!persistedFile.exists()) {
					File parentFile = persistedFile.getParentFile();
					if (!parentFile.exists())
						parentFile.mkdirs();
					return builder.createPersistedTo(persistedFile);
				} else {
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

}
