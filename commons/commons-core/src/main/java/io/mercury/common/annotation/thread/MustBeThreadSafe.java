package io.mercury.common.annotation.thread;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Nonnull
public @interface MustBeThreadSafe {
}
