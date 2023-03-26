package com.lightbend.akkasample.sample4;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.Optional;

public class DbActor extends AbstractActor {

    private final SynchronousDatabaseConnection connection = new SynchronousDatabaseConnection();

    private void getProduct(long id) {
        final Optional<Product> product = connection.findProduct(id);
        sender().tell(new ProductResult(product), self());
    }

    public static Props props() {
        return Props.create(DbActor.class);
    }

    // protocol
    public record GetProduct(
            long id) {
    }

    public record ProductResult(
            Optional<Product> product) {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GetProduct.class, query -> getProduct(query.id))
                .build();
    }

}
