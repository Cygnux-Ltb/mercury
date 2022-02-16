package io.mercury.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 抽象函数标识
 * 
 * @author yellow013
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.SOURCE)
public @interface AbstractFunction {
}
