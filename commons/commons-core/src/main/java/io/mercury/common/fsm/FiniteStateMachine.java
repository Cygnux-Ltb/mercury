package io.mercury.common.fsm;

public interface FiniteStateMachine {

	State getState();

	State doAction(Action action);

}
