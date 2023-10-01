package io.mercury.transport.socket;

import io.mercury.transport.api.Receiver;
import io.mercury.transport.api.Sender;

public interface Transceiver<T> extends Receiver {

	Sender<T> getSender();

	boolean startSend();

}
