/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.lightbend.akkasample.sample2;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.lightbend.akkasample.StdIn;

/**
 * actor that changes behavior
 */
public class App {

    static class Alarm extends AbstractLoggingActor {
        // protocol
        static class Activity {
        }

        static class Disable {
            private final String password;

            public Disable(String password) {
                this.password = password;
            }
        }

        static class Enable {
            private final String password;

            public Enable(String password) {
                this.password = password;
            }
        }

        private final String password;
        private Receive enabled;
        private Receive disabled;

        public Alarm(String password) {
            this.password = password;
        }

        private void onEnable(Enable enable) {
            if (password.equals(enable.password)) {
                log().info("Alarm enable");
                getContext().become(enabled);
            } else {
                log().info("Someone failed to enable the alarm");
            }
        }

        private void onDisable(Disable disable) {
            if (password.equals(disable.password)) {
                log().info("Alarm disabled");
                getContext().become(disabled);
            } else {
                log().warning("Someone who didn't know the password tried to disable it");
            }
        }

        private void onActivity(Activity ignored) {
            log().warning("alarm!!!");
        }

        public static Props props(String password) {
            return Props.create(Alarm.class, password);
        }

        @Override
        public Receive createReceive() {
            enabled = receiveBuilder()
                    .match(Activity.class, this::onActivity)
                    .match(Disable.class, this::onDisable)
                    .build();
            disabled = receiveBuilder()
                    .match(Enable.class, this::onEnable)
                    .build();
            return null;
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();

        final ActorRef alarm = system.actorOf(Alarm.props("cat"), "alarm");

        alarm.tell(new Alarm.Activity(), ActorRef.noSender());
        alarm.tell(new Alarm.Enable("dogs"), ActorRef.noSender());
        alarm.tell(new Alarm.Enable("cat"), ActorRef.noSender());
        alarm.tell(new Alarm.Activity(), ActorRef.noSender());
        alarm.tell(new Alarm.Disable("dogs"), ActorRef.noSender());
        alarm.tell(new Alarm.Disable("cat"), ActorRef.noSender());
        alarm.tell(new Alarm.Activity(), ActorRef.noSender());

        System.out.println("ENTER to terminate");
        StdIn.readLine();
        system.terminate();
    }
}
