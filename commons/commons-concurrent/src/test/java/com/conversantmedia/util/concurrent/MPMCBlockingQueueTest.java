package com.conversantmedia.util.concurrent;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author John Cairns <jcairns@dotomi.com> Date: 4//25/12 Time: 3:27 PM
 */
public class MPMCBlockingQueueTest {

	final static boolean ALLOW_LONG_RUN = false;

	private ThreadPoolExecutor executor;

	@Before
	public void setup() {
		executor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1024));
	}

	@After
	public void teardown() {
		executor.shutdown();
	}

	@Test
	public void testAsSynchronousQueue() {
		final int cap = 1;
		BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);
		while (dbq.offer(Integer.valueOf(0)))
			;

		Assert.assertFalse(dbq.offer(Integer.valueOf(10)));

		Assert.assertEquals(2, dbq.size());

		Assert.assertEquals(Integer.valueOf(0), dbq.poll());
	}

	@Test
	public void testDisruptorBlockingQueueTestC1() {
		final int cap = 10;
		BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);
		while (dbq.offer(Integer.valueOf(0)))
			;
		Assert.assertEquals(16, dbq.size());
	}

	@Test
	public void testMPMCBlockingQueueTestC2() {

		final int cap = 50;

		Set<Integer> x = new HashSet<Integer>(cap);
		for (int i = 0; i < 2 * cap; i++) {
			x.add(Integer.valueOf(i));
		}

		BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap, x);
		// next power of two
		Assert.assertEquals(64, dbq.size());
	}

	@Test
	public void testOffer() {

		final int cap = 16;
		BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);
		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
		}

		Assert.assertFalse(dbq.offer(Integer.valueOf(cap)));

		for (int i = 0; i < cap; i++) {
			Assert.assertEquals(Integer.valueOf(i), dbq.poll());
		}

	}

	@Test
	public void remove() {

		final int cap = 10;
		BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);
		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
		}

		Integer i = dbq.peek();
		Integer x = dbq.remove();

		Assert.assertEquals(i, x);
		Assert.assertEquals(i, Integer.valueOf(0));
		Assert.assertFalse(i.equals(dbq.peek()));
	}

	@Test
	public void testPoll() {
		final int cap = 10;
		BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		Assert.assertNull(dbq.poll());

		dbq.offer(Integer.valueOf(1));
		dbq.offer(Integer.valueOf(2));
		Assert.assertEquals(dbq.poll(), Integer.valueOf(1));
		Assert.assertEquals(dbq.poll(), Integer.valueOf(2));

		Assert.assertNull(dbq.poll());
	}

	@Test
	public void inOutIn() {
		final int cap = 8;
		BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		Assert.assertNull(dbq.poll());

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < cap; j++) {
				dbq.offer(j);
			}

			Assert.assertFalse(dbq.offer(1000));

			for (int j = 0; j < cap; j++) {
				Assert.assertEquals(Integer.valueOf(j), dbq.poll());
			}

			Assert.assertNull(dbq.poll());
		}
	}

	@Test
	public void testElement() {
		final int cap = 10;
		BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		try {
			dbq.element();
			Assert.fail();
		} catch (NoSuchElementException ex) {
			// expected
		}
	}

	@Test
	public void testPeek() {
		final int cap = 10;
		BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		try {

			Assert.assertNull(dbq.peek());

		} catch (NoSuchElementException nsex) {
			Assert.fail();
		}

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
			Assert.assertEquals(Integer.valueOf(0), dbq.peek());
		}

		for (int i = 0; i < cap; i++) {
			Assert.assertEquals(Integer.valueOf(i), dbq.peek());
			dbq.poll(); // count up values checking peeks
		}
	}

	@Test
	public void testPut() throws InterruptedException {

		final int cap = 10;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
		}

		executor.execute(() -> {
			try {
				Thread.sleep(1000);
				// after a second remove one
				dbq.poll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		// in main thread add one
		// this operation must wait for thread
		dbq.put(Integer.valueOf(cap));

		boolean hasValCap = false;
		while (!dbq.isEmpty()) {
			if (dbq.poll().equals(Integer.valueOf(cap)))
				hasValCap = true;
		}
		Assert.assertTrue(hasValCap);

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveAllIsNotSupported() {

		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<Integer>(cap);

		dbq.removeAll(Collections.emptySet());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveIsNotSupported() {
		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<Integer>(cap);

		dbq.remove(0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRetainAllIsNotSupported() {
		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<Integer>(cap);

		dbq.retainAll(Collections.emptySet());
	}

	@Ignore // FIXME - this test flickers in parallel test runner
	public void testTimeOffer() throws InterruptedException {

		final int cap = 16;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
		}

		executor.execute(() -> {
			try {
				Thread.sleep(1500);
				// after a second remove one
				dbq.poll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		// expect to fail for only 50 ms
		Assert.assertFalse(dbq.offer(Integer.valueOf(cap), 50, TimeUnit.MILLISECONDS));

		Assert.assertTrue(dbq.offer(Integer.valueOf(cap), 16550, TimeUnit.MILLISECONDS));

		boolean hasValCap = false;
		while (!dbq.isEmpty()) {
			if (dbq.poll().equals(Integer.valueOf(cap)))
				hasValCap = true;
		}
		Assert.assertTrue(hasValCap);
	}

	@Ignore // timing test not suitble for build
	public void pollTimeIsAccurate() throws InterruptedException {
		final MPMCBlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(256);

		final long startTime = System.nanoTime();

		for (int i = 0; i < 50; i++) {
			dbq.poll(100, TimeUnit.MICROSECONDS);
		}

		final long runTime = System.nanoTime() - startTime;

		final long expTime = 50 * 100 * 1000;
		Assert.assertTrue(runTime >= expTime / 2);

		Assert.assertTrue(runTime <= expTime * 2);
	}

	@Ignore // timing test are not suitable for build
	public void offerTimeIsAccurate() throws InterruptedException {
		final MPMCBlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(256);

		for (int i = 0; i < 256; i++) {
			dbq.offer(1);
		}

		final long startTime = System.nanoTime();

		for (int i = 0; i < 50; i++) {
			dbq.offer(1, 100, TimeUnit.MICROSECONDS);
		}

		final long runTime = System.nanoTime() - startTime;

		final long expTime = 50 * 100 * 1000;
		Assert.assertTrue(runTime >= expTime / 2);
		Assert.assertTrue(runTime <= expTime * 2);

	}

	@Test
	public void testTake() throws InterruptedException {

		final int cap = 10;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		executor.execute(() -> {
			try {
				Thread.sleep(1000);
				// after a second remove one
				dbq.offer(Integer.valueOf(cap));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		// wait for value to be added
		Assert.assertEquals(Integer.valueOf(cap), dbq.take());
	}

	@Test
	public void testTimePoll() throws InterruptedException {
		final int cap = 10;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		executor.execute(() -> {
			try {
				Thread.sleep(1000);
				// after a second remove one
				dbq.offer(Integer.valueOf(cap));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		// wait for value to be added
		Assert.assertNull(dbq.poll(50, TimeUnit.MICROSECONDS));
		Assert.assertEquals(Integer.valueOf(cap), dbq.poll(50, TimeUnit.SECONDS));
	}

	@Test
	public void testRemainingCapacity() {
		final int cap = 128;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {
			Assert.assertEquals(cap - i, dbq.remainingCapacity());
			dbq.offer(Integer.valueOf(i));
		}

	}

	@Test
	public void testDrainToC() {
		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
		}

		final List<Integer> c = new LinkedList<>();
		Assert.assertEquals(dbq.drainTo(c), cap);
		int i = 0;
		for (final Integer a : c) {
			Assert.assertEquals(a, Integer.valueOf(i++));
		}

	}

	@Test
	public void drainToToCMax() {

		final int cap = 100;
		final int max = 75;
		final BlockingQueue<Integer> dbq = new DisruptorBlockingQueue<Integer>(cap);

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
		}

		final List<Integer> c = new LinkedList<>();
		Assert.assertEquals(dbq.drainTo(c, max), max);
		Assert.assertEquals(c.size(), max);
		int i = 0;
		for (final Integer a : c) {
			Assert.assertEquals(a, Integer.valueOf(i++));
		}
	}

	@Test
	public void testSize() {
		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		Assert.assertEquals(0, dbq.size());

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
			Assert.assertEquals(i + 1, dbq.size());
		}

		Assert.assertEquals(cap, dbq.size());

		for (int i = 0; i < cap; i++) {
			Assert.assertEquals(dbq.size(), cap - i);
			dbq.poll();
		}

		Assert.assertEquals(0, dbq.size());

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
			Assert.assertEquals(i + 1, dbq.size());
		}

		Assert.assertEquals(cap, dbq.size());

		for (int i = 0; i < cap; i++) {
			Assert.assertEquals(dbq.size(), cap - i);
			dbq.poll();
		}

	}

	@Test
	public void testIsEmpty() {
		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		Assert.assertTrue(dbq.isEmpty());

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
			Assert.assertFalse(dbq.isEmpty());
		}

		for (int i = 0; i < cap; i++) {
			Assert.assertFalse(dbq.isEmpty());
			dbq.poll();
		}

		Assert.assertTrue(dbq.isEmpty());

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
			Assert.assertFalse(dbq.isEmpty());
		}

		for (int i = 0; i < cap; i++) {
			Assert.assertFalse(dbq.isEmpty());
			dbq.poll();
		}

		Assert.assertTrue(dbq.isEmpty());
	}

	@Test
	public void testContains() {

		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {
			Assert.assertFalse(dbq.contains(Integer.valueOf(i)));
		}

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
		}

		for (int i = 0; i < cap; i++) {
			Assert.assertTrue(dbq.contains(Integer.valueOf(i)));
		}

		for (int i = cap; i < 2 * cap; i++) {
			Assert.assertFalse(dbq.contains(Integer.valueOf(i)));
		}
	}

	@Test
	public void testToArray() {

		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {
			Assert.assertTrue(dbq.offer(Integer.valueOf(i)));
		}

		Object[] objArray = dbq.toArray();
		for (int i = 0; i < cap; i++) {
			Assert.assertEquals(Integer.valueOf(i), objArray[i]);
		}
	}

	@Test
	public void testAdd() {
		final int cap = 16;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {

			dbq.add(Integer.valueOf(i));
		}

		try {
			dbq.add(Integer.valueOf(cap));
			Assert.fail();
		} catch (IllegalStateException ex) {
			// expected;
		}
	}

	@Test
	public void testContainsAll() {

		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {

			dbq.offer(Integer.valueOf(i));
		}

		Set<Integer> si = new HashSet<>(10);
		for (int i = 0; i < cap / 10; i++) {
			si.add(Integer.valueOf(i));
		}
		Assert.assertTrue(dbq.containsAll(si));

		si.add(Integer.valueOf(-1));
		Assert.assertFalse(dbq.containsAll(si));
		si.remove(-1);
		dbq.clear();
		Assert.assertFalse(dbq.containsAll(si));
	}

	@Test
	public void testAddAll() {

		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		Set<Integer> si = new HashSet<>(cap);
		for (int i = 0; i < cap / 10; i++) {
			si.add(Integer.valueOf(i));
		}
		dbq.addAll(si);
		Assert.assertTrue(dbq.containsAll(si));

		Set<Integer> ni = new HashSet<>(cap);
		for (int i = 0; i < cap / 10; i++) {
			ni.add(Integer.valueOf(-i));
		}
		dbq.addAll(ni);
		Assert.assertTrue(dbq.containsAll(si));
		Assert.assertTrue(dbq.containsAll(ni));

		for (int i = 2 * cap / 10; i < 2 * cap; i++) {
			si.add(Integer.valueOf(i));
		}
		dbq.addAll(si);
		Assert.assertEquals(dbq.size(), 128);
	}

	@Test
	public void testClear() {
		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
		}

		Set<Integer> si = new HashSet<>(cap);
		for (int i = 0; i < cap / 10; i++) {
			si.add(Integer.valueOf(i));
		}

		Assert.assertTrue(dbq.containsAll(si));
		dbq.clear();
		Assert.assertFalse(dbq.containsAll(si));
		Assert.assertEquals(0, dbq.size());
		Assert.assertTrue(dbq.isEmpty());
		Assert.assertNull(dbq.poll());
	}

	@Test
	public void testIterator() {
		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
		}

		int i = 0;
		for (final Integer c : dbq) {
			Assert.assertEquals(Integer.valueOf(i++), c);
		}
	}

	@Test
	public void testTypeToArray() {
		final int cap = 100;
		final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

		for (int i = 0; i < cap; i++) {
			dbq.offer(Integer.valueOf(i));
		}

		Integer[] t = new Integer[cap];
		dbq.toArray(t);
		for (int i = 0; i < cap; i++) {
			Assert.assertEquals(Integer.valueOf(i), t[i]);
		}
	}

	@Test
	public void textIntMaxValue() {

		// the blocking queue depends on sequence numbers that are integers
		// be sure the blocking queue operates normally over
		// a range spanning integer values

		if (ALLOW_LONG_RUN) {
			final int cap = 3;
			final BlockingQueue<Integer> dbq = new MPMCBlockingQueue<>(cap);

			long nIter = 0;

			for (int i = 0; i < Integer.MAX_VALUE; i++) {

				for (int a = 0; a < cap; a++) {
					Assert.assertEquals(dbq.size(), a);
					dbq.offer(Integer.valueOf(a));
					nIter++;
				}

				for (int a = 0; a < cap; a++) {
					Assert.assertEquals(dbq.size(), cap - a);
					Assert.assertEquals("At i=" + i, dbq.poll(), Integer.valueOf(a));
				}

				if (nIter % Integer.MAX_VALUE == 0)
					System.out.println(nIter + "times MAX_VALUE");

			}
		} else {
			System.out.println("max value test not executed");
		}
	}
}
