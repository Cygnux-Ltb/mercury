package io.mercury.common.concurrent.disruptor;

import com.lmax.disruptor.support.LongEvent;
import io.mercury.common.thread.Sleep;
import io.mercury.common.thread.Threads;
import org.junit.Test;

import java.util.concurrent.atomic.LongAdder;

import static io.mercury.common.concurrent.disruptor.base.CommonStrategy.Yielding;
import static org.junit.Assert.assertEquals;

public class RingProcessChainTest {

    @Test
    public void test() {
        LongAdder p0 = new LongAdder();
        LongAdder p1 = new LongAdder();
        LongAdder p2 = new LongAdder();
        RingProcessChain<LongEvent, Long> processChain = RingProcessChain
                .singleProducer(LongEvent.class, LongEvent::set)
                .firstHandler((event, sequence, endOfBatch) -> {
                    System.out.println("sequence -> " + sequence + " p0 - " + event.get() + " : " + endOfBatch);
                    p0.increment();
                }).addHandler(1, (event, sequence, endOfBatch) -> {
                    System.out.println("sequence -> " + sequence + " p1 - " + event.get() + " : " + endOfBatch);
                    p1.increment();
                }).addHandler(2, (event, sequence, endOfBatch) -> {
                    System.out.println("sequence -> " + sequence + " p2 - " + event.get() + " : " + endOfBatch);
                    p2.increment();
                }).name("Test-RingProcessChain").size(32).waitStrategy(Yielding.get()).create();

        Thread thread = Threads.startNewThread(() -> {
            for (long l = 0L; l < 1000; l++)
                processChain.publishEvent(l);
        });

        Sleep.millis(2000);

        System.out.println("p0 - " + p0.intValue());
        assertEquals(p0.intValue(), 1000L);

        System.out.println("p1 - " + p1.intValue());
        assertEquals(p1.intValue(), 1000L);

        System.out.println("p2 - " + p2.intValue());
        assertEquals(p2.intValue(), 1000L);

        processChain.stop();
        thread.interrupt();
    }

}
