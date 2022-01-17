package io.mercury.common.util;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nonnull;

public final class ResourceUtil {

	/**
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public static void close(@Nonnull Object obj) throws Exception {
		if (obj != null) {
			if (obj instanceof AutoCloseable)
				close((AutoCloseable) obj);
			if (obj instanceof Closeable)
				close((Closeable) obj);
		}
	}

	/**
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public static void close(@Nonnull AutoCloseable obj) throws Exception {
		if (obj != null)
			obj.close();
	}

	/**
	 * 
	 * @param obj
	 * @throws IOException
	 */
	public static void close(@Nonnull Closeable obj) throws IOException {
		if (obj != null)
			obj.close();
	}

}
