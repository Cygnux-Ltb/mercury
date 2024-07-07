package io.mercury.common.concurrent.ring;

import com.lmax.disruptor.WorkHandler;
import io.mercury.common.concurrent.ring.base.HandlerManager;
import org.junit.Test;

public class RingEventbusTest {

    @Test
    public void test0() {

        RingEventbus.multiProducer(EventBean.class).process(
                HandlerManager.complexWithFirst((WorkHandler<EventBean>) event -> {

                        }

                ).build()
        );

        new EventBean();
    }

    private static class EventBean {

        private long count;


    }


}