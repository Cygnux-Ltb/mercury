package io.mercury.common.concurrent.ring;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.MutableSet;
import org.slf4j.Logger;

import java.util.List;

import static io.mercury.common.collections.CollectionUtil.toArray;
import static io.mercury.common.collections.MutableLists.newFastList;
import static io.mercury.common.collections.MutableMaps.newIntObjectHashMap;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNullElse;

public final class HandlerGraph<E> {

    private static final Logger log = Log4j2LoggerFactory.getLogger(HandlerGraph.class);

    private final HandleType type;

    private ExceptionHandler<E> exceptionHandler;

    private List<EventHandler<E>> eventHandlers;

    private List<WorkHandler<E>> workHandlers;

    private MutableIntObjectMap<MutableSet<EventHandler<E>>> pipeline;


    private EventHandlerSet(List<EventHandler<E>> eventHandlers, HandleType type) {
        this.eventHandlers = eventHandlers;
        this.type = type;
    }

    private EventHandlerSet(List<WorkHandler<E>> workHandlers) {
        this.workHandlers = workHandlers;
        this.type = HandleType.DISTRIBUTION;
    }

    private EventHandlerSet(MutableIntObjectMap<MutableSet<EventHandler<E>>> pipeline) {
        this.pipeline = pipeline;
        this.type = HandleType.PIPELINE;
    }

    private EventHandlerSet<E> setExceptionHandler(ExceptionHandler<E> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    private final ExceptionHandler<E> defaultExceptionHandler = new ExceptionHandler<>() {

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
    };

    void deploy(Disruptor<E> disruptor) {
        disruptor.setDefaultExceptionHandler(requireNonNullElse(exceptionHandler, defaultExceptionHandler));
        switch (type) {
            case GROUP -> setEventHandlers(disruptor);
            case DISTRIBUTION -> setWorkHandlers(disruptor);
            case GRAPH -> setPipelineHandler(disruptor);
        }
    }

    @SuppressWarnings("unchecked")
    private void setEventHandlers(Disruptor<E> disruptor) {
        disruptor.handleEventsWith(toArray(eventHandlers, EventHandler[]::new));
    }

    @SuppressWarnings("unchecked")
    private void setWorkHandlers(Disruptor<E> disruptor) {
        disruptor.handleEventsWithWorkerPool(toArray(workHandlers, WorkHandler[]::new));
    }

    private void setPipelineHandler(Disruptor<E> disruptor) {
        disruptor.handleEventsWith(handlers);
    }

    public static <E> EventHandlerSet<E> single(EventHandler<E> handler) {
        return new EventHandlerWizard<E>().add(handler).build();
    }

    public static <E> EventHandlerSet<E> single(EventHandler<E> handler, ExceptionHandler<E> exceptionHandler) {
        return new EventHandlerWizard<E>().add(handler).exceptionHandler(exceptionHandler).build();
    }

    @SafeVarargs
    public static <E> EventHandlerSet<E> multicast(EventHandler<E>... handlers) {
        return new EventHandlerWizard<E>().add(handlers).build();
    }

    public static <E> ComplexWizard<E> pipeline() {
        return new ComplexWizard<>();
    }

    @SafeVarargs
    public static <E> WorkHandlerWizard<E> distribution(WorkHandler<E>... handlers) {
        return new WorkHandlerWizard<E>().add(handlers);
    }


    private abstract static class Wizard<E, W extends Wizard<E, W>> {

        private final HandleType type;

        private ExceptionHandler<E> exceptionHandler;

        private Wizard(HandleType type) {
            this.type = type;
        }

        public HandleType getType() {
            return type;
        }

        private ExceptionHandler<E> getExceptionHandler() {
            return exceptionHandler;
        }

        public W exceptionHandler(ExceptionHandler<E> exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return returnThis();
        }

        public abstract W returnThis();

        public abstract EventHandlerSet<E> build();

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
        public EventHandlerSet<E> build() {
            new Eve
            return null;
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
        public EventHandlerSet<E> build() {

            return null;
        }

    }

    public static class HandlerGraphWizard<E> extends Wizard<E, HandlerGraphWizard<E>> {

        private final MutableIntObjectMap<MutableIntObjectMap<List<EventHandler<E>>>> handlerGraph = newIntObjectHashMap();

        private final static int HANDLER_GROUP = 0;

        private final static int HANDLER_POOL = -1;

        @Override
        public HandlerGraphWizard<E> returnThis() {
            return this;
        }

        @Override
        public EventHandlerSet<E> build() {
            return null;
        }

    }

    private enum HandleType {
        GROUP,
        DISTRIBUTION,
        GRAPH
    }

}
