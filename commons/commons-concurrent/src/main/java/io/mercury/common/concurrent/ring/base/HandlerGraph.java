package io.mercury.common.concurrent.ring.base;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static io.mercury.common.collections.CollectionUtil.toArray;
import static io.mercury.common.collections.MutableLists.newFastList;
import static io.mercury.common.collections.MutableMaps.newIntObjectMap;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNullElse;

public final class HandlerGraph<E> {

    private static final Logger log = Log4j2LoggerFactory.getLogger(HandlerGraph.class);

    private final HandleType type;

    private final ExceptionHandler<E> exceptionHandler;

    private final List<EventHandler<E>> eventHandlers;

    private final List<WorkHandler<E>> workHandlers;

    private final MutableIntObjectMap<List<EventHandler<E>>> sortedEventHandlers;

    private final MutableIntObjectMap<List<WorkHandler<E>>> sortedWorkHandlers;

    private HandlerGraph(@Nonnull HandleType type,
                         @Nullable ExceptionHandler<E> exceptionHandler,
                         @Nullable List<EventHandler<E>> eventHandlers,
                         @Nullable List<WorkHandler<E>> workHandlers,
                         @Nullable MutableIntObjectMap<List<EventHandler<E>>> sortedEventHandlers,
                         @Nullable MutableIntObjectMap<List<WorkHandler<E>>> sortedWorkHandlers) {
        this.type = type;
        this.exceptionHandler = exceptionHandler;
        this.eventHandlers = eventHandlers;
        this.workHandlers = workHandlers;
        this.sortedEventHandlers = sortedEventHandlers;
        this.sortedWorkHandlers = sortedWorkHandlers;
    }

    public void deploy(Disruptor<E> disruptor) {
        disruptor.setDefaultExceptionHandler(requireNonNullElse(exceptionHandler, new ExceptionHandler<>() {
            @Override
            public void handleEventException(Throwable ex, long sequence, E event) {
                log.error("handleEventException -> event == {}, sequence == {}, exception message == {}",
                        event, sequence, ex.getMessage(), ex);
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                log.error("handleOnStartException -> {}", ex.getMessage(), ex);
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                log.error("handleOnShutdownException -> {}", ex.getMessage(), ex);
            }
        }));
        switch (type) {
            case SINGLE, PIPELINE -> setPipelineHandler(disruptor);
            case BROADCAST -> setBroadcastHandlers(disruptor);
            case DISTRIBUTION -> setDistributionHandlers(disruptor);
            case COMPLEX -> setComplexHandlers(disruptor);
        }
    }

    private void setPipelineHandler(Disruptor<E> disruptor) {
        if (eventHandlers != null) {
            if (eventHandlers.size() > 1) {
                var handlerGroup = disruptor.handleEventsWith(eventHandlers.getFirst());
                for (int i = 1; 1 < eventHandlers.size(); i++) {
                    handlerGroup.then(eventHandlers.get(i));
                }
            } else {
                // With set single event
                disruptor.handleEventsWith(eventHandlers.getFirst());
            }
        } else {
            throw new IllegalStateException("List<EventHandler> is null");
        }
    }

    @SuppressWarnings("unchecked")
    private void setBroadcastHandlers(Disruptor<E> disruptor) {
        if (eventHandlers != null)
            disruptor.handleEventsWith(toArray(eventHandlers, EventHandler[]::new));
        else
            throw new IllegalStateException("List<EventHandler> is null");

    }

    @SuppressWarnings("unchecked")
    private void setDistributionHandlers(Disruptor<E> disruptor) {
        if (workHandlers != null)
            disruptor.handleEventsWithWorkerPool(toArray(workHandlers, WorkHandler[]::new));
        else
            throw new IllegalStateException("List<WorkHandler> is null");
    }

    @SuppressWarnings("unchecked")
    private void setComplexHandlers(Disruptor<E> disruptor) {
        if (sortedEventHandlers != null && sortedWorkHandlers != null) {
            var keys = sortedEventHandlers.keySet().union(sortedWorkHandlers.keySet()).toList();
            int firstKey = keys.getFirst();
            EventHandlerGroup<E> handlerGroup;
            if (sortedEventHandlers.containsKey(firstKey))
                handlerGroup = disruptor.handleEventsWith(toArray(sortedEventHandlers.get(firstKey), EventHandler[]::new));
            else
                handlerGroup = disruptor.handleEventsWithWorkerPool(toArray(sortedWorkHandlers.get(firstKey), WorkHandler[]::new));
            for (int i = 1; i < keys.size(); i++) {
                int key = keys.get(i);
                if (sortedEventHandlers.containsKey(key))
                    handlerGroup.then(toArray(sortedEventHandlers.get(key), EventHandler[]::new));
                else
                    handlerGroup.thenHandleEventsWithWorkerPool(toArray(sortedWorkHandlers.get(key), WorkHandler[]::new));
            }
        } else
            throw new IllegalStateException("SortedEventHandlerList OR SortedWorkHandlerList is null");
    }


    public static <E> HandlerGraph<E> single(@Nonnull EventHandler<E> handler) {
        return new EventHandlerWizard<E>(HandleType.SINGLE)
                .add(handler)
                .build();
    }

    public static <E> HandlerGraph<E> single(@Nonnull EventHandler<E> handler,
                                             @Nonnull ExceptionHandler<E> exceptionHandler) {
        return new EventHandlerWizard<E>(HandleType.SINGLE)
                .add(handler)
                .exceptionHandler(exceptionHandler)
                .build();
    }

