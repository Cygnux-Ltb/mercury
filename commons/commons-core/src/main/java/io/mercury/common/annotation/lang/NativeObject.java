package io.mercury.common.annotation.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
public @interface NativeObject {

	String[] headFile() default "";

}
