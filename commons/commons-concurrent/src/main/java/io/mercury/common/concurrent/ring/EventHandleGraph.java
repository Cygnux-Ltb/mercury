package io.mercury.common.concurrent.ring;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.List;

public final class EventHandleGraph<E> {

    public EventHandleGraph() {
    }

    private EventHandler<E> singleHandler;

    void deploy(Disruptor<E> disruptor) {
        // TODO 异常处理
        disruptor.setDefaultExceptionHandler(null);
        if (singleHandler != null) {
            disruptor.handleEventsWith(singleHandler);
        }

    }

    private EventHandleGraph<E> setSingleHandler(EventHandler<E> singleHandler) {
        this.singleHandler = singleHandler;
        return this;
    }

    public static <E> EventHandleGraph<E> single(EventHandler<E> handler) {
        EventHandleGraph<E> handleGraph = new EventHandleGraph<>();
        handleGraph.setSingleHandler(handler);
        return handleGraph;
    }


    public static class Wizard<E> {

        private List<EventHandler<E>> pipeline;




    }

}
