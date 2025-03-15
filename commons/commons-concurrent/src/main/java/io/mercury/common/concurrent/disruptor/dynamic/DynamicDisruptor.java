package io.mercury.common.concurrent.disruptor.dynamic;


import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.Sequencer;
import com.lmax.disruptor.WorkProcessor;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ExceptionHandlerWrapper;
import io.mercury.common.concurrent.disruptor.dynamic.core.AbstractSentinelHandler;
import io.mercury.common.concurrent.disruptor.dynamic.core.DynamicConsumer;
import io.mercury.common.concurrent.disruptor.dynamic.core.HandlerEvent;
import io.mercury.common.concurrent.disruptor.dynamic.core.HandlerFactory;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelClient;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelEvent;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelListener;
import io.mercury.common.concurrent.disruptor.dynamic.strategy.PidStrategy;
import io.mercury.common.concurrent.disruptor.dynamic.strategy.RegulateStrategy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author : Rookiex
 * @version :
 */
public class DynamicDisruptor implements DynamicConsumer, SentinelListener {

    public static final int NU_USED = 0;
    public static final int USED = 1;
    public static final int STATE_CHANGE = 2;

    public static final int DEFAULT_INIT_SIZE = 32;
    public static final int DEFAULT_CORE_SIZE = 32;
    public static final int DEFAULT_MAX_SIZE = 256;

    private final String name;
    private final int initSize;
    private final int coreSize;
    private final int maxSize;

    private SentinelClient sentinelClient;

    public DynamicDisruptor(String name) {
        this.name = name;
        this.initSize = DEFAULT_INIT_SIZE;
        this.coreSize = DEFAULT_CORE_SIZE;
        this.maxSize = DEFAULT_MAX_SIZE;
    }

    public DynamicDisruptor(String name, int initSize, int coreSize, int maxSize) {
        this.name = name;
        this.initSize = initSize;
        this.coreSize = coreSize;
        this.maxSize = maxSize;
    }

