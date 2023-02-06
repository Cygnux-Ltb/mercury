package io.mercury.transport.rmq.annotation;

public @interface RmqConsume {

    String queueName() default "";

    boolean autoAck() default true;

    String consumerTag() default "";

    boolean exclusive() default false;

}
