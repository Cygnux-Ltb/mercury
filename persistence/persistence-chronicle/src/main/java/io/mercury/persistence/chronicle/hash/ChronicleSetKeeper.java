package io.mercury.persistence.chronicle.hash;

import io.mercury.common.collections.keeper.FilesKeeper;
import io.mercury.common.file.PermissionDeniedException;
import io.mercury.common.lang.Asserter;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.set.ChronicleSet;
import net.openhft.chronicle.set.ChronicleSetBuilder;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.io.File;
import java.io.IOException;

@ThreadSafe
public class ChronicleSetKeeper<E> extends FilesKeeper<String, ChronicleSet<E>> {

    private final ChronicleSetConfigurator<E> cfg;

    public ChronicleSetKeeper(@Nonnull ChronicleSetConfigurator<E> cfg) {
        Asserter.nonNull(cfg, "cfg");
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
            File mapFile = new File(cfg.getSavePath(), filename);
            try {
                if (!mapFile.exists()) {
                    // 创建文件目录
                    createFile(mapFile);
                    return builder.createPersistedTo(mapFile);
                } else {
                    // Is recover data
                    if (cfg.isRecover())
                        return builder.createOrRecoverPersistedTo(mapFile);
                    else
                        return builder.createPersistedTo(mapFile);
                }
            } catch (IOException | PermissionDeniedException e) {
                throw new ChronicleIOException(e);
            }
        } else
            return builder.create();
    }

    @Override
    public void close() throws IOException {

    }

}
