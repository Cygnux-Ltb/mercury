package io.mercury.common.di.annotation;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import java.lang.annotation.Documented;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Documented
@Order(HIGHEST_PRECEDENCE)
public @interface TopPriorityComponent {

}
