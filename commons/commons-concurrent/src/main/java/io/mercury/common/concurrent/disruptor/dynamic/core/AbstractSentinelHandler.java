package io.mercury.common.concurrent.disruptor.dynamic.core;

import com.lmax.disruptor.LifecycleAware;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.ConsumeStatusInfo;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelClient;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.ThreadStatusInfo;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.CountDownLatch;

/**
 * @author : Rookiex
 * @version :
 */
public abstract class AbstractSentinelHandler
        implements WorkHandler<HandlerEvent>, LifecycleAware, ThreadStatusInfo, ConsumeStatusInfo {

    private static Logger log = Log4j2LoggerFactory.getLogger(AbstractSentinelHandler.class);

    private final SentinelClient sentinelClient;

    protected AbstractSentinelHandler(SentinelClient sentinelClient) {
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
            log.error("deal transmit err: {}", e.getMessage(), e);
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
