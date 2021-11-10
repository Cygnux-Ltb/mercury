package io.mercury.common.concurrent.disruptor;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;

import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;

/**
 * 
 * @author yellow013
 *
 * @param <E>
 */
public class RingProcessChain<E, I> extends SingleProducerRingBuffer<E, I> {

	private static final Logger log = CommonLoggerFactory.getLogger(RingProcessChain.class);

	/**
	 * 
	 * @param name
	 * @param size
	 * @param eventType
	 * @param option
	 * @param mode
	 * @param publisher
	 * @param processors
	 */
	@SafeVarargs
	public RingProcessChain(String name, int size, @Nonnull Class<E> eventType, @Nonnull WaitStrategyOption option,
			@Nonnull StartMode mode, @Nonnull RingEventPublisher<E, I> publisher, @Nonnull Processor<E>... processors) {
		this(name, size,
				// 使用反射EventFactory
				ReflectionEventFactory.with(eventType, log), option, mode, publisher, processors);
	}

	/**
	 * 
	 * @param name
	 * @param size
	 * @param eventFactory
	 * @param option
	 * @param mode
	 * @param publisher
	 * @param processors
	 */
	@SafeVarargs
	public RingProcessChain(String name, int size, @Nonnull EventFactory<E> eventFactory,
			@Nonnull WaitStrategyOption option, @Nonnull StartMode mode, @Nonnull RingEventPublisher<E, I> publisher,
			@Nonnull Processor<E>... processors) {
		this(name, size, eventFactory, option, mode, publisher,
				// 通过实现供应器接口进行类型转换, 对泛型进行明确声明.
				// 避开可变长参数的数组类型自动推导导致的无法正确识别构造函数的问题
				new Supplier<List<EventHandler<E>>>() {
					public List<EventHandler<E>> get() {
						return Stream.of(Assertor.requiredLength(processors, 1, "processors"))
								.map(EventHandlerProxy::new).collect(Collectors.toList());
					}
				}.get());

	}

	/**
	 * 
	 * @param name
	 * @param size
	 * @param eventType
	 * @param option
	 * @param mode
	 * @param publisher
	 * @param handlers
	 */
	public RingProcessChain(String name, int size, @Nonnull Class<E> eventType, @Nonnull WaitStrategyOption option,
			@Nonnull StartMode mode, @Nonnull RingEventPublisher<E, I> publisher,
			@Nonnull List<EventHandler<E>> handlers) {
		this(name, size,
				// 使用反射EventFactory
				ReflectionEventFactory.with(eventType, log), option, mode, publisher, handlers);
	}

	/**
	 * 
	 * @param name
	 * @param size
	 * @param eventFactory
	 * @param option
	 * @param mode
	 * @param publisher
	 * @param handlers
	 */
	public RingProcessChain(String name, int size, @Nonnull EventFactory<E> eventFactory,
			@Nonnull WaitStrategyOption option, @Nonnull StartMode mode, @Nonnull RingEventPublisher<E, I> publisher,
			@Nonnull List<EventHandler<E>> handlers) {
		super(name, size, eventFactory, option,
				// EventTranslator实现函数, 负责调用处理In对象到Event对象之间的转换
				(event, sequence, in) -> publisher.accept(event, in));
		Assertor.requiredLength(handlers, 1, "handlers");
		// 将处理器以处理链的方式添加进入Disruptor
		if (handlers.size() == 1) {
			getDisruptor().handleEventsWith(handlers.get(0));
		} else {
			var handlerGroup = getDisruptor().handleEventsWith(handlers.get(0));
			for (int i = 1; i < handlers.size(); i++)
				handlerGroup.then(handlers.get(i));
		}
		log.info(
				"Initialize RingProcessChain -> {}, size -> {}, WaitStrategy -> {}, StartMode -> {}, EventHandler count -> {}",
				super.name, size, option, mode, handlers.size());
		startWith(mode);
	}

	@Override
	protected String getComponentType() {
		return "RingProcessChain";
	}

}
