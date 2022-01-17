package io.mercury.common.concurrent.disruptor;

import static io.mercury.common.thread.ThreadSupport.newMaxPriorityThread;

import org.slf4j.Logger;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import io.mercury.common.collections.queue.LoadContainer;
import io.mercury.common.concurrent.queue.AbstractSingleConsumerQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.ThreadSupport;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
public class RingQueue<T> extends AbstractSingleConsumerQueue<T> {

	private static final Logger log = Log4j2LoggerFactory.getLogger(RingQueue.class);

	private final Disruptor<LoadContainer<T>> disruptor;

	private final LoadContainerEventProducer producer;

	public RingQueue(String queueName, int size, Processor<T> processor) {
		this(queueName, size, true, processor);
	}

	public RingQueue(String queueName, int size, boolean startNow, Processor<T> processor) {
		this(queueName, size, startNow, processor, CommonWaitStrategy.Sleeping.get());
	}

	public RingQueue(String queueName, int size, boolean startNow, Processor<T> processor, WaitStrategy strategy) {
		super(processor);
		if (queueName != null)
			super.name = queueName;
		// if (queueSize == 0 || queueSize % 2 != 0)
		// throw new IllegalArgumentException("queueSize set error...");
		this.disruptor = new Disruptor<>(
				// 实现EventFactory<LoadContainer<>>的Lambda
				LoadContainer::new,
				// 队列容量
				size,
				// 实现ThreadFactory的Lambda
				(Runnable runnable) -> newMaxPriorityThread(this.name + "-worker", runnable),
				// DaemonThreadFactory.INSTANCE,
				// 生产者策略, 使用单生产者
				ProducerType.SINGLE,
				// Waiting策略
				strategy);
		this.disruptor.handleEventsWith(this::process);
		this.producer = new LoadContainerEventProducer(disruptor.getRingBuffer());
		if (startNow)
			start();
	}

	private void process(LoadContainer<T> container, long sequence, boolean endOfBatch) {
		try {
			processor.process(container.unloading());
		} catch (Exception e) {
			log.error("processor.process(t) throw exception -> [{}]", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * LoadContainerEventProducer
	 * 
	 * @author yellow013
	 */
	private class LoadContainerEventProducer {

		private final RingBuffer<LoadContainer<T>> ringBuffer;

		private final EventTranslatorOneArg<LoadContainer<T>, T> translator = (container, sequence, t) -> container
				.loading(t);

		private LoadContainerEventProducer(RingBuffer<LoadContainer<T>> ringBuffer) {
			this.ringBuffer = ringBuffer;
		}

		private void onEvent(T t) {
			ringBuffer.publishEvent(translator, t);
		}
	}

	@Override
	public boolean enqueue(T t) {
		try {
			if (!isRunning())
				return false;
			producer.onEvent(t);
			return true;
		} catch (Exception e) {
			log.error("producer.onData(t) throw exception -> [{}]", e.getMessage(), e);
			return false;
		}
	}

	@Override
	protected void start0() {
		disruptor.start();
		log.info("Disruptor::start() func execution succeed, {} is start", name);
	}

	@Override
	protected void stop0() {
		disruptor.shutdown();
		log.info("Disruptor::shutdown() func execution succeed, {} is shutdown", name);
	}

	public static void main(String[] args) {

		RingQueue<Integer> queue = new RingQueue<>("Test-Queue", 32, true, in -> System.out.println(in));

		ThreadSupport.startNewThread(() -> {
			int i = 0;
			for (;;)
				queue.enqueue(++i);
		});

		SleepSupport.sleep(10000);
		queue.stop();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public QueueType getQueueType() {
		return QueueType.SPSC;
	}

}
