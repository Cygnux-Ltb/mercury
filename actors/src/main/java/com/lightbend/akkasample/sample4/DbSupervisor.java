/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.lightbend.akkasample.sample4;

import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;

import java.util.ArrayList;
import java.util.List;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import scala.concurrent.duration.Duration;

public class DbSupervisor extends AbstractLoggingActor {

	private final SupervisorStrategy strategy = new OneForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder
			// this requires a new connection
			.match(SynchronousDatabaseConnection.ConnectionLost.class, e -> restart())
			// but this just failed the current request
			.match(SynchronousDatabaseConnection.RequestTimedOut.class, e -> resume()).build());

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	public static Props props() {
		return Props.create(DbSupervisor.class);
	}

	@Override
	public Receive createReceive() {
		final Props connectionProps = DbActor.props()
				// make sure we run it on a separate dispatcher since it is blocking
				.withDispatcher("akkasample.blocking-dispatcher");

		// create a router and 5 actors, each handling a single
		// database connection
		Router router;
		{
			List<Routee> routees = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				ActorRef r = getContext().actorOf(connectionProps);
				getContext().watch(r);
				routees.add(new ActorRefRoutee(r));
			}
			router = new Router(new RoundRobinRoutingLogic(), routees);
		}
		return receiveBuilder()
				// just forward the db-actor query to one of the children through the router
				.match(DbActor.GetProduct.class, request -> router.route(request, sender())).build();
	}
}
