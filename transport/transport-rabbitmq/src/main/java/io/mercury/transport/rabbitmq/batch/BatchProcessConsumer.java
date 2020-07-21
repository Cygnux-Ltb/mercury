package io.mercury.transport.rabbitmq.batch;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import io.mercury.common.collections.MutableLists;
import io.mercury.common.functional.BytesDeserializer;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;

/**
 * @author yellow013
 */
public class BatchProcessConsumer<T> extends DefaultConsumer {

	private Logger log = CommonLoggerFactory.getLogger(getClass());

	private Channel channel;

	private BatchHandler<T> batchHandler;

	private BytesDeserializer<T> deserializer;

	/**
	 * prefetchCount : rabbit consumer最多接受的数量
	 */
	private int prefetchCount;
	/**
	 * seconds: automatic flush check in the specified time
	 */
	private long millisecond = 500L;
	/**
	 * 上一次的buffer -> (bufferList) 大小
	 */
	private long lastSize;

	private LongAdder cacheSize;
	/**
	 * last rabbitmq message sequence
	 */
	private volatile long lastDeliveryTag;

	private ReentrantLock lock;

	private MutableList<T> bufferList;

	private Predicate<T> filter;

	private RefreshNowEvent<T> refreshNowEvent;

	private ScheduledExecutorService schedule;

	/**
	 * constructor
	 *
	 * @param channel          rabbit channel
	 * @param prefetchCount    从rmq获取prefetchCount数量的消息
	 * @param millisSecond     自动flush的时间间隔
	 * @param qosBatchCallBack 当达到prefetchCount值或自动flush触发此回调
	 */
	public BatchProcessConsumer(Channel channel, int prefetchCount, long millisecond, BatchHandler<T> batchHandler,
			BytesDeserializer<T> deserializer, RefreshNowEvent<T> refreshNowEvent, Predicate<T> filter) {
		super(channel);
		this.channel = channel;
		this.refreshNowEvent = refreshNowEvent;
		this.filter = filter;
		this.batchHandler = Assertor.nonNull(batchHandler, "batchHandler");
		this.deserializer = Assertor.nonNull(deserializer, "deserializer");
		this.prefetchCount = prefetchCount;
		if (millisecond > 0) {
			this.millisecond = millisecond;
		} else {
			log.warn("Use default millisecond: {}", this.millisecond);
		}
		init();
	}

	public BatchProcessConsumer(Channel channel, BytesDeserializer<T> serializable, BatchHandler<T> batchHandler) {
		super(channel);
		this.channel = channel;
		this.batchHandler = Assertor.nonNull(batchHandler, "qosBatchHandler");
		this.deserializer = Assertor.nonNull(serializable, "deserializer");
		init();
	}

	private void init() {
		try {
			channel.basicQos(prefetchCount);
		} catch (IOException e) {
			log.error("set prefetchCount failure", e);
			return;
		}
		this.cacheSize = new LongAdder();
		bufferList = MutableLists.newFastList();

		lock = new ReentrantLock();
		ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder()
				.namingPattern("BatchHandlerAutoFlush-pool-%d").build();
		schedule = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
		automaticFlush();
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
		// 序列化
		T t = deserializer.deserialization(body);
		// 过滤器
		if (filter != null && !filter.test(t)) {
			return;
		}
		lock.lock();
		try {
			save(t, envelope.getDeliveryTag());
		} finally {
			lock.unlock();
		}
	}

	private void save(T t, long lastDeliveryTag) {
		this.lastDeliveryTag = lastDeliveryTag;
		if (Objects.nonNull(t)) {
			cacheSize.increment();
			bufferList.add(t);
		}
		if (cacheSize.longValue() >= prefetchCount) {
			log.info("The message to be stored reaches the threshold[{}] -> {} , local deliveryTag[{}] ",
					cacheSize.longValue(), prefetchCount, this.lastDeliveryTag);
			flush();
		} else if (refreshNowEvent != null && refreshNowEvent.flushNow(t)) {
			log.info("----- 触发立刻刷新事件! tag -> {}-----", lastDeliveryTag);
			flush();
		}

	}

	/**
	 * batch flush queue message
	 */
	private void flush() {
		lock.lock();
		try {
			if (cacheSize.longValue() != 0) {
				if (batchHandler.handle(bufferList)) {
					channel.basicAck(lastDeliveryTag, true);
					log.info("ack tag -> {}", lastDeliveryTag);
					bufferList.clear();
					cacheSize.reset();
					log.info("cache clear, current size -> {}, cache -> {} , local tag -> {}", bufferList.size(),
							cacheSize.longValue(), lastDeliveryTag);
				}
			}
		} catch (IOException e) {
			// todo do reconnect and clear cache
			log.error("basicAck throw IOException -> message==[{}]", e.getMessage(), e);
		} catch (Exception e) {
			log.error("batch process failure, deliverTag[{}]", this.lastDeliveryTag, e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * schedule task : 如果缓存中的数据长期没有变动,触发flush动作 1.
	 * 当缓存队列中的值等于上一次定时任务触发的值,并且该值大于0,不等于rmq预取值,则认为该缓存数据长期没有变动,立即触发回调.
	 * <p>
	 * 缓存值达到预取值的大小交给主线程去触发刷新.减少锁的竞争.
	 */
	private void automaticFlush() {
		schedule.scheduleWithFixedDelay(() -> {
			try {
				if (cacheSize.longValue() == lastSize && cacheSize.longValue() > 0
						&& cacheSize.longValue() != prefetchCount) {
					log.info("automatic flush cache ...{} -> {} ,deliveryTag[{}]", cacheSize.longValue(), lastSize,
							this.lastDeliveryTag);
					flush();
				} else {
					lastSize = cacheSize.longValue();
				}
			} catch (Exception e) {
				log.error("automatic flush execute failure,deleveryTag[{}]", e, this.lastDeliveryTag);
			}
		}, 0, millisecond, TimeUnit.MILLISECONDS);
	}

	@SuppressWarnings("unused")
	private void destroy() {
		if (!schedule.isShutdown()) {
			schedule.shutdown();
			schedule = null;
		}
		if (channel.isOpen()) {
			try {
				channel.close();
			} catch (IOException e) {
				log.error("close channel failure", e);
			} catch (TimeoutException e) {
				log.error("close channel timeout ", e);
			}
		}
		channel = null;
	}
}
