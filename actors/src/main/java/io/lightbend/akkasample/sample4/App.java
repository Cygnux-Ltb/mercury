/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.lightbend.akkasample.sample4;

import com.lightbend.akkasample.StdIn;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

// let's combine a bunch of concepts
public class App {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();

		ActorRef db = system.actorOf(DbSupervisor.props(), "db");

		system.actorOf(WebServer.props(db, "localhost", 8080), "webserver");

		System.out.println("ENTER to terminate");
		StdIn.readLine();
		system.terminate();
	}
}
