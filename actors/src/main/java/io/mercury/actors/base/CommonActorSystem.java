package io.mercury.actors.base;

import static io.mercury.common.thread.ShutdownHooks.addShutdownHookThread;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.mercury.common.thread.ThreadTool;
import scala.concurrent.Future;

public final class CommonActorSystem {

	private final ActorSystem internal;

	private final LoggingAdapter logger;

	public static final CommonActorSystem newInstance(String name) {
		return new CommonActorSystem(name);
	}

	private CommonActorSystem(String name) {
		this.internal = ActorSystem.create(name);
		this.logger = Logging.getLogger(internal, this);
		// Add ShutdownHook
		addShutdownHookThread("CommonActorSystemTerminateThread", this::terminateActorSystem);
	}

	private void terminateActorSystem() {
		logger.info("ActorSystem {} is terminated...", internal.name());
		Future<Terminated> terminate = internal.terminate();
		while (!terminate.isCompleted())
			ThreadTool.sleep(100);
	}

	public ActorSystem internal() {
		return internal;
	}

	public String name() {
		return internal.name();
	}

	public ActorRef actorOf(Props props) {
		return internal.actorOf(props);
	}

	public ActorRef actorOf(Props props, String name) {
		return internal.actorOf(props, name);
	}

	public ActorRef deadLetters() {
		return internal.deadLetters();
	}

	public ActorSelection actorSelectionOf(String path) {
		return internal.actorSelection(path);
	}

	public ActorSelection actorSelectionOf(ActorPath path) {
		return internal.actorSelection(path);
	}

}
