package io.mercury.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识抽象函数
 * 
 * @author yellow013
 *
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.SOURCE)
public @interface AbstractFunction {
}
