package io.mercury.common.fsm;

public interface FiniteStateMachine<S extends State, A extends Action> {

	S state();

	S action(A action);

}
