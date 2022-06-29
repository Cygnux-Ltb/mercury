package io.mercury.common.util;

import java.io.Serial;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.reflect.ConstructorUtils;

/**
 * @author yellow013
 */
public final class JreReflection {

    private JreReflection() {
    }

    /**
     * @param <R>
     * @param <T>
     * @param clazz
     * @param fieldName
     * @return
     * @throws RuntimeReflectionException
     */
    public static <R, T> R extractField(Class<T> clazz, String fieldName) throws RuntimeReflectionException {
        return extractField(clazz, null, fieldName);
    }

    /**
     * @param <R>
     * @param <T>
     * @param clazz
     * @param obj
     * @param fieldName
     * @return
     * @throws RuntimeReflectionException
     */
    @SuppressWarnings("unchecked")
    public static <R, T> R extractField(Class<T> clazz, T obj, String fieldName) throws RuntimeReflectionException {
        try {
            Field field = getField(clazz, fieldName);
            field.setAccessible(true);
            return (R) field.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeReflectionException(
                    "Can not access field: [" + fieldName + "] be caused by -> " + e.getMessage(), e);
        } catch (RuntimeReflectionException e) {
            throw e;
        }
    }

    /**
     * @param clazz
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null)
                throw new RuntimeReflectionException("Can not find field: [" + fieldName + "]", e);
            else
                return getField(superClass, fieldName);
        }
    }

    /**
     * @param <T>
     * @param type
     * @return
     * @throws RuntimeReflectionException
     */
    public static <T> T invokeConstructor(Class<T> type) throws RuntimeReflectionException {
        return invokeConstructor(type, new Object[]{});
    }

    /**
     * @param <T>
     * @param type
     * @param args
     * @return
     * @throws RuntimeReflectionException
     */
    public static <T> T invokeConstructor(Class<T> type, Object... args) throws RuntimeReflectionException {
        try {
            return ConstructorUtils.invokeConstructor(type, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
                 | InstantiationException e) {
            throw new RuntimeReflectionException(
                    "Can not invoke constructor with class [" + type.getName() + "] be caused by -> " + e.getMessage(),
                    e);
        }
    }

    /**
     * @author yellow013
     */
    public static class RuntimeReflectionException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = -8452094826323264342L;

        public RuntimeReflectionException(String msg, Throwable cause) {
            super(msg, cause);
        }

    }

}
