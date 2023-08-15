package io.mercury.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * 表示获取值可能来自缓存
 *
 * @author yellow013
 */
@Documented
@Target({FIELD, METHOD, LOCAL_VARIABLE})
@Retention(SOURCE)
public @interface MaybeWithCache {
}
