package io.mercury.common.annotation.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识不安全的类型
 * 
 * @author yellow013
 * 
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeUnsafe {
}
