package io.mercury.common.concurrent.queue.primitive;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Ignore;

import io.mercury.common.concurrent.queue.lowlatency.ConcurrentBlockingQueue;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Ignore
public class ArrayBlockingQueueTest extends JSR166TestCase {

	public static class Fair extends BlockingQueueTest {
		protected BlockingQueue emptyCollection() {
			return new ConcurrentBlockingQueue(SIZE, true);
		}
	}

	public static class NonFair extends BlockingQueueTest {
		protected BlockingQueue emptyCollection() {
			return new ConcurrentBlockingQueue(SIZE, false);
		}
	}

	/**
	 * Returns a new queue of given size containing consecutive Integers 0 ... n.
	 */
	@SuppressWarnings("deprecation")
	private ConcurrentBlockingQueue<Integer> populatedQueue(int n) {
		ConcurrentBlockingQueue<Integer> q = new ConcurrentBlockingQueue<Integer>(n);
		assertTrue(q.isEmpty());
		for (int i = 0; i < n; i++)
			assertTrue(q.offer(new Integer(i)));
		assertFalse(q.isEmpty());
		assertEquals(0, q.remainingCapacity());
		assertEquals(n, q.size());
		return q;
	}

	/**
	 * A new queue has the indicated capacity
	 */
	public void testConstructor1() {
		assertEquals(SIZE, new ConcurrentBlockingQueue(SIZE).remainingCapacity());
	}

	/**
	 * Constructor throws IAE if capacity argument nonpositive
	 */
	public void testConstructor2() {
		try {
			new ConcurrentBlockingQueue(0);
			shouldThrow();
		} catch (IllegalArgumentException success) {
		}
	}

	/**
	 * Initializing from null Collection throws NPE
	 */
	public void testConstructor3() {
		try {
			new ConcurrentBlockingQueue(1, true, null);
			shouldThrow();
		} catch (NullPointerException success) {
		}
	}

	/**
	 * Initializing from Collection of null elements throws NPE
	 */
	public void testConstructor4() {
		Collection<Integer> elements = Arrays.asList(new Integer[SIZE]);
		try {
			new ConcurrentBlockingQueue(SIZE, false, elements);
			shouldThrow();
		} catch (NullPointerException success) {
		}
	}

	/**
	 * Initializing from Collection with some null elements throws NPE
	 */
	public void testConstructor5() {
		Integer[] ints = new Integer[SIZE];
		for (int i = 0; i < SIZE - 1; ++i)
			ints[i] = i;
		@SuppressWarnings("unused")
		Collection<Integer> elements = Arrays.asList(ints);
		try {
			new ConcurrentBlockingQueue(SIZE, false, Arrays.asList(ints));
			shouldThrow();
		} catch (NullPointerException success) {
		}
	}

	/**
	 * Initializing from too large collection throws IAE
	 */
	public void testConstructor6() {
		Integer[] ints = new Integer[SIZE];
		for (int i = 0; i < SIZE; ++i)
			ints[i] = i;
		Collection<Integer> elements = Arrays.asList(ints);
		try {
			new ConcurrentBlockingQueue(SIZE - 1, false, elements);
			shouldThrow();
		} catch (IllegalArgumentException success) {
		}
	}

	/**
	 * Queue contains all elements of collection used to initialize
	 */
	public void testConstructor7() {
		Integer[] ints = new Integer[SIZE];
		for (int i = 0; i < SIZE; ++i)
			ints[i] = i;
		Collection<Integer> elements = Arrays.asList(ints);
		ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(SIZE, true, elements);
		for (int i = 0; i < SIZE; ++i)
			assertEquals(ints[i], q.poll());
	}

	/**
	 * Queue transitions from empty to full when elements added
	 */
	public void testEmptyFull() {
		ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(2);
		assertTrue(q.isEmpty());
		assertEquals(2, q.remainingCapacity());
		q.add(one);
		assertFalse(q.isEmpty());
		q.add(two);
		assertFalse(q.isEmpty());
		assertEquals(0, q.remainingCapacity());
		assertFalse(q.offer(three));
	}

