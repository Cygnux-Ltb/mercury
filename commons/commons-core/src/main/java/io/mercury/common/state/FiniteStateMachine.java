package io.mercury.common.state;

public interface FiniteStateMachine {

	State getState();

	State handleSignal(Signal signal);

}
