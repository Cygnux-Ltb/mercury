package io.mercury.actors.sample.supervision;

import static akka.japi.Util.classTag;
import static akka.pattern.Patterns.ask;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import io.mercury.actors.sample.supervision.Expression.Add;
import io.mercury.actors.sample.supervision.Expression.Const;
import io.mercury.actors.sample.supervision.Expression.Divide;
import io.mercury.actors.sample.supervision.Expression.Multiply;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public class Main {

	public static void main(String[] args) throws Exception {
		ActorSystem system = ActorSystem.create("calculator-system");
		ActorRef calculatorService = system.actorOf(Props.create(ArithmeticService.class), "arithmetic-service");

		// (3 + 5) / (2 * (1 + 1))
		Expression task = new Divide(new Add(new Const(3), new Const(5)),
				new Multiply(new Const(2), new Add(new Const(1), new Const(1))));

		FiniteDuration duration = Duration.create(1, TimeUnit.SECONDS);
		Integer result = Await
				.result(ask(calculatorService, task, new Timeout(duration)).mapTo(classTag(Integer.class)), duration);
		System.out.println("Got result: " + result);

		Await.ready(system.terminate(), Duration.Inf());
	}
}
