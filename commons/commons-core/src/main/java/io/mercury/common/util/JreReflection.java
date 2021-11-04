package io.mercury.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.reflect.ConstructorUtils;

/**
 * 
 * @author yellow013
 *
 */
public final class JreReflection {

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
	public static <R, T> R extractField(Class<T> type, T t, String fieldName) {
		try {
			final Field field = getField(type, fieldName);
			field.setAccessible(true);
			return (R) field.get(t);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException("Can not access field: " + e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Field getField(Class<?> type, String fieldName) throws NoSuchFieldException {
		try {
			return type.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class<?> superType = type.getSuperclass();
			if (superType == null) {
				throw e;
			} else {
				return getField(superType, fieldName);
			}
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param type
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static <T> T invokeConstructor(Class<T> type) {
		try {
			return ConstructorUtils.invokeConstructor(type, new Object[] {});
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
				| InstantiationException e) {
			throw new RuntimeReflectionException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param type
	 * @param args
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static <T> T invokeConstructor(Class<T> type, Object... args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		try {
			return ConstructorUtils.invokeConstructor(type, args);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
				| InstantiationException e) {
			throw new RuntimeReflectionException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static class RuntimeReflectionException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8452094826323264342L;

		public RuntimeReflectionException(String msg, Throwable throwable) {
			super(msg, throwable);
		}

	}

}
