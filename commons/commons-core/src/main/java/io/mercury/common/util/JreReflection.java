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
	 * @param type
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <R, T> R extractField(Class<T> type, T obj, String fieldName) {
		try {
			var field = getField(type, fieldName);
			field.setAccessible(true);
			return (R) field.get(obj);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException("Can not access field: " + e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param type
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Field getField(Class<?> type, String fieldName) throws NoSuchFieldException {
		try {
			return type.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			var superType = type.getSuperclass();
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
	 * @throws RuntimeReflectionException
	 */
	public static <T> T invokeConstructor(Class<T> type) throws RuntimeReflectionException {
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
