package io.mercury.actors.example;

import java.io.IOException;

import akka.actor.typed.ActorSystem;
import io.mercury.actors.example.msg.SayHello;
import io.mercury.common.log.LogConfigurator;
import io.mercury.common.log.LogConfigurator.LogLevel;

/**
 * 
 * @author Akka official
 *
 */
public class AkkaQuickstart {

	public static void main(String[] args) {

		LogConfigurator.setFilename("actor-test");
		LogConfigurator.setLogLevel(LogLevel.INFO);

		// #actor-system
		final ActorSystem<SayHello> greeterMain = ActorSystem.create(GreeterMain.create(), "hello-akka");
		// #actor-system

		// #main-send-messages
		greeterMain.tell(new SayHello("Charles"));
		// #main-send-messages

		try {
			System.out.println(">>> Press ENTER to exit <<<");
			System.in.read();
		} catch (IOException ignored) {

		} finally {
			greeterMain.terminate();
		}
	}

}