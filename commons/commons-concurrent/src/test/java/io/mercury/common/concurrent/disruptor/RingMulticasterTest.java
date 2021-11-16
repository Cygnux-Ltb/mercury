package io.mercury.common.concurrent.disruptor;

import static io.mercury.common.concurrent.disruptor.CommonWaitStrategy.Yielding;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.LongAdder;

import org.junit.Test;

import io.mercury.common.concurrent.disruptor.example.LongEvent;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;

public class RingMulticasterTest {

	@Test
	public void test() {
		var p0 = new LongAdder();
		var p1 = new LongAdder();
		var p2 = new LongAdder();
		var multicaster = RingMulticaster.newBuilder(LongEvent.class, (LongEvent event, Long l) -> {
			event.set(l);
		}).addHandler((event, sequence, endOfBatch) -> {
			System.out.println("sequence -> " + sequence + " p0 - " + event.get() + " : " + endOfBatch);
			p0.increment();
		}).addHandler((event, sequence, endOfBatch) -> {
			System.out.println("sequence -> " + sequence + " p1 - " + event.get() + " : " + endOfBatch);
			p1.increment();
		}).addHandler((event, sequence, endOfBatch) -> {
			System.out.println("sequence -> " + sequence + " p2 - " + event.get() + " : " + endOfBatch);
			p2.increment();
		}).name("Test-Multicaster").setWaitStrategy(Yielding.get()).size(32).create();

		Thread thread = Threads.startNewThread(() -> {
			for (long l = 0L; l < 1000; l++)
				multicaster.publishEvent(l);
		});

		SleepSupport.sleep(2000);

		System.out.println("p0 - " + p0.intValue());
		assertEquals(p0.intValue(), 1000L);

		System.out.println("p1 - " + p1.intValue());
		assertEquals(p1.intValue(), 1000L);

		System.out.println("p2 - " + p2.intValue());
		assertEquals(p2.intValue(), 1000L);

		multicaster.stop();
		thread.interrupt();
	}

}
