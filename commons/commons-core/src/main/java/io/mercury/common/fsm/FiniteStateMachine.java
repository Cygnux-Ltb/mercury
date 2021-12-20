package io.mercury.common.fsm;

public interface FiniteStateMachine {

	State getState();

	State handleSignal(Signal signal);

}
