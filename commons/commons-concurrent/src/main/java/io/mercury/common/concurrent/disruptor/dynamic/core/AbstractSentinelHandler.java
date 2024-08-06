package io.mercury.common.concurrent.ring.dynamic.core;

import com.lmax.disruptor.LifecycleAware;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import io.mercury.common.concurrent.ring.dynamic.sentinel.ConsumeStatusInfo;
import io.mercury.common.concurrent.ring.dynamic.sentinel.SentinelClient;
import io.mercury.common.concurrent.ring.dynamic.sentinel.ThreadStatusInfo;

import java.util.concurrent.CountDownLatch;

/**
 * @author : Rookiex
 * @version :
 */
public abstract class AbstractSentinelHandler
        implements WorkHandler<HandlerEvent>, LifecycleAware, ThreadStatusInfo, ConsumeStatusInfo {

    private final SentinelClient sentinelClient;

    public AbstractSentinelHandler(SentinelClient sentinelClient) {
        this.sentinelClient = sentinelClient;
    }

    /**
     * Callback to indicate a unit of work needs to be processed.
     *
     * @param event published to the {@link RingBuffer}
     * @throws Exception if the {@link WorkHandler} would like the exception handled
     *                   further up the chain.
     */
    @Override
    public void onEvent(HandlerEvent event) throws Exception {
        try {
            threadRun();
            deal(event);
        } catch (Exception e) {
            System.out.println("deal transmit err");
        } finally {
            addConsumeCount();
            threadWait();
        }
    }

    /**
     * @param event e
     * @throws Exception e
     */
    public abstract void deal(HandlerEvent event) throws Exception;

    private final CountDownLatch shutdownLatch = new CountDownLatch(1);

    @Override
    public void onStart() {
        threadReady();
    }

    @Override
    public void onShutdown() {
        threadShutdown();
        shutdownLatch.countDown();
    }

    public void awaitShutdown() throws InterruptedException {
        shutdownLatch.await();
    }

    @Override
    public void threadRun() {
        sentinelClient.threadRun();
    }

    @Override
    public void threadWait() {
        sentinelClient.threadWait();
    }

    @Override
    public void threadReady() {
        sentinelClient.threadReady();
    }

    @Override
    public void threadShutdown() {
        sentinelClient.threadShutdown();
    }

    @Override
    public void addConsumeCount() {
        sentinelClient.addConsumeCount();
    }

    @Override
    public void addProduceCount() {

    }
}
