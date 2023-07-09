package io.mercury.transport.zmq.annotation;

import io.mercury.transport.zmq.ZmqConfigurator.ZmqProtocol;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZmqSubscribe {

    ZmqProtocol protocol();

    String addr();

    boolean handleTopic() default true;

    String[] topic() default {""};

}
