package io.mercury.common.concurrent.disruptor;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.LongAdder;

import org.junit.Test;

import io.mercury.common.concurrent.disruptor.example.LongEvent;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;
import io.mercury.common.thread.RunnableComponent.StartMode;

public class RingProcessChainTest {

	@Test
	public void test() {
		var p0 = new LongAdder();
		var p1 = new LongAdder();
		var p2 = new LongAdder();
		var processChain = new RingProcessChain<LongEvent, Long>("Test-Multicaster", 32, LongEvent.class,
				WaitStrategyOption.LiteBlocking, StartMode.Auto, (LongEvent t, Long l) -> {
					t.set(l);
				}, event -> {
					System.out.println("p0 - " + event.get());
					p0.increment();
				}, event -> {
					System.out.println("p1 - " + event.get());
					p1.increment();
				}, event -> {
					System.out.println("p2 - " + event.get());
					p2.increment();
				});
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
