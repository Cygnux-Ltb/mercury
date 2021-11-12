package io.mercury.common.concurrent.disruptor;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.LongAdder;

import org.junit.Test;

import io.mercury.common.concurrent.disruptor.example.LongEvent;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;

public class RingProcessChainTest {

	@Test
	public void test() {
		var p0 = new LongAdder();
		var p1 = new LongAdder();
		var p2 = new LongAdder();
		var processChain = RingProcessChain.newBuilder(LongEvent.class, (LongEvent t, Long l) -> {
			t.set(l);
		}).setFirstHandler((event, sequence, endOfBatch) -> {
			System.out.println("sequence -> " + sequence + " p0 - " + event.get() + " : " + endOfBatch);
			p0.increment();
		}).setHandler(1, (event, sequence, endOfBatch) -> {
			System.out.println("sequence -> " + sequence + " p1 - " + event.get() + " : " + endOfBatch);
			p1.increment();
		}).setHandler(2, (event, sequence, endOfBatch) -> {
			System.out.println("sequence -> " + sequence + " p2 - " + event.get() + " : " + endOfBatch);
			p2.increment();
		}).name("Test-RingProcessChain").size(32).setWaitStrategy(WaitStrategyOption.LiteBlocking).create();

		Thread thread = Threads.startNewThread(() -> {
			for (long l = 0L; l < 1000; l++)
				processChain.publishEvent(l);
		});

		SleepSupport.sleep(2000);

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
