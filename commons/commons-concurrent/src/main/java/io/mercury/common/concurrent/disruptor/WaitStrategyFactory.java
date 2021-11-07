package io.mercury.common.concurrent.disruptor;

import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.LiteTimeoutBlockingWaitStrategy;
import com.lmax.disruptor.PhasedBackoffWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;

final class WaitStrategyFactory {

	private WaitStrategyFactory() {
	}

	static WaitStrategy getStrategy(WaitStrategyOption option) {
		switch (option) {
		case BusySpin:
			return new BusySpinWaitStrategy();
		case Blocking:
			return new BlockingWaitStrategy();
		case LiteBlocking:
			return new LiteBlockingWaitStrategy();
		case TimeoutBlocking:
			return new TimeoutBlockingWaitStrategy(10, TimeUnit.MICROSECONDS);
		case LiteTimeoutBlocking:
			return new LiteTimeoutBlockingWaitStrategy(10, TimeUnit.MICROSECONDS);
		case Sleeping:
			return new SleepingWaitStrategy();
		case Yielding:
			return new YieldingWaitStrategy();
		case PhasedBackoff:
			return new PhasedBackoffWaitStrategy(10, 20, TimeUnit.MICROSECONDS, new LiteBlockingWaitStrategy());
		default:
			throw new RuntimeException("option -> (" + option + ") does not support.");
		}
	}

}
