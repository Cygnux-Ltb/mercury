package io.mercury.actors.example;

import org.junit.ClassRule;
import org.junit.Test;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.mercury.actors.example.Greeter.Greeted;

//#definition
public class AkkaQuickstartTest {

	@ClassRule
	public static final TestKitJunitResource TestKit = new TestKitJunitResource();
//#definition

	// #test
	@Test
	public void testGreeterActorSendingOfGreeting() {
		TestProbe<Greeted> testProbe = TestKit.createTestProbe();
		ActorRef<Greeter.Greet> underTest = TestKit.spawn(Greeter.create(), "greeter");
		underTest.tell(new Greeter.Greet("Charles", testProbe.getRef()));
		testProbe.expectMessage(new Greeter.Greeted("Charles", underTest));
	}
	// #test

}