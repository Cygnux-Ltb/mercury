package io.mercury.common.disruptor.dynamic.example;

import io.mercury.common.disruptor.dynamic.core.AbstractSentinelHandler;
import io.mercury.common.disruptor.dynamic.core.HandlerFactory;
import io.mercury.common.disruptor.dynamic.sentinel.SentinelClient;

/**
 * @Author : Rookiex
 * @Date : Created in 2019/11/11 17:28
 * @Describe :
 * @version:
 */
public class ExampleHandlerFactory implements HandlerFactory {

	private SentinelClient sentinelClient;

	@Override
	public AbstractSentinelHandler createHandler() {
		return new ExampleSentinelHandler(sentinelClient);
	}

	@Override
	public void setSentinelClient(SentinelClient sentinelClient) {
		this.sentinelClient = sentinelClient;
	}

}
