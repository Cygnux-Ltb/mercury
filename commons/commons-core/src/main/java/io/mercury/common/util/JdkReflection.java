package io.mercury.common.util;

import java.lang.reflect.Field;

/**
 * 
 * @author yellow013
 *
 */
public final class JdkReflection {

	/**
	 * 
	 * @param <R>
	 * @param <T>
	 * @param clazz
	 * @param object
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <R, T> R extractField(Class<T> clazz, T object, String fieldName) {
		try {
			final Field field = getField(clazz, fieldName);
			field.setAccessible(true);
			return (R) field.get(object);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException("Can not access Disruptor internals: ", e);
		}
	}

	/**
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				throw e;
			} else {
				return getField(superClass, fieldName);
			}
		}
	}

}
