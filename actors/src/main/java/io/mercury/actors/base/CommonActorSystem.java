package io.mercury.actors.base;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.mercury.common.thread.ShutdownHooks;
import io.mercury.common.thread.SleepSupport;
import scala.concurrent.Future;

public final class CommonActorSystem {

	private final ActorSystem actorSystem;

	private final LoggingAdapter logger;

	public static final CommonActorSystem newSystem(String name) {
		return new CommonActorSystem(name);
	}

	private CommonActorSystem(String name) {
		this.actorSystem = ActorSystem.create(name);
		this.logger = Logging.getLogger(actorSystem, this);
		// Add ShutdownHook
		ShutdownHooks.addShutdownHook("ActorSystemTerminateThread", this::terminateActorSystem);
	}

	private void terminateActorSystem() {
		logger.info("ActorSystem {} is terminated...", actorSystem.name());
		Future<Terminated> terminate = actorSystem.terminate();
		while (!terminate.isCompleted())
			SleepSupport.sleep(100);
	}

	public ActorSystem get() {
		return actorSystem;
	}

	public String name() {
		return actorSystem.name();
	}

	public ActorRef actorOf(Props props) {
		return actorSystem.actorOf(props);
	}

	public ActorRef actorOf(Props props, String name) {
		return actorSystem.actorOf(props, name);
	}

	public ActorRef deadLetters() {
		return actorSystem.deadLetters();
	}

	public ActorSelection actorSelectionOf(String path) {
		return actorSystem.actorSelection(path);
	}

	public ActorSelection actorSelectionOf(ActorPath path) {
		return actorSystem.actorSelection(path);
	}

}
