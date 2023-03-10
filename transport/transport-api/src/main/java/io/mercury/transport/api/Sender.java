package io.mercury.transport.api;

public interface Sender<M> extends Transport {

    /**
     * @param msg T
     */
    void sent(M msg);

}
