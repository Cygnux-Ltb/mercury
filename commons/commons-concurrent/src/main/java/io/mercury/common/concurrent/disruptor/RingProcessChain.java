package io.mercury.common.concurrent.disruptor;

import java.util.List;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.slf4j.Logger;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;

import io.mercury.common.collections.CollectionUtil;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.collections.MutableMaps;
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

	@SuppressWarnings("unchecked")
	private RingProcessChain(String name, int size, @Nonnull EventFactory<E> eventFactory,
			@Nonnull WaitStrategyOption option, @Nonnull StartMode mode,
			@Nonnull EventTranslatorOneArg<E, I> translator,
			@Nonnull MutableIntObjectMap<List<EventHandler<E>>> handlersMap) {
		super(name, size, eventFactory, option,
				// EventTranslator实现函数, 负责调用处理In对象到Event对象之间的转换
				// (event, sequence, in) -> publisher.accept(event, in)
				translator);
		int[] keys = handlersMap.keySet().toArray();
		var handlers0 = handlersMap.get(keys[0]);
		if (keys.length == 1) {
			super.disruptor.handleEventsWith(CollectionUtil.toArray(handlers0, length -> {
				return new EventHandler[length];
			}));
		} else {
			var handlerGroup = super.disruptor.handleEventsWith(CollectionUtil.toArray(handlers0, length -> {
				return new EventHandler[length];
			}));
			for (int i = 1; i < keys.length; i++) {
				// 将处理器以处理链的方式添加进入Disruptor
				var handlers = handlersMap.get(keys[i]);
				handlerGroup.then(CollectionUtil.toArray(handlers, length -> {
					return new EventHandler[length];
				}));
			}
		}
		log.info(
				"Initialize RingProcessChain -> {}, size -> {}, WaitStrategy -> {}, StartMode -> {}, EventHandler level count -> {}",
				super.name, size, option, mode, handlersMap.size());
		startWith(mode);
	}

	@Override
	protected String getComponentType() {
		return "RingProcessChain";
	}

	public static class Builder<E, I> {

		private String name;
		private int size = 64;
		private WaitStrategyOption option = WaitStrategyOption.BusySpin;
		private StartMode mode = StartMode.Auto;
		private final EventFactory<E> eventFactory;
		private final EventTranslatorOneArg<E, I> translator;
		private final MutableIntObjectMap<List<EventHandler<E>>> handlersMap = MutableMaps.newIntObjectHashMap();

		private Builder(EventFactory<E> eventFactory, EventTranslatorOneArg<E, I> translator) {
			this.eventFactory = eventFactory;
			this.translator = translator;
		}

		public Builder<E, I> setFirstProcessor(@Nonnull Processor<E> processor) {
			Assertor.nonNull(processor, "processor");
			handlersMap.getIfAbsentPut(0, MutableLists::newFastList).add(
					// 将Processor实现加载到HandlerProxy中
					new EventHandlerProxy<>(processor, log));
			return this;
		}

		public Builder<E, I> setProcessor(int level, @Nonnull Processor<E> processor) {
			Assertor.nonNull(processor, "processor");
			if (level < 1)
				return setFirstProcessor(processor);
			handlersMap.getIfAbsentPut(level, MutableLists::newFastList).add(
					// 将Processor实现加载到HandlerProxy中
					new EventHandlerProxy<>(processor, log));
			return this;
		}

		public Builder<E, I> setFirstHandler(@Nonnull EventHandler<E> handler) {
			Assertor.nonNull(handler, "handler");
			handlersMap.getIfAbsentPut(0, MutableLists::newFastList).add(handler);
			return this;
		}

		public Builder<E, I> setHandler(int level, @Nonnull EventHandler<E> handler) {
			Assertor.nonNull(handler, "handler");
			if (level < 1)
				return setFirstHandler(handler);
			handlersMap.getIfAbsentPut(level, MutableLists::newFastList).add(handler);
			return this;
		}

		public Builder<E, I> name(String name) {
			this.name = name;
			return this;
		}

		public Builder<E, I> setWaitStrategy(WaitStrategyOption option) {
			this.option = option;
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
			return new RingProcessChain<>(name, size, eventFactory, option, mode, translator, handlersMap);
		}

	}

}
