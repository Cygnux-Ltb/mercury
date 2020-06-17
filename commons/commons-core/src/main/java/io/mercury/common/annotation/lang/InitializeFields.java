package io.mercury.common.annotation.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface InitializeFields {

	Class<?>[] value();

}
