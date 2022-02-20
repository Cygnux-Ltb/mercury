package io.mercury.persistence.chronicle.hash;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import io.mercury.common.collections.keeper.AbstractKeeper;
import io.mercury.common.lang.Assertor;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.set.ChronicleSet;
import net.openhft.chronicle.set.ChronicleSetBuilder;

@ThreadSafe
public class ChronicleSetKeeper<E> extends AbstractKeeper<String, ChronicleSet<E>> {

	private ChronicleSetConfigurator<E> cfg;

	public ChronicleSetKeeper(@Nonnull ChronicleSetConfigurator<E> cfg) {
		Assertor.nonNull(cfg, "cfg");
		this.cfg = cfg;
	}

	@Nonnull
	@Override
	public ChronicleSet<E> acquire(@Nonnull String filename) throws ChronicleIOException {
		return super.acquire(filename);
	}

	@Override
	protected ChronicleSet<E> createWithKey(String filename) {
		ChronicleSetBuilder<E> builder = ChronicleSetBuilder.of(cfg.getElementClass()).entries(cfg.getEntries());
		if (cfg.getActualChunkSize() > 0)
			builder.actualChunkSize(cfg.getActualChunkSize());
		if (cfg.getAverageElement() != null)
			builder.averageKey(cfg.getAverageElement());
		if (cfg.isPersistent()) {
			File persistedFile = new File(cfg.getSavePath(), filename);
			try {
				if (!persistedFile.exists()) {
					File parentFile = persistedFile.getParentFile();
					if (!parentFile.exists())
						parentFile.mkdirs();
					return builder.createPersistedTo(persistedFile);
				} else {
					if (cfg.isRecover())
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
