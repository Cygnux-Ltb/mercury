package io.mercury.common.concurrent.queue.primitive;

import io.mercury.common.concurrent.queue.lowlatency.ConcurrentQueue;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;

/**
 * Created by robaustin on 31/01/2014.
 */
@Ignore
public class ConcurrentBlockingObjectQueueTest {

    @Test
    public void testTake() throws Exception {

        final ConcurrentQueue<Integer> queue = new ConcurrentQueue<>();

        // writer thread
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                queue.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final ArrayBlockingQueue<Integer> actual = new ArrayBlockingQueue<>(1);

        // reader thread
        Executors.newSingleThreadExecutor().execute(() -> {
            final int value;
            try {
                value = queue.take();
                actual.add(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final Integer value = actual.poll(1, TimeUnit.SECONDS);
        Assert.assertEquals((int) value, 1);
        Thread.sleep(100);
    }


    @Test
    public void testWrite() {

    }

    @Test
    public void testRead() throws Exception {
        final ConcurrentQueue<Integer> queue = new ConcurrentQueue<>();
        queue.put(10);
        final int value = queue.take();
        assertEquals(10, value);
    }

    @Test
    public void testRead2() throws Exception {
        final ConcurrentQueue<Integer> queue = new ConcurrentQueue<>();
        queue.put(10);
        queue.put(11);
        final int value = queue.take();
        assertEquals(10, value);
        final int value1 = queue.take();
        assertEquals(11, value1);
    }

    @Test
    public void testReadLoop() throws Exception {
        final ConcurrentQueue<Integer> queue = new ConcurrentQueue<>();

        for (int i = 1; i < 50; i++) {
            queue.put(i);
            final int value = queue.take();
            assertEquals(i, value);
        }
    }

    /**
     * reader and add, reader and writers on different threads
     */
    @Test
    public void testWithFasterReader() throws Exception {

        final ConcurrentQueue<Integer> queue = new ConcurrentQueue<>();
        final int max = 100;
        final CountDownLatch countDown = new CountDownLatch(1);

        final AtomicBoolean success = new AtomicBoolean(true);

        new Thread(() -> {
            for (int i = 1; i < max; i++) {
                try {
                    queue.put(i);
                    Thread.sleep((int) (Math.random() * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            int value = 0;
            for (int i = 1; i < max; i++) {
                try {
                    final int newValue = queue.take();
                    assertEquals(i, newValue);
                    if (newValue != value + 1) {
                        success.set(false);
                        return;
                    }
                    value = newValue;
                    Thread.sleep((int) (Math.random() * 10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countDown.countDown();
        }).start();

        countDown.await();

        Assert.assertTrue(success.get());
    }


    /**
     * faster writer
     */
    @Test
    public void testWithFasterWriter() throws Exception {

        final ConcurrentQueue<Integer> queue = new ConcurrentQueue<>();
        final int max = 200;
        final CountDownLatch countDown = new CountDownLatch(1);
        final AtomicBoolean success = new AtomicBoolean(true);

        new Thread(() -> {
            for (int i = 1; i < max; i++) {
                try {
                    queue.put(i);
                    Thread.sleep((int) (Math.random() * 3));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            int value = 0;
            for (int i = 1; i < max; i++) {
                try {
                    final int newValue = queue.take();
                    assertEquals(i, newValue);
                    if (newValue != value + 1) {
                        success.set(false);
                        return;
                    }
                    value = newValue;
                    Thread.sleep((int) (Math.random() * 10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countDown.countDown();
        }).start();

        countDown.await();
        Assert.assertTrue(success.get());
    }


    @Test
    @Ignore
    public void testFlatOut() throws Exception {
        testConcurrentBlockingObjectQueue(Integer.MAX_VALUE);
    }

    private void testConcurrentBlockingObjectQueue(final int nTimes) throws InterruptedException {
        final ConcurrentQueue<Integer> queue = new ConcurrentQueue<>(1024);
        final CountDownLatch countDown = new CountDownLatch(1);

        final AtomicBoolean success = new AtomicBoolean(true);

        Thread writerThread = new Thread(() -> {
            try {
                for (int i = 1; i < nTimes; i++) {
                    queue.put(i);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });

        writerThread.setName("ConcurrentBlockingObjectQueue<Integer>-writer");

        Thread readerThread = new Thread(() -> {
            int value = 0;
            for (int i = 1; i < nTimes; i++) {
                final int newValue;
                try {
                    newValue = queue.take();
                    if (newValue != value + 1) {
                        success.set(false);
                        return;
                    }
                    value = newValue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countDown.countDown();
        });

        readerThread.setName("ConcurrentBlockingObjectQueue<Integer>-reader");

        writerThread.start();
        readerThread.start();

        countDown.await();

        writerThread.stop();
        readerThread.stop();
    }

    private void testArrayBlockingQueue(final int nTimes) throws InterruptedException {

        final ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1024);
        final CountDownLatch countDown = new CountDownLatch(1);

        final AtomicBoolean success = new AtomicBoolean(true);

        Thread writerThread = new Thread(() -> {
            try {
                for (int i = 1; i < nTimes; i++) {
                    queue.put(i);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });

        writerThread.setName("ArrayBlockingQueue-writer");

        Thread readerThread = new Thread(() -> {
            int value = 0;
            for (int i = 1; i < nTimes; i++) {
                final int newValue;
                try {
                    newValue = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                if (newValue != value + 1) {
                    success.set(false);
                    return;
                }
                value = newValue;
            }
            countDown.countDown();

        });

        readerThread.setName("ArrayBlockingQueue-reader");

        writerThread.start();
        readerThread.start();

        countDown.await();

        writerThread.stop();
        readerThread.stop();
    }


    @Test
    public void testLatency() throws InterruptedException {

        for (int pwr = 2; pwr < 20; pwr++) {
            int i = (int) Math.pow(2, pwr);

            final long arrayBlockingQueueStart = System.nanoTime();
            testArrayBlockingQueue(i);
            final double arrayBlockingDuration = System.nanoTime() - arrayBlockingQueueStart;

            final long queueStart = System.nanoTime();
            testConcurrentBlockingObjectQueue(i);
            final double concurrentBlockingDuration = System.nanoTime() - queueStart;

            System.out.printf("Performing %,d loops, ArrayBlockingQueue() took %.3f ms and calling ConcurrentBlockingObjectQueue<Integer> took %.3f ms on average, ratio=%.1f%n",
                    i, arrayBlockingDuration / 1000000.0, concurrentBlockingDuration / 1000000.0, (double) arrayBlockingDuration / (double) concurrentBlockingDuration);
            /**
             System.out.printf("%d\t%.3f\t%.3f\n",
             i, arrayBlockingDuration / 1000000.0, concurrentBlockingDuration / 1000000.0, (double) arrayBlockingDuration / (double) concurrentBlockingDuration);
             **/
        }


    }
}
