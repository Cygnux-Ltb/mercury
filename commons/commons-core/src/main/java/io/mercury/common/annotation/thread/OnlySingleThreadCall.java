package io.mercury.common.annotation.thread;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
public @interface OnlySingleThreadCall {
}