	/**
	 * remainingCapacity decreases on add, increases on remove
	 */
	public void testRemainingCapacity() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			assertEquals(i, q.remainingCapacity());
			assertEquals(SIZE - i, q.size());
			q.remove();
		}
		for (int i = 0; i < SIZE; ++i) {
			assertEquals(SIZE - i, q.remainingCapacity());
			assertEquals(i, q.size());
			//q.add(new Integer(i));
			q.add((i));
		}
	}

	/**
	 * Offer succeeds if not full; fails if full
	 */
	public void testOffer() {
		ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(1);
		assertTrue(q.offer(zero));
		assertFalse(q.offer(one));
	}

	/**
	 * add succeeds if not full; throws ISE if full
	 */
	@SuppressWarnings("deprecation")
	public void testAdd() {
		try {
			ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(SIZE);
			for (int i = 0; i < SIZE; ++i) {
				assertTrue(q.add(new Integer(i)));
			}
			assertEquals(0, q.remainingCapacity());
			q.add(new Integer(SIZE));
			shouldThrow();
		} catch (IllegalStateException success) {
		}
	}

	/**
	 * addAll(this) throws IAE
	 */
	public void testAddAllSelf() {
		try {
			ConcurrentBlockingQueue q = populatedQueue(SIZE);
			q.addAll(q);
			shouldThrow();
		} catch (IllegalArgumentException success) {
		}
	}

	/**
	 * addAll of a collection with any null elements throws NPE after possibly
	 * adding some elements
	 */
	@SuppressWarnings("deprecation")
	public void testAddAll3() {
		try {
			ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(SIZE);
			Integer[] ints = new Integer[SIZE];
			for (int i = 0; i < SIZE - 1; ++i)
				ints[i] = new Integer(i);
			q.addAll(Arrays.asList(ints));
			shouldThrow();
		} catch (NullPointerException success) {
		}
	}

	/**
	 * addAll throws ISE if not enough room
	 */
	@SuppressWarnings("deprecation")
	public void testAddAll4() {
		try {
			ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(1);
			Integer[] ints = new Integer[SIZE];
			for (int i = 0; i < SIZE; ++i)
				ints[i] = new Integer(i);
			q.addAll(Arrays.asList(ints));
			shouldThrow();
		} catch (IllegalStateException success) {
		}
	}

	/**
	 * Queue contains all elements, in traversal order, of successful addAll
	 */
	@SuppressWarnings("deprecation")
	public void testAddAll5() {
		Integer[] empty = new Integer[0];
		Integer[] ints = new Integer[SIZE];
		for (int i = 0; i < SIZE; ++i)
			ints[i] = new Integer(i);
		ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(SIZE);
		assertFalse(q.addAll(Arrays.asList(empty)));
		assertTrue(q.addAll(Arrays.asList(ints)));
		for (int i = 0; i < SIZE; ++i)
			assertEquals(ints[i], q.poll());
	}

	/**
	 * all elements successfully put are contained
	 */
	@SuppressWarnings("deprecation")
	public void testPut() throws InterruptedException {
		ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			Integer I = new Integer(i);
			q.put(I);
			assertTrue(q.contains(I));
		}
		assertEquals(0, q.remainingCapacity());
	}

	/**
	 * put blocks interruptibly if full
	 */
	public void testBlockingPut() throws InterruptedException {
		final ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(SIZE);
		final CountDownLatch pleaseInterrupt = new CountDownLatch(1);
		Thread t = newStartedThread(new CheckedRunnable() {
			public void realRun() throws InterruptedException {
				for (int i = 0; i < SIZE; ++i)
					q.put(i);
				assertEquals(SIZE, q.size());
				assertEquals(0, q.remainingCapacity());

				Thread.currentThread().interrupt();
				try {
					q.put(99);
					shouldThrow();
				} catch (InterruptedException success) {
				}
				assertFalse(Thread.interrupted());

				pleaseInterrupt.countDown();
				try {
					q.put(99);
					shouldThrow();
				} catch (InterruptedException success) {
				}
				assertFalse(Thread.interrupted());
			}
		});

		await(pleaseInterrupt);
		assertThreadStaysAlive(t);
		t.interrupt();
		awaitTermination(t);
		assertEquals(SIZE, q.size());
		assertEquals(0, q.remainingCapacity());
	}

	/**
	 * put blocks interruptibly waiting for take when full
	 */
	public void testPutWithTake() throws InterruptedException {
		final int capacity = 2;
		final ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(capacity);
		final CountDownLatch pleaseTake = new CountDownLatch(1);
		final CountDownLatch pleaseInterrupt = new CountDownLatch(1);
		Thread t = newStartedThread(new CheckedRunnable() {
			public void realRun() throws InterruptedException {
				for (int i = 0; i < capacity; i++)
					q.put(i);
				pleaseTake.countDown();
				q.put(86);

				pleaseInterrupt.countDown();
				try {
					q.put(99);
					shouldThrow();
				} catch (InterruptedException success) {
				}
				assertFalse(Thread.interrupted());
			}
		});

		await(pleaseTake);
		assertEquals(0, q.remainingCapacity());
		assertEquals(0, q.take());

		await(pleaseInterrupt);
		assertThreadStaysAlive(t);
		t.interrupt();
		awaitTermination(t);
		assertEquals(0, q.remainingCapacity());
	}

	/**
	 * timed offer times out if full and elements not taken
	 */
	public void testTimedOffer() throws InterruptedException {
		final ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(2);
		final CountDownLatch pleaseInterrupt = new CountDownLatch(1);
		Thread t = newStartedThread(new CheckedRunnable() {
			public void realRun() throws InterruptedException {
				q.put(new Object());
				q.put(new Object());
				long startTime = System.nanoTime();
				assertFalse(q.offer(new Object(), timeoutMillis(), MILLISECONDS));
				assertTrue(millisElapsedSince(startTime) >= timeoutMillis());
				pleaseInterrupt.countDown();
				try {
					q.offer(new Object(), 2 * LONG_DELAY_MS, MILLISECONDS);
					shouldThrow();
				} catch (InterruptedException success) {
				}
			}
		});

		await(pleaseInterrupt);
		assertThreadStaysAlive(t);
		t.interrupt();
		awaitTermination(t);
	}

	/**
	 * take retrieves elements in FIFO order
	 */
	public void testTake() throws InterruptedException {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			assertEquals(i, q.take());
		}
	}

	/**
	 * Take removes existing elements until empty, then blocks interruptibly
	 */
	public void testBlockingTake() throws InterruptedException {
		final ConcurrentBlockingQueue q = populatedQueue(SIZE);
		final CountDownLatch pleaseInterrupt = new CountDownLatch(1);
		Thread t = newStartedThread(new CheckedRunnable() {
			public void realRun() throws InterruptedException {
				for (int i = 0; i < SIZE; ++i) {
					assertEquals(i, q.take());
				}

				Thread.currentThread().interrupt();
				try {
					q.take();
					shouldThrow();
				} catch (InterruptedException success) {
				}
				assertFalse(Thread.interrupted());

				pleaseInterrupt.countDown();
				try {
					q.take();
					shouldThrow();
				} catch (InterruptedException success) {
				}
				assertFalse(Thread.interrupted());
			}
		});

		await(pleaseInterrupt);
		assertThreadStaysAlive(t);
		t.interrupt();
		awaitTermination(t);
	}

	/**
	 * poll succeeds unless empty
	 */
	public void testPoll() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			assertEquals(i, q.poll());
		}
		assertNull(q.poll());
	}

	/**
	 * timed poll with zero timeout succeeds when non-empty, else times out
	 */
	public void testTimedPoll0() throws InterruptedException {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			assertEquals(i, q.poll(0, MILLISECONDS));
		}
		assertNull(q.poll(0, MILLISECONDS));
		checkEmpty(q);
	}

	/**
	 * timed poll with nonzero timeout succeeds when non-empty, else times out
	 */
	public void testTimedPoll() throws InterruptedException {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			long startTime = System.nanoTime();
			assertEquals(i, q.poll(LONG_DELAY_MS, MILLISECONDS));
			assertTrue(millisElapsedSince(startTime) < LONG_DELAY_MS);
		}
		long startTime = System.nanoTime();
		assertNull(q.poll(timeoutMillis(), MILLISECONDS));
		assertTrue(millisElapsedSince(startTime) >= timeoutMillis());
		checkEmpty(q);
	}

	/**
	 * Interrupted timed poll throws InterruptedException instead of returning
	 * timeout status
	 */
	public void testInterruptedTimedPoll() throws InterruptedException {
		final BlockingQueue<Integer> q = populatedQueue(SIZE);
		final CountDownLatch aboutToWait = new CountDownLatch(1);
		Thread t = newStartedThread(new CheckedRunnable() {
			public void realRun() throws InterruptedException {
				for (int i = 0; i < SIZE; ++i) {
					long t0 = System.nanoTime();
					assertEquals(i, (int) q.poll(LONG_DELAY_MS, MILLISECONDS));
					assertTrue(millisElapsedSince(t0) < SMALL_DELAY_MS);
				}
				long t0 = System.nanoTime();
				aboutToWait.countDown();
				try {
					q.poll(MEDIUM_DELAY_MS, MILLISECONDS);
					shouldThrow();
				} catch (InterruptedException success) {
					assertTrue(millisElapsedSince(t0) < MEDIUM_DELAY_MS);
				}
			}
		});

		aboutToWait.await();
		waitForThreadToEnterWaitState(t, SMALL_DELAY_MS);
		t.interrupt();
		awaitTermination(t, MEDIUM_DELAY_MS);
		checkEmpty(q);
	}

	/**
	 * peek returns next element, or null if empty
	 */
	public void testPeek() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			assertEquals(i, q.peek());
			assertEquals(i, q.poll());
			assertTrue(q.peek() == null || !q.peek().equals(i));
		}
		assertNull(q.peek());
	}

	/**
	 * element returns next element, or throws NSEE if empty
	 */
	public void testElement() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			assertEquals(i, q.element());
			assertEquals(i, q.poll());
		}
		try {
			q.element();
			shouldThrow();
		} catch (NoSuchElementException success) {
		}
	}

	/**
	 * remove removes next element, or throws NSEE if empty
	 */
	public void testRemove() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			assertEquals(i, q.remove());
		}
		try {
			q.remove();
			shouldThrow();
		} catch (NoSuchElementException success) {
		}
	}

	/**
	 * contains(x) reports true when elements added but not yet removed
	 */
	@SuppressWarnings("deprecation")
	public void testContains() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			assertTrue(q.contains(new Integer(i)));
			assertEquals(i, q.poll());
			assertFalse(q.contains(new Integer(i)));
		}
	}

	/**
	 * clear removes all elements
	 */
	public void testClear() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		q.clear();
		assertTrue(q.isEmpty());
		assertEquals(0, q.size());
		assertEquals(SIZE, q.remainingCapacity());
		q.add(one);
		assertFalse(q.isEmpty());
		assertTrue(q.contains(one));
		q.clear();
		assertTrue(q.isEmpty());
	}

	/**
	 * containsAll(c) is true when c contains a subset of elements
	 */
	@SuppressWarnings("deprecation")
	public void testContainsAll() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		ConcurrentBlockingQueue p = new ConcurrentBlockingQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			assertTrue(q.containsAll(p));
			assertFalse(p.containsAll(q));
			p.add(new Integer(i));
		}
		assertTrue(p.containsAll(q));
	}

	/**
	 * retainAll(c) retains only those elements of c and reports true if changed
	 */
	public void testRetainAll() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		ConcurrentBlockingQueue p = populatedQueue(SIZE);
		for (int i = 0; i < SIZE; ++i) {
			boolean changed = q.retainAll(p);
			if (i == 0)
				assertFalse(changed);
			else
				assertTrue(changed);

			assertTrue(q.containsAll(p));
			assertEquals(SIZE - i, q.size());
			p.remove();
		}
	}

	/**
	 * removeAll(c) removes only those elements of c and reports true if changed
	 */
	public void testRemoveAll() {
		for (int i = 1; i < SIZE; ++i) {
			ConcurrentBlockingQueue q = populatedQueue(SIZE);
			ConcurrentBlockingQueue p = populatedQueue(i);
			assertTrue(q.removeAll(p));
			assertEquals(SIZE - i, q.size());
			for (int j = 0; j < i; ++j) {
				Integer I = (Integer) (p.remove());
				assertFalse(q.contains(I));
			}
		}
	}

	void checkToArray(ConcurrentBlockingQueue q) {
		int size = q.size();
		Object[] o = q.toArray();
		assertEquals(size, o.length);
		Iterator it = q.iterator();
		for (int i = 0; i < size; i++) {
			Integer x = (Integer) it.next();
			assertEquals((Integer) o[0] + i, (int) x);
			assertEquals((Integer) o[i], x);
		}
	}

	/**
	 * toArray() contains all elements in FIFO order
	 */
	public void testToArray() {
		ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(SIZE);
		for (int i = 0; i < SIZE; i++) {
			checkToArray(q);
			q.add(i);
		}
		// Provoke wraparound
		for (int i = 0; i < SIZE; i++) {
			checkToArray(q);
			assertEquals(i, q.poll());
			checkToArray(q);
			q.add(SIZE + i);
		}
		for (int i = 0; i < SIZE; i++) {
			checkToArray(q);
			assertEquals(SIZE + i, q.poll());
		}
	}

	void checkToArray2(ConcurrentBlockingQueue q) {
		int size = q.size();
		Integer[] a1 = size == 0 ? null : new Integer[size - 1];
		Integer[] a2 = new Integer[size];
		Integer[] a3 = new Integer[size + 2];
		if (size > 0)
			Arrays.fill(a1, 42);
		Arrays.fill(a2, 42);
		Arrays.fill(a3, 42);
		Integer[] b1 = size == 0 ? null : (Integer[]) q.toArray(a1);
		Integer[] b2 = (Integer[]) q.toArray(a2);
		Integer[] b3 = (Integer[]) q.toArray(a3);
		assertSame(a2, b2);
		assertSame(a3, b3);
		Iterator it = q.iterator();
		for (int i = 0; i < size; i++) {
			Integer x = (Integer) it.next();
			assertEquals(b1[i], x);
			assertEquals(b1[0] + i, (int) x);
			assertEquals(b2[i], x);
			assertEquals(b3[i], x);
		}
		assertNull(a3[size]);
		assertEquals(42, (int) a3[size + 1]);
		if (size > 0) {
			assertNotSame(a1, b1);
			assertEquals(size, b1.length);
			for (int i = 0; i < a1.length; i++) {
				assertEquals(42, (int) a1[i]);
			}
		}
	}

	/**
	 * toArray(a) contains all elements in FIFO order
	 */
	public void testToArray2() {
		ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(SIZE);
		for (int i = 0; i < SIZE; i++) {
			checkToArray2(q);
			q.add(i);
		}
		// Provoke wraparound
		for (int i = 0; i < SIZE; i++) {
			checkToArray2(q);
			assertEquals(i, q.poll());
			checkToArray2(q);
			q.add(SIZE + i);
		}
		for (int i = 0; i < SIZE; i++) {
			checkToArray2(q);
			assertEquals(SIZE + i, q.poll());
		}
	}

	/**
	 * toArray(incompatible array type) throws ArrayStoreException
	 */
	public void testToArray1_BadArg() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		try {
			q.toArray(new String[10]);
			shouldThrow();
		} catch (ArrayStoreException success) {
		}
	}

	/**
	 * iterator iterates through all elements
	 */
	public void testIterator() throws InterruptedException {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		Iterator it = q.iterator();
		while (it.hasNext()) {
			assertEquals(it.next(), q.take());
		}
	}

	/**
	 * iterator.remove removes current element
	 */
	public void testIteratorRemove() {
		final ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(3);
		q.add(two);
		q.add(one);
		q.add(three);

		Iterator it = q.iterator();
		it.next();
		it.remove();

		it = q.iterator();
		assertSame(it.next(), one);
		assertSame(it.next(), three);
		assertFalse(it.hasNext());
	}

	/**
	 * iterator ordering is FIFO
	 */
	public void testIteratorOrdering() {
		final ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(3);
		q.add(one);
		q.add(two);
		q.add(three);

		assertEquals("queue should be full", 0, q.remainingCapacity());

		int k = 0;
		for (Iterator it = q.iterator(); it.hasNext();) {
			assertEquals(++k, it.next());
		}
		assertEquals(3, k);
	}

	/**
	 * Modifications do not cause iterators to fail
	 */
	public void testWeaklyConsistentIteration() {
		final ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(3);
		q.add(one);
		q.add(two);
		q.add(three);
		for (Iterator it = q.iterator(); it.hasNext();) {
			q.remove();
			it.next();
		}
		assertEquals(0, q.size());
	}

	/**
	 * toString contains toStrings of elements
	 */
	public void testToString() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		String s = q.toString();
		for (int i = 0; i < SIZE; ++i) {
			assertTrue(s.contains(String.valueOf(i)));
		}
	}

	/**
	 * offer transfers elements across Executor tasks
	 */
	public void testOfferInExecutor() {
		final ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(2);
		q.add(one);
		q.add(two);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		final CheckedBarrier threadsStarted = new CheckedBarrier(2);
		executor.execute(new CheckedRunnable() {
			public void realRun() throws InterruptedException {
				assertFalse(q.offer(three));
				threadsStarted.await();
				assertTrue(q.offer(three, LONG_DELAY_MS, MILLISECONDS));
				assertEquals(0, q.remainingCapacity());
			}
		});

		executor.execute(new CheckedRunnable() {
			public void realRun() throws InterruptedException {
				threadsStarted.await();
				assertEquals(0, q.remainingCapacity());
				assertSame(one, q.take());
			}
		});

		joinPool(executor);
	}

	/**
	 * timed poll retrieves elements across Executor threads
	 */
	public void testPollInExecutor() {
		final ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(2);
		final CheckedBarrier threadsStarted = new CheckedBarrier(2);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new CheckedRunnable() {
			public void realRun() throws InterruptedException {
				assertNull(q.poll());
				threadsStarted.await();
				assertSame(one, q.poll(LONG_DELAY_MS, MILLISECONDS));
				checkEmpty(q);
			}
		});

		executor.execute(new CheckedRunnable() {
			public void realRun() throws InterruptedException {
				threadsStarted.await();
				q.put(one);
			}
		});

		joinPool(executor);
	}

	/**
	 * A deserialized serialized queue has same elements in same order
	 */
	public void testSerialization() throws Exception {
		Queue x = populatedQueue(SIZE);
		Queue y = serialClone(x);

		assertNotSame(x, y);
		assertEquals(x.size(), y.size());
		assertEquals(x.toString(), y.toString());
		assertTrue(Arrays.equals(x.toArray(), y.toArray()));
		while (!x.isEmpty()) {
			assertFalse(y.isEmpty());
			assertEquals(x.remove(), y.remove());
		}
		assertTrue(y.isEmpty());
	}

	/**
	 * drainTo(c) empties queue into another collection c
	 */
	@SuppressWarnings("deprecation")
	public void testDrainTo() {
		ConcurrentBlockingQueue q = populatedQueue(SIZE);
		ArrayList l = new ArrayList();
		q.drainTo(l);
		assertEquals(0, q.size());
		assertEquals(SIZE, l.size());
		for (int i = 0; i < SIZE; ++i)
			assertEquals(l.get(i), new Integer(i));
		q.add(zero);
		q.add(one);
		assertFalse(q.isEmpty());
		assertTrue(q.contains(zero));
		assertTrue(q.contains(one));
		l.clear();
		q.drainTo(l);
		assertEquals(0, q.size());
		assertEquals(2, l.size());
		for (int i = 0; i < 2; ++i)
			assertEquals(l.get(i), new Integer(i));
	}

	/**
	 * drainTo empties full queue, unblocking a waiting put.
	 */
	@SuppressWarnings("deprecation")
	public void testDrainToWithActivePut() throws InterruptedException {
		final ConcurrentBlockingQueue q = populatedQueue(SIZE);
		Thread t = new Thread(new CheckedRunnable() {
			public void realRun() throws InterruptedException {
				q.put(new Integer(SIZE + 1));
			}
		});

		t.start();
		ArrayList l = new ArrayList();
		q.drainTo(l);
		assertTrue(l.size() >= SIZE);
		for (int i = 0; i < SIZE; ++i)
			assertEquals(l.get(i), new Integer(i));
		t.join();
		assertTrue(q.size() + l.size() >= SIZE);
	}

	/**
	 * drainTo(c, n) empties first min(n, size) elements of queue into c
	 */
	@SuppressWarnings("deprecation")
	public void testDrainToN() {
		ConcurrentBlockingQueue q = new ConcurrentBlockingQueue(SIZE * 2);
		for (int i = 0; i < SIZE + 2; ++i) {
			for (int j = 0; j < SIZE; j++)
				assertTrue(q.offer(new Integer(j)));
			ArrayList l = new ArrayList();
			q.drainTo(l, i);
			int k = (i < SIZE) ? i : SIZE;
			assertEquals(k, l.size());
			assertEquals(SIZE - k, q.size());
			for (int j = 0; j < k; ++j)
				assertEquals(l.get(j), new Integer(j));
			while (q.poll() != null)
				;
		}
	}

}
