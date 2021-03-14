package io.mercury.common.disruptor.dynamic.core;

import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.LifecycleAware;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;

import io.mercury.common.disruptor.dynamic.sentinel.ConsumeStatusInfo;
import io.mercury.common.disruptor.dynamic.sentinel.SentinelClient;
import io.mercury.common.disruptor.dynamic.sentinel.ThreadStatusInfo;

/**
 * @Author : Rookiex
 * @Date : Created in 2019/11/8 11:20
 * @Describe :
 * @version:
 */
public abstract class AbstractSentinelHandler
		implements WorkHandler<HandlerEvent>, LifecycleAware, ThreadStatusInfo, ConsumeStatusInfo {
	private SentinelClient sentinelClient;

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
			System.out.println("deal transmit err ");
		} finally {
			addConsumeCount();
			threadWait();
		}
	}

	/**
	 * @param event e
	 * @throws Exception
	 *
	 */
	public abstract void deal(HandlerEvent event) throws Exception;

	private CountDownLatch shutdownLatch = new CountDownLatch(1);

	@Override
	public void onStart() {
		threadReady();
	}

	@Override
	public void onShutdown() {
		threadShutDown();
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
	public void threadShutDown() {
		sentinelClient.threadShutDown();
	}

	@Override
	public void addConsumeCount() {
		sentinelClient.addConsumeCount();
	}

	@Override
	public void addProduceCount() {

	}
}
