package io.mercury.transport.socket;

import io.mercury.transport.core.api.Receiver;
import io.mercury.transport.core.api.Sender;

public interface Transceiver<T> extends Receiver {

	Sender<T> getInnerSender();

	void startSend();

}
