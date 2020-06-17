package io.mercury.common.annotation.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.SOURCE)
public @interface ThrowsRuntimeException {

	Class<? extends RuntimeException>[] value() default RuntimeException.class;

}
