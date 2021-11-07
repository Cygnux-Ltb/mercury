package io.mercury.common.disruptor;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.LongAdder;

import org.junit.Test;

import io.mercury.common.concurrent.disruptor.RingMulticaster;
import io.mercury.common.concurrent.disruptor.WaitStrategyOption;
import io.mercury.common.concurrent.disruptor.example.LongEvent;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;
import io.mercury.common.thread.RunnableComponent.StartMode;

public class RingMulticasterTest {

	@Test
	public void test() {
		var p0 = new LongAdder();
		var p1 = new LongAdder();
		var p2 = new LongAdder();
		var multicaster = new RingMulticaster<LongEvent, Long>("Test-Multicaster", LongEvent.class, 32,
				WaitStrategyOption.LiteBlocking, StartMode.Auto, (LongEvent t, Long l) -> {
					return t.set(l);
				}, event -> {
					p0.increment();
				}, event -> {
					p1.increment();
				}, event -> {
					p2.increment();
				});
		Thread thread = Threads.startNewThread(() -> {
			for (long l = 0L; l < 1000; l++)
				multicaster.publishEvent(l);
		});

		SleepSupport.sleep(2000);

		System.out.println(p0.intValue());
		assertEquals(p0.intValue(), 1000L);
		
		System.out.println(p1.intValue());
		assertEquals(p1.intValue(), 1000L);
		
		System.out.println(p2.intValue());
		assertEquals(p2.intValue(), 1000L);
		
		multicaster.stop();
		thread.interrupt();
	}

}
