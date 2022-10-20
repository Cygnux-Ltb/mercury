package io.mercury.actors;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface IActorSystem {

    /**
     * Initiate an orderly shutdown of the actor system.
     * <p>
     * The currently running actor actions are not terminated; new calls to actors are ignored, actor system triggers actor destructors in their respective thread contexts.
     * <p>
     * Creating new actors under this system will fail after initiating the shutdown.
     * <p>
     * Clients may use {@link #shutdownCompletable()} to be notified when the shutdown procedure completes.
     *
     * @return CompletableFuture that client may use to be notified when shutdown completes; the supplied string is shutdown reason
     */
    CompletableFuture<String> shutdown();

    /**
     * @return a CompletableFuture to be triggered when actor system shutdown completes. The result value is the shutdown reason.
     */
    CompletableFuture<String> shutdownCompletable();

    /**
     * Get an instance of {@link IActorBuilder} under this system.
     *
     * @param <T> actor POJO class
     * @return actor builder instance
     */
    <T> IActorBuilder<T> actorBuilder();

    /**
     * Create a new actor under this system with a specified POJO instance factory and name.
     *
     * @param <T>         actor POJO class
     * @param constructor factory to create actor POJO class instance
     * @param name        actor name
     * @return ActorRef actor reference
     */
    <T> IActorRef<T> actorOf(Supplier<T> constructor, String name);

    /**
     * Create a new actor under this system with a specified POJO instance factory and an autogenerated name.
     *
     * @param <T>         actor POJO class
     * @param constructor factory to create actor POJO class instance.
     * @return ActorRef actor reference
     */
    <T> IActorRef<T> actorOf(Supplier<T> constructor);

    <I, T> IForkBuilder<I, T> forkBuilder(Collection<I> ids);

    String name();

}