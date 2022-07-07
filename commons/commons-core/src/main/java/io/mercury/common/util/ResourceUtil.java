package io.mercury.common.util;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;

public final class ResourceUtil {

    private ResourceUtil() {
    }

    /**
     * @param obj Object
     * @throws Exception
     */
    public static <O> void close(@Nullable O obj) throws Exception {
        if (obj != null) {
            if (obj instanceof AutoCloseable)
                close((AutoCloseable) obj);
            if (obj instanceof Closeable)
                close((Closeable) obj);
        }
    }

    /**
     * @param closeable AutoCloseable
     * @throws Exception
     */
    public static void close(@Nullable AutoCloseable closeable) throws Exception {
        if (closeable != null)
            closeable.close();
    }

    /**
     * @param closeable Closeable
     * @throws IOException
     */
    public static void close(@Nullable Closeable closeable) throws IOException {
        if (closeable != null)
            closeable.close();
    }

}
