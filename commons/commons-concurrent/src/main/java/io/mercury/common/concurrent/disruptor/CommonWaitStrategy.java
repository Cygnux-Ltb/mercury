package io.mercury.common.concurrent.disruptor;

import java.util.function.Supplier;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;

public enum CommonWaitStrategy implements Supplier<WaitStrategy> {

	/**
	 * Internally the BlockingWaitStrategy uses a typical lock and condition
	 * variable to handle thread wake-up. The BlockingWaitStrategy is the slowest of
	 * the available wait strategies, but is the most conservative with the respect
	 * to CPU usage and will give the most consistent behaviour across the widest
	 * variety of deployment options.
	 */
	Blocking,

	/**
	 * 
	 * The SleepingWaitStrategy it attempts to be conservative with CPU usage by
	 * using a simple busy wait loop. The difference is that the
	 * SleepingWaitStrategy uses a call to LockSupport.parkNanos(1) in the middle of
	 * the loop. On a typical Linux system this will pause the thread for around
	 * 60µs. <br>
	 * <br>
	 * This has the benefits that the producing thread does not need to take any
	 * action other increment the appropriate counter and that it does not require
	 * the cost of signalling a condition variable. However, the mean latency of
	 * moving the event between the producer and consumer threads will be higher.
	 * <br>
	 * <br>
	 * It works best in situations where low latency is not required, but a low
	 * impact on the producing thread is desired. A common use case is for
	 * asynchronous logging.
	 */
	Sleeping,

	/**
	 * The YieldingWaitStrategy is one of two WaitStrategys that can be use in
	 * low-latency systems. It is designed for cases where there is the option to
	 * burn CPU cycles with the goal of improving latency. <br>
	 * <br>
	 * The YieldingWaitStrategy will busy spin, waiting for the sequence to
	 * increment to the appropriate value. Inside the body of the loop
	 * Thread#yield() will be called allowing other queued threads to run. <br>
	 * <br>
	 * This is the recommended wait strategy when you need very high performance,
	 * and the number of EventHandler threads is lower than the total number of
	 * logical cores, e.g. you have hyper-threading enabled.
	 */
	Yielding,

	/**
	 * The BusySpinWaitStrategy is the highest performing WaitStrategy. Like the
	 * YieldingWaitStrategy, it can be used in low-latency systems, but puts the
	 * highest constraints on the deployment environment. <br>
	 * <br>
	 * This wait strategy should only be used if the number of EventHandler threads
	 * is lower than the number of physical cores on the box, e.g. hyper-threading
	 * should be disabled.
	 */
	BusySpin,

	;

	@Override
	public WaitStrategy get() {
		return switch (this) {
			case BusySpin -> new BusySpinWaitStrategy();
			case Blocking -> new BlockingWaitStrategy();
			case Yielding -> new YieldingWaitStrategy();
			default -> new SleepingWaitStrategy();
		};
	}

}