    /**
     * 工作的序列
     */
    private final Sequence workSequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);

    /**
     * 异常handler
     */
    private ExceptionHandler<HandlerEvent> exceptionHandler = new ExceptionHandlerWrapper<>();

    /**
     * 工作的processor数组
     */
    private WorkProcessor<HandlerEvent>[] processors;

    /**
     * 工作的handler数组,和processor数组一一对应
     */
    private AbstractSentinelHandler[] handlers;

    /**
     * 和processor,handler一一对应,标识位置上是否空闲,用Atomic封装了CAS操作
     */
    private AtomicIntegerArray availableArray;

    /**
     * 工作线程池
     */
    private ExecutorService executor;

    /**
     * Disruptor
     */
    private Disruptor<HandlerEvent> disruptor;

    /**
     * 线程取名字用的同步int
     */
    private final AtomicInteger threadId = new AtomicInteger();

    /**
     * handler 工厂
     */
    private HandlerFactory handlerFactory;

    /**
     * 默认的处理策略是比例控制
     */
    private RegulateStrategy strategy = new PidStrategy();

    public ExceptionHandler<HandlerEvent> getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler<HandlerEvent> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void init(int bufferSize, SentinelClient sentinelClient, HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
        init(bufferSize, sentinelClient);
    }

    @SuppressWarnings("unchecked")
    private void init(int bufferSize, SentinelClient sentinelClient) {
        this.processors = new WorkProcessor[maxSize];
        this.handlers = new AbstractSentinelHandler[maxSize];
        this.availableArray = new AtomicIntegerArray(maxSize);
        this.sentinelClient = sentinelClient;
        this.handlerFactory.setSentinelClient(sentinelClient);
        this.sentinelClient.addListener(this);

        this.executor = new ThreadPoolExecutor(coreSize, maxSize, 120L, TimeUnit.SECONDS, new SynchronousQueue<>(),
                r -> {
                    Thread t = new Thread(r);
                    t.setName("DisruptorHandler-" + name + "-" + threadId.incrementAndGet());
                    return t;
                });

        this.disruptor = new Disruptor<>(HandlerEvent::new, bufferSize, r -> {
            Thread t = new Thread(r);
            t.setName("DisruptorCore-" + name);
            return t;
        });

        disruptor.start();
    }

    public void start() {
        RingBuffer<HandlerEvent> ringBuffer = disruptor.getRingBuffer();

        for (int i = 0; i < initSize; i++) {
            AbstractSentinelHandler handlerEvent = createHandler();
            handlers[i] = handlerEvent;
            processors[i] = createProcessor(handlerEvent);
            updateUseState(i, USED);
            ringBuffer.addGatingSequences(processors[i].getSequence());
            executor.execute(processors[i]);
        }

        sentinelClient.start();
    }

    private AbstractSentinelHandler createHandler() {
        return handlerFactory.createHandler();
    }

    private WorkProcessor<HandlerEvent> createProcessor(AbstractSentinelHandler disruptorHandler) {
        RingBuffer<HandlerEvent> ringBuffer = disruptor.getRingBuffer();
        return new WorkProcessor<>(ringBuffer, ringBuffer.newBarrier(), disruptorHandler, exceptionHandler,
                workSequence);
    }

    @Override
    public void incrConsumer() {
        int nextUnUsed = getNextUnUsed();
        if (nextUnUsed == -1) {
            // 没有空位和大部分都在状态切换的时候
            System.out.println("no available index exits ==> ");
        } else {
            RingBuffer<HandlerEvent> ringBuffer = disruptor.getRingBuffer();
            AbstractSentinelHandler disruptorHandler = createHandler();
            WorkProcessor<HandlerEvent> processor = createProcessor(disruptorHandler);
            processors[nextUnUsed] = processor;
            handlers[nextUnUsed] = disruptorHandler;

            ringBuffer.addGatingSequences(processor.getSequence());
            executor.execute(processor);
            updateUseState(nextUnUsed, USED);
        }
    }

    @Override
    public void decrConsumer() {
        int nextUnUsed = getNextUsed();
        if (nextUnUsed == -1) {
            // 已经小于等于核心数量的时候
            System.out.println("used thread less than core size");
        } else {
            RingBuffer<HandlerEvent> ringBuffer = disruptor.getRingBuffer();
            WorkProcessor<HandlerEvent> processor = processors[nextUnUsed];
            AbstractSentinelHandler handler = handlers[nextUnUsed];
            if (processor == null || handler == null) {
                System.out.println("remove disruptor thread ,handler == " + handler + " ,processor == " + processor);
            }
            if (processor != null && handler != null) {
                processor.halt();
                try {
                    handler.awaitShutdown();
                } catch (InterruptedException ignored) {
                }
                ringBuffer.removeGatingSequence(processor.getSequence());
            }

            processors[nextUnUsed] = null;
            handlers[nextUnUsed] = null;
            updateUseState(nextUnUsed, NU_USED);
        }
    }

    public void publishEvent(EventTranslatorVararg<HandlerEvent> translator, Object... args) {
        this.disruptor.getRingBuffer().publishEvent(translator, args);
        sentinelClient.addProduceCount();
    }

    private int getNextUsed() {
        int count = 0;
        for (int i = 0; i < maxSize; i++) {
            if (availableArray.get(i) == USED) {
                count++;
            }
            if (count > coreSize) {
                if (availableArray.compareAndSet(i, USED, STATE_CHANGE)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int getNextUnUsed() {
        for (int i = 0; i < maxSize; i++) {
            if (availableArray.compareAndSet(i, NU_USED, STATE_CHANGE)) {
                return i;
            }
        }
        return -1;
    }

    private void updateUseState(int index, int state) {
        availableArray.set(index, state);
    }

    @Override
    public void notice(SentinelEvent sentinelEvent) {
        strategy.regulate(this, sentinelEvent);
    }

    public void setStrategy(RegulateStrategy strategy) {
        this.strategy = strategy;
    }

    public void setHandlerFactory(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }
}