    public static <E> EventHandlerWizard<E> pipeline() {
        return new EventHandlerWizard<>(HandleType.PIPELINE);
    }

    @SafeVarargs
    public static <E> EventHandlerWizard<E> pipelineWith(@Nonnull EventHandler<E>... handlers) {
        return new EventHandlerWizard<E>(HandleType.PIPELINE)
                .add(handlers);
    }

    public static <E> EventHandlerWizard<E> broadcast() {
        return new EventHandlerWizard<>(HandleType.BROADCAST);
    }

    @SafeVarargs
    public static <E> EventHandlerWizard<E> broadcastTo(@Nonnull EventHandler<E>... handlers) {
        return new EventHandlerWizard<E>(HandleType.BROADCAST)
                .add(handlers);
    }

    public static <E> WorkHandlerWizard<E> distribution() {
        return new WorkHandlerWizard<>();
    }

    @SafeVarargs
    public static <E> WorkHandlerWizard<E> distributionTo(@Nonnull WorkHandler<E>... handlers) {
        return new WorkHandlerWizard<E>()
                .add(handlers);
    }


    public static <E> ComplexHandlerWizard<E> complex() {
        return new ComplexHandlerWizard<>();
    }

    @SafeVarargs
    public static <E> ComplexHandlerWizard<E> complexWithFirst(EventHandler<E>... handlers) {
        return new ComplexHandlerWizard<E>().first(handlers);
    }

    @SafeVarargs
    public static <E> ComplexHandlerWizard<E> complexWithFirst(WorkHandler<E>... handlers) {
        return new ComplexHandlerWizard<E>().first(handlers);
    }

    private abstract static class Wizard<E, W extends Wizard<E, W>> {

        protected final HandleType type;

        protected ExceptionHandler<E> exceptionHandler;

        private Wizard(HandleType type) {
            this.type = type;
        }

        public W exceptionHandler(ExceptionHandler<E> exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return returnThis();
        }

        public abstract W returnThis();

        public abstract HandlerGraph<E> build();

    }

    public static class EventHandlerWizard<E> extends Wizard<E, EventHandlerWizard<E>> {

        private final MutableList<EventHandler<E>> eventHandlers = newFastList();

        private EventHandlerWizard(HandleType type) {
            super(type);
        }

        @SafeVarargs
        public final EventHandlerWizard<E> add(EventHandler<E>... handlers) {
            this.eventHandlers.addAll(asList(handlers));
            return this;
        }

        @Override
        public EventHandlerWizard<E> returnThis() {
            return this;
        }

        @Override
        public HandlerGraph<E> build() {
            return new HandlerGraph<>(type, exceptionHandler,
                    eventHandlers, null,
                    null, null);
        }

    }


    public static class WorkHandlerWizard<E> extends Wizard<E, WorkHandlerWizard<E>> {

        private final MutableList<WorkHandler<E>> workHandlers = newFastList();

        private WorkHandlerWizard() {
            super(HandleType.DISTRIBUTION);
        }

        @SafeVarargs
        public final WorkHandlerWizard<E> add(WorkHandler<E>... handlers) {
            this.workHandlers.addAll(asList(handlers));
            return this;
        }

        @Override
        public WorkHandlerWizard<E> returnThis() {
            return this;
        }

        @Override
        public HandlerGraph<E> build() {
            return new HandlerGraph<>(type, exceptionHandler,
                    null, workHandlers,
                    null, null);
        }

    }

    public static class ComplexHandlerWizard<E> extends Wizard<E, ComplexHandlerWizard<E>> {

        private final MutableIntObjectMap<List<EventHandler<E>>> sortedEventHandlers = MutableMaps.newIntObjectMap();

        private final MutableIntObjectMap<List<WorkHandler<E>>> sortedWorkHandlers = MutableMaps.newIntObjectMap();

        private int index = 0;

        private ComplexHandlerWizard() {
            super(HandleType.COMPLEX);
        }

        @SafeVarargs
        public final ComplexHandlerWizard<E> first(EventHandler<E>... handlers) {
            sortedEventHandlers.put(Integer.MIN_VALUE, asList(handlers));
            return this;
        }

        @SafeVarargs
        public final ComplexHandlerWizard<E> first(WorkHandler<E>... handlers) {
            sortedWorkHandlers.put(Integer.MIN_VALUE, asList(handlers));
            return this;
        }

        @SafeVarargs
        public final ComplexHandlerWizard<E> then(EventHandler<E>... handlers) {
            sortedEventHandlers.put(index, asList(handlers));
            index++;
            return this;
        }

        @SafeVarargs
        public final ComplexHandlerWizard<E> then(WorkHandler<E>... handlers) {
            sortedWorkHandlers.put(index, asList(handlers));
            index++;
            return this;
        }

        @SafeVarargs
        public final ComplexHandlerWizard<E> last(EventHandler<E>... handlers) {
            sortedEventHandlers.put(Integer.MAX_VALUE, asList(handlers));
            return this;
        }

        @SafeVarargs
        public final ComplexHandlerWizard<E> last(WorkHandler<E>... handlers) {
            sortedWorkHandlers.put(Integer.MAX_VALUE, asList(handlers));
            return this;
        }

        @Override
        public ComplexHandlerWizard<E> returnThis() {
            return this;
        }

        @Override
        public HandlerGraph<E> build() {
            return new HandlerGraph<>(type, exceptionHandler,
                    null, null,
                    sortedEventHandlers, sortedWorkHandlers);
        }

    }

    private enum HandleType {
        SINGLE,
        PIPELINE,
        BROADCAST,
        DISTRIBUTION,
        COMPLEX
    }

}
