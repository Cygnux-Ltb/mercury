package io.mercury.actors.impl;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import io.mercury.actors.Actor;
import io.mercury.actors.IActorRef;
import io.mercury.actors.IActorScheduler;
import io.mercury.actors.impl.IRegSet.IRegistration;

public class ActorImpl<T> implements IActorRef<T> {

    private volatile T object;
    private final IActorSystem actorSystem;
    private final IActorScheduler scheduler;
    private final String name;
    private final BiConsumer<T, Exception> exceptionHandler;
    private final Consumer<T> destructor;
    private volatile Object box;
    private volatile IRegistration reg;

    ActorImpl(T object, Supplier<T> constructor, IActorScheduler scheduler,
              IActorSystem actorSystem, String name,
              BiConsumer<T, Exception> exceptionHandler,
              Consumer<T> destructor) {
        this.actorSystem = actorSystem;
        this.exceptionHandler = exceptionHandler;
        this.name = name == null ? getClass().getSimpleName() + "-" + UUID.randomUUID() : name;
        this.destructor = destructor;
        if (object != null) {
            this.object = object;
        }
        this.scheduler = scheduler;
        scheduler.actorCreated(this);
        if (constructor != null) {
            this.object = constructor.get();
        }
        actorSystem.add(this);
    }

    @Override
    public void tell(Consumer<T> action) {
        IActorRef<?> caller = Actor.current();
        scheduleCall(action, caller);
    }

    private void scheduleCall(Consumer<T> action, IActorRef<?> caller) {
        scheduleCallErrorAware(action, caller, e -> exceptionHandler.accept(object, e));
    }

    private void scheduleCallErrorAware(Consumer<T> action, IActorRef<?> caller, Consumer<Exception> exceptionCallback) {
        scheduler.schedule(() -> {
            Actor.setCurrent(this);
            Actor.setCaller(caller);
            try {
                if (object == null)
                    return;
                action.accept(object);
            } catch (Exception e) {
                exceptionCallback.accept(e);
            } finally {
                Actor.setCurrent(null);
                Actor.setCaller(null);
            }
        }, this);
    }

    @Override
    public void later(Consumer<T> action, long ms) {
        IActorRef<?> caller = Actor.current();
        actorSystem.later(() -> {
            if (object != null) {
                scheduleCall(action, caller);
            }
        }, ms);
    }

    @Override
    public <R> void ask(BiConsumer<T, Consumer<R>> action, Consumer<R> consumer) {
        IActorRef<?> current = Actor.current();
        Consumer<R> completion = current == null
                ? consumer
                : result -> current.tell(c -> consumer.accept(result));
        tell(target -> action.accept(target, completion));
    }

    @Override
    public <R> void ask(Function<T, R> call, Consumer<R> consumer) {
        ask((target, callback) -> callback.accept(call.apply(target)), consumer);
    }

    @Override
    public <R> CompletableFuture<R> ask(BiConsumer<T, Consumer<R>> action) {
        IActorRef<?> current = Actor.current();
        CompletableFuture<R> future = new CompletableFuture<>();
        Consumer<R> completion = current == null
                ? future::complete
                : result -> current.tell(c -> future.complete(result));
        Consumer<Exception> failure = current == null
                ? future::completeExceptionally
                : exception -> current.tell(c -> future.completeExceptionally(exception));
        scheduleCallErrorAware(target -> action.accept(target, completion), current, failure);
        return future;
    }

    @Override
    public <R> CompletableFuture<R> ask(Function<T, R> action) {
        return ask((target, callback) -> callback.accept(action.apply(target)));
    }

    private static IActorRef<?> safeCurrent() {
        IActorRef<?> caller = Actor.current();
        if (caller == null)
            throw new IllegalStateException("It is not allowed to call ask from non-actor context. There's no actor to receive the response");
        return caller;
    }

    T object() {
        return object;
    }

    @Override
    public String toString() {
        return "[" + actorSystem.name() + ":" + name + "]";
    }

    @Override
    public io.mercury.actors.IActorSystem system() {
        return actorSystem;
    }

    /**
     * Called internally from system
     */
    void dispose(Runnable whenFinished) {
        tell(o -> {
            if (destructor != null) {
                try {
                    destructor.accept(object);
                } catch (Exception ex) {
                    ex.printStackTrace(); // TODO: logging
                }
            }
            ((IActorSystem) system()).remove(this);
            scheduler.actorDisposed(this);
            object = null;
            whenFinished.run();
        });

    }

    @Override
    public void close() {
        dispose(() -> {
        });
    }

    void box(Object box) {
        this.box = box;
    }

    Object box() {
        return box;
    }

    void reg(IRegistration reg) {
        this.reg = reg;
    }

    IRegistration reg() {
        return reg;
    }

}