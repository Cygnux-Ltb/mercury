package io.mercury.actors.example;

import java.io.IOException;

import akka.actor.typed.ActorSystem;
import io.mercury.actors.example.msg.SayHello;
import io.mercury.common.log.Log4j2Configurator;
import io.mercury.common.log.Log4j2Configurator.LogLevel;

/**
 * @author Akka official
 */
public class AkkaQuickstart {

    static {
        Log4j2Configurator.setLogFilename("actor-test");
        Log4j2Configurator.setLogLevel(LogLevel.INFO);
    }

    public static void main(String[] args) {

        // #actor-system
        final ActorSystem<SayHello> greeterMain = ActorSystem.create(GreeterMain.create(), "hello-akka");
        // #actor-system

        // #main-send-messages
        greeterMain.tell(new SayHello("Charles"));
        // #main-send-messages

        try {
            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            greeterMain.terminate();
        }

    }

}