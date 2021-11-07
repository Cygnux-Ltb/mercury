package io.mercury.common.disruptor;

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
