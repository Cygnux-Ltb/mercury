package io.mercury.common.concurrent.disruptor;

public enum WaitStrategyOption {

	BusySpin,

	Blocking,

	LiteBlocking,

	TimeoutBlocking,

	LiteTimeoutBlocking,

	PhasedBackoff,

	Sleeping,

	Yielding,

	;

}
