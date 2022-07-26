package io.mercury.actors;

public interface IActorScheduler extends AutoCloseable {

    default void actorCreated(Object actorId) {
    }

    default void actorDisposed(Object actorId) {
    }

    void schedule(Runnable task, Object actorId);

    @Override
    default void close() {
    }
}
