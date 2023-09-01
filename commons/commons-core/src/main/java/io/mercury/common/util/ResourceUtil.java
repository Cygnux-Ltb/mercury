package io.mercury.common.util;

import io.mercury.common.log4j2.Log4j2LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;

public final class ResourceUtil {

    private static final Logger log = Log4j2LoggerFactory.getLogger(ResourceUtil.class);

    private ResourceUtil() {
    }

    /**
     * @param obj Object
     * @throws Exception e
     */
    public static <O> void close(@Nullable O obj) throws Exception {
        if (obj != null) {
            if (obj instanceof AutoCloseable)
                close((AutoCloseable) obj);
            if (obj instanceof Closeable)
                close((Closeable) obj);
        } else
            log.warn("The object to be closed is NULL");
    }

    /**
     * @param closeable AutoCloseable
     * @throws Exception e
     */
    public static void close(@Nullable AutoCloseable closeable) throws Exception {
        if (closeable != null)
            closeable.close();
    }

    /**
     * @param closeable Closeable
     * @throws IOException ioe
     */
    public static void close(@Nullable Closeable closeable) throws IOException {
        if (closeable != null)
            closeable.close();
    }

    /**
     * @param obj O
     */
    public static <O> void closeIgnoreException(@Nullable O obj) {
        if (obj != null)
            try {
                close(obj);
            } catch (Exception e) {
                log.error("Close object -> [{}] throw {}", obj.getClass().getName(),
                        e.getClass().getName(), e);
            }
        else
            log.warn("The object to be closed is NULL");
    }


    /**
     * @param objs O array
     */
    @SafeVarargs
    public static <O> void closeIgnoreException(@Nullable O... objs) {
        if (objs != null) {
            for (O obj : objs)
                try {
                    close(obj);
                } catch (Exception e) {
                    log.error("Close object -> [{}] throw {}", obj.getClass().getName(),
                            e.getClass().getName(), e);
                }
        } else {
            log.warn("The objects to be closed is NULL");
        }
    }

}
