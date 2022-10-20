package com.lightbend.akkasample.sample4;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * A dummy database that is slow and blocking and occasionally "looses the
 * connection". Not thread-safe, not meant to be shared between actors.
 */
public class SynchronousDatabaseConnection {

    public static class ConnectionLost extends RuntimeException {
        @Serial
        private static final long serialVersionUID = 23808501218949286L;

        public ConnectionLost() {
            super("Database connection lost");
        }
    }

    public static class RequestTimedOut extends RuntimeException {
        @Serial
        private static final long serialVersionUID = -2105302329668141685L;

        public RequestTimedOut() {
            super("Database request timed out");
        }
    }

    private Map<Long, Product> inMemoryDb;
    private final Random failureRandom = new Random();

    public SynchronousDatabaseConnection() {
        inMemoryDb = new HashMap<>();
        inMemoryDb.put(1L, new Product(1, "Clean Socks", "Socks for both feet, that are clean"));
        inMemoryDb.put(2L, new Product(2, "Hat", "A thing you wear on your head"));
        inMemoryDb.put(3L, new Product(3, "Gloves", "Keeps your hands warm on cold days"));
    }

    public Optional<Product> findProduct(long id) {
        // simulate a slow blocking database where the queries takes
        // between 200 and 700 ms
        try {
            Thread.sleep(failureRandom.nextInt(500) + 200);
        } catch (InterruptedException ex) {
            // nothing to do here as it is dummy code
        }

        final int random = failureRandom.nextInt(10);
        if (random == 0) {
            // additionally, 1 out of 10 times it fails irrecoverably
            inMemoryDb = null;
            throw new ConnectionLost();
        } else if (random > 6) {
            // and 3 out of 10 requests it fails but not irrecoverably
            throw new RequestTimedOut();
        }

        return Optional.ofNullable(inMemoryDb.get(id));
    }
}
