package io.mercury.common.annotation.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.CLASS)
public @interface ThreadSafeVariable {

}
