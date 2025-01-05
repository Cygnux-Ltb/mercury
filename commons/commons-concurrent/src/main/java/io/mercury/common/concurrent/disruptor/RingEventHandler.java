package io.mercury.common.concurrent.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WaitStrategy;

import static io.mercury.common.concurrent.disruptor.base.CommonStrategy.Yielding;

public abstract class RingEventHandler<E> implements EventHandler<E> {

    protected final RingEventbus<E> eventbus;

    protected RingEventHandler(Builder builder, EventFactory<E> factory) {
        if (builder.isSingleProducer) {
            this.eventbus = RingEventbus.singleProducer(factory)
                    .size(builder.size)
                    .name(builder.name)
                    .waitStrategy(builder.waitStrategy)
                    .process(this);
        } else {
            this.eventbus = RingEventbus.multiProducer(factory)
                    .size(builder.size)
                    .name(builder.name)
                    .waitStrategy(builder.waitStrategy)
                    .process(this);
        }
    }

    public static Builder singleProducer() {
        return new Builder(true);
    }

    public static Builder multiProducer() {
        return new Builder(false);
    }

    public static class Builder {

        private final boolean isSingleProducer;
        private String name = "eventbus";
        private int size = 64;
        private WaitStrategy waitStrategy = Yielding.get();

        private Builder(boolean isSingleProducer) {
            this.isSingleProducer = isSingleProducer;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        private Builder waitStrategy(WaitStrategy waitStrategy) {
            this.waitStrategy = waitStrategy;
            return this;
        }

    }

}
