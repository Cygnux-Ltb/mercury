package io.mercury.common.concurrent.disruptor;

import static io.mercury.common.collections.CollectionUtil.toArray;
import static io.mercury.common.concurrent.disruptor.CommonWaitStrategy.Sleeping;
import static io.mercury.common.concurrent.disruptor.CommonWaitStrategy.Yielding;
import static io.mercury.common.sys.CurrentRuntime.availableProcessors;

import java.util.List;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.slf4j.Logger;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;

import io.mercury.common.collections.MutableLists;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.functional.Processor;
import io.mercury.common.lang.Throws;
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

	@SuppressWarnings("unchecked")
	private RingProcessChain(String name, int size, @Nonnull EventFactory<E> eventFactory,
			@Nonnull WaitStrategy waitStrategy, @Nonnull StartMode mode,
			@Nonnull EventTranslatorOneArg<E, I> translator,
			@Nonnull MutableIntObjectMap<List<EventHandler<E>>> handlersMap) {
		super(name, size, eventFactory, waitStrategy, translator);
		int[] keys = handlersMap.keySet().toSortedArray();
		var handlers0 = handlersMap.get(keys[0]);
		if (keys.length == 1) {
			super.disruptor.handleEventsWith(toArray(handlers0, length -> {
				return (EventHandler<E>[]) new EventHandler[length];
			}));
		} else {
			var handlerGroup = super.disruptor.handleEventsWith(toArray(handlers0, length -> {
				return (EventHandler<E>[]) new EventHandler[length];
			}));
			for (int i = 1; i < keys.length; i++) {
				// 将处理器以处理链的方式添加进Disruptor
				var handlers = handlersMap.get(keys[i]);
				handlerGroup.then(toArray(handlers, length -> {
					return (EventHandler<E>[]) new EventHandler[length];
				}));
			}
		}
		log.info(
				"Initialize RingProcessChain -> {}, size -> {}, WaitStrategy -> {}, StartMode -> {}, EventHandler china level count -> {}",
				super.name, size, waitStrategy, mode, handlersMap.size());
		startWith(mode);
	}

	@Override
	protected String getComponentType() {
		return "RingProcessChain";
	}

	public static <E, I> Builder<E, I> newBuilder(@Nonnull Class<E> eventType,
			@Nonnull RingEventPublisher<E, I> publisher) {
		return newBuilder(
				// 使用反射EventFactory
				ReflectionEventFactory.with(eventType, log), publisher);
	}

	public static <E, I> Builder<E, I> newBuilder(@Nonnull Class<E> eventType,
			@Nonnull EventTranslatorOneArg<E, I> translator) {
		return newBuilder(
				// 使用反射EventFactory
				ReflectionEventFactory.with(eventType, log), translator);
	}

	public static <E, I> Builder<E, I> newBuilder(EventFactory<E> eventFactory,
			@Nonnull RingEventPublisher<E, I> publisher) {
		return newBuilder(eventFactory,
				// EventTranslator实现函数, 负责调用处理In对象到Event对象之间的转换
				(event, sequence, in) -> publisher.accept(event, in));
	}

	public static <E, I> Builder<E, I> newBuilder(EventFactory<E> eventFactory,
			@Nonnull EventTranslatorOneArg<E, I> translator) {
		return new Builder<>(eventFactory, translator);
	}

	public static class Builder<E, I> {

		private String name;
		private int size = 64;
		private StartMode mode = StartMode.Auto;
		private WaitStrategy waitStrategy;
		private final EventFactory<E> eventFactory;
		private final EventTranslatorOneArg<E, I> translator;
		private final MutableIntObjectMap<List<EventHandler<E>>> handlersMap = MutableMaps.newIntObjectHashMap();

		private Builder(EventFactory<E> eventFactory, EventTranslatorOneArg<E, I> translator) {
			this.eventFactory = eventFactory;
			this.translator = translator;
		}

		public Builder<E, I> addFirstProcessor(@Nonnull Processor<E> processor) {
			return addProcessor(Integer.MIN_VALUE, processor);
		}

		public Builder<E, I> addSecondProcessor(@Nonnull Processor<E> processor) {
			return addProcessor(Integer.MIN_VALUE + 1, processor);
		}

		public Builder<E, I> addLastProcessor(@Nonnull Processor<E> processor) {
			return addProcessor(Integer.MAX_VALUE, processor);
		}

		public Builder<E, I> addProcessor(int level, @Nonnull Processor<E> processor) {
			Assertor.nonNull(processor, "processor");
			handlersMap.getIfAbsentPut(level, MutableLists::newFastList).add(
					// 将Processor实现加载到HandlerWrapper中
					new EventHandlerWrapper<>(processor, log));
			return this;
		}

		public Builder<E, I> addFirstHandler(@Nonnull EventHandler<E> handler) {
			return addHandler(Integer.MIN_VALUE, handler);
		}

		public Builder<E, I> addSecondHandler(@Nonnull EventHandler<E> handler) {
			return addHandler(Integer.MIN_VALUE + 1, handler);
		}

		public Builder<E, I> addLastHandler(@Nonnull EventHandler<E> handler) {
			return addHandler(Integer.MAX_VALUE, handler);
		}

		public Builder<E, I> addHandler(int level, @Nonnull EventHandler<E> handler) {
			Assertor.nonNull(handler, "handler");
			handlersMap.getIfAbsentPut(level, MutableLists::newFastList).add(handler);
			return this;
		}

		public Builder<E, I> name(String name) {
			this.name = name;
			return this;
		}

		public Builder<E, I> setWaitStrategy(CommonWaitStrategy waitStrategy) {
			return setWaitStrategy(waitStrategy.get());
		}

		public Builder<E, I> setWaitStrategy(WaitStrategy waitStrategy) {
			this.waitStrategy = waitStrategy;
			return this;
		}

		public Builder<E, I> setStartMode(StartMode mode) {
			this.mode = mode;
			return this;
		}

		public Builder<E, I> size(int size) {
			this.size = size;
			return this;
		}

		public RingProcessChain<E, I> create() {
			if (handlersMap.isEmpty())
				Throws.illegalArgument("handlersMap");
			if (waitStrategy == null)
				waitStrategy = handlersMap.stream().mapToInt(List::size).sum() > availableProcessors() ? Sleeping.get()
						: Yielding.get();
			return new RingProcessChain<>(name, size, eventFactory, waitStrategy, mode, translator, handlersMap);
		}

	}

}
