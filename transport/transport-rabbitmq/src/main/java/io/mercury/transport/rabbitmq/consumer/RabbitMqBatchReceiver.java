package io.mercury.transport.rabbitmq.consumer;

import java.io.IOException;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import io.mercury.common.functional.BytesDeserializer;
import io.mercury.transport.core.api.Receiver;
import io.mercury.transport.rabbitmq.AbstractRabbitMqTransport;
import io.mercury.transport.rabbitmq.configurator.RmqReceiverConfigurator;

/**
 * @author yellow013
 * @date 2019.03.15
 * 
 */
public class RabbitMqBatchReceiver<T> extends AbstractRabbitMqTransport implements Receiver, Runnable {

	private String receiverName;

	private String receiveQueue;

	// 队列持久化
	private boolean durable = true;
	// 连接独占此队列
	private boolean exclusive = false;
	// channel关闭后自动删除队列
	private boolean autoDelete = false;

	private BatchProcessConsumer<T> consumer;

	public RabbitMqBatchReceiver(String tag, @Nonnull RmqReceiverConfigurator configurator, long autoFlushInterval,
			BytesDeserializer<T> deserializer, BatchHandler<T> batchHandler, RefreshNowEvent<T> refreshNowEvent,
			Predicate<T> filter) {
		super(tag, "QosBatchReceiver", configurator.connection());
		this.receiveQueue = configurator.receiveQueue().queue().name();
		createConnection();
		queueDeclare();
		consumer = new BatchProcessConsumer<T>(super.channel, configurator.qos(), autoFlushInterval, batchHandler,
				deserializer, refreshNowEvent, filter);
	}

	public RabbitMqBatchReceiver(String tag, @Nonnull RmqReceiverConfigurator configurator, long autoFlushInterval,
			BytesDeserializer<T> deserializer, BatchHandler<T> batchHandler, RefreshNowEvent<T> refreshNowEvent) {
		super(tag, "QosBatchReceiver", configurator.connection());
		this.receiveQueue = configurator.receiveQueue().queue().name();
		createConnection();
		queueDeclare();
		consumer = new BatchProcessConsumer<T>(channel, configurator.qos(), autoFlushInterval, batchHandler,
				deserializer, refreshNowEvent, null);
	}

	private void queueDeclare() {
		this.receiverName = "receiver::" + rmqConnection.fullInfo() + "$" + receiveQueue;
		try {
			channel.queueDeclare(receiveQueue, durable, exclusive, autoDelete, null);
		} catch (IOException e) {
			log.error(
					"Method channel.queueDeclare(queue==[{}], durable==[{]}, exclusive==[{}], autoDelete==[{}], arguments==null) IOException message -> {}",
					receiveQueue, durable, exclusive, autoDelete, e.getMessage(), e);
			destroy();
		}
	}

	@Override
	public void run() {
		receive();
	}

	@Override
	public void receive() {
		try {
			channel.basicConsume(receiveQueue, false, tag, consumer);
		} catch (IOException e) {
			log.error("basicConsume error", e.getMessage(), e);
		}
	}

	@Override
	public String name() {
		return receiverName;
	}

	@Override
	public boolean destroy() {
		log.info("Call method RabbitMqReceiver.destroy()");
		closeConnection();
		return true;
	}

	@Override
	public void reconnect() {
		// TODO Auto-generated method stub

	}
}
