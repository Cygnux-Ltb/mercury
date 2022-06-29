package io.mercury.common.util;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;

public final class ResourceUtil {

    private ResourceUtil() {
    }

    /**
     * @param o
     * @throws Exception
     */
    public static <O> void close(@Nullable O o) throws Exception {
        if (o != null) {
            if (o instanceof AutoCloseable)
                close((AutoCloseable) o);
            if (o instanceof Closeable)
                close((Closeable) o);
        }
    }

    /**
     * @param closeable
     * @throws Exception
     */
    public static void close(@Nullable AutoCloseable closeable) throws Exception {
        if (closeable != null)
            closeable.close();
    }

    /**
     * @param closeable
     * @throws IOException
     */
    public static void close(@Nullable Closeable closeable) throws IOException {
        if (closeable != null)
            closeable.close();
    }

}
