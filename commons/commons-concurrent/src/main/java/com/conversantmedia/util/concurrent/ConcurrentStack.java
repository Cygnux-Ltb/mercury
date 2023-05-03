package com.conversantmedia.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Concurrent "lock-free" version of a stack.
 */
public final class ConcurrentStack<N> implements BlockingStack<N> {

    private final int size;

    private final AtomicReferenceArray<N> stack;

    // representing the top of the stack
    private final ContendedAtomicInteger stackTop = new ContendedAtomicInteger(0);

    private final SequenceLock seqLock = new SequenceLock();

    private final Condition stackNotFullCondition;
    private final Condition stackNotEmptyCondition;

    public ConcurrentStack(final int size) {
        this(size, SpinPolicy.WAITING);
    }

    /**
     * construct a new stack of given capacity
     *
     * @param size       - the stack size
     * @param spinPolicy - determine the level of cpu aggressiveness in waiting
     */
    public ConcurrentStack(final int size, final SpinPolicy spinPolicy) {
        int stackSize = 1;
        while (stackSize < size)
            stackSize <<= 1;
        this.size = stackSize;
        stack = new AtomicReferenceArray<>(stackSize);

        switch (spinPolicy) {
            case BLOCKING -> {
                stackNotFullCondition = new StackNotFull();
                stackNotEmptyCondition = new StackNotEmpty();
            }
            case SPINNING -> {
                stackNotFullCondition = new SpinningStackNotFull();
                stackNotEmptyCondition = new SpinningStackNotEmpty();
            }
            default -> {
                stackNotFullCondition = new WaitingStackNotFull();
                stackNotEmptyCondition = new WaitingStackNotEmpty();
            }
        }
    }

    @Override
    public boolean push(final N n, final long time, final TimeUnit unit) throws InterruptedException {
        final long endDate = System.nanoTime() + unit.toNanos(time);
        while (!push(n)) {
            if (endDate - System.nanoTime() < 0) {
                return false;
            }

            Condition.waitStatus(time, unit, stackNotFullCondition);
        }
        stackNotEmptyCondition.signal();
        return true;
    }

    @Override
    public void pushInterruptibly(final N n) throws InterruptedException {
        while (!push(n)) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            stackNotFullCondition.await();
        }
        stackNotEmptyCondition.signal();
    }

    @Override
    public boolean contains(final N n) {
        if (n != null) {
            for (int i = 0; i < stackTop.get(); i++) {
                if (n.equals(stack.get(i)))
                    return true;
            }
        }
        return false;
    }

    /**
     * add an element to the stack, failing if the stack is unable to grow
     *
     * @param n - the element to push
     * @return boolean - false if stack overflow, true otherwise
     */
    @Override
    public boolean push(final N n) {
        int spin = 0;
        for (; ; ) {

            final long writeLock = seqLock.tryWriteLock();
            if (writeLock > 0L) {
                try {
                    final int stackTop = this.stackTop.get();
                    if (size > stackTop) {
                        try {
                            stack.set(stackTop, n);
                            stackNotEmptyCondition.signal();
                            return true;
                        } finally {
                            this.stackTop.set(stackTop + 1);
                        }
                    } else {
                        return false;
                    }
                } finally {
                    seqLock.unlock(writeLock);
                }

            }
            spin = Condition.progressiveYield(spin);
        }
    }

    /**
     * peek at the top of the stack
     *
     * @return N - the object at the top of the stack
     */
    @Override
    public N peek() {
        // read the current cursor
        int spin = 0;
        for (; ; ) {
            final long readLock = seqLock.readLock();
            final int stackTop = this.stackTop.get();
            if (stackTop > 0) {
                final N n = stack.get(stackTop - 1);
                if (seqLock.readLockHeld(readLock)) {
                    return n;
                } // else loop again
            } else {
                return null;
            }

            spin = Condition.progressiveYield(spin);
        }
    }

    /**
     * pop the next element off the stack
     *
     * @return N - The object on the top of the stack
     */
    @Override
    public N pop() {

        int spin = 0;
        // now pop the stack
        for (; ; ) {
            final long writeLock = seqLock.tryWriteLock();
            if (writeLock > 0) {
                try {
                    final int stackTop = this.stackTop.get();
                    final int lastRef = stackTop - 1;
                    if (stackTop > 0) {
                        try {
                            // if we can modify the stack - i.e. nobody else is modifying
                            final N n = stack.get(lastRef);
                            stack.set(lastRef, null);
                            stackNotFullCondition.signal();
                            return n;
                        } finally {
                            this.stackTop.set(lastRef);
                        }
                    } else {
                        return null;
                    }
                } finally {
                    seqLock.unlock(writeLock);
                }
            }

            spin = Condition.progressiveYield(spin);

        }
    }

    @Override
    public N pop(final long time, final TimeUnit unit) throws InterruptedException {
        final long endTime = System.nanoTime() + unit.toNanos(time);
        for (; ; ) {
            final N n = pop();
            if (n != null) {
                stackNotFullCondition.signal();
                return n;
            } else {
                if (endTime - System.nanoTime() < 0) {
                    return null;
                }
            }
            Condition.waitStatus(time, unit, stackNotEmptyCondition);
        }
    }

    @Override
    public N popInterruptibly() throws InterruptedException {
        for (; ; ) {
            final N n = pop();
            if (n != null) {
                stackNotFullCondition.signal();
                return n;
            } else {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
            }
            stackNotEmptyCondition.await();
        }
    }

    /**
     * Return the size of the stack
     *
     * @return int - number of elements in the stack
     */
    @Override
    public int size() {
        return stackTop.get();
    }

    /**
     * how much available space in the stack
     */
    @Override
    public int remainingCapacity() {
        return size - stackTop.get();
    }

    /**
     * @return boolean - true if stack is currently empty
     */
    @Override
    public boolean isEmpty() {
        return stackTop.get() == 0;
    }

    /**
     * clear the stack - does not null old references
     */
    @Override
    public void clear() {
        int spin = 0;
        for (; ; ) {
            final long writeLock = seqLock.tryWriteLock();
            if (writeLock > 0L) {
                final int stackTop = this.stackTop.get();
                if (stackTop > 0) {
                    try {
                        for (int i = 0; i < stackTop; i++) {
                            stack.set(i, null);
                        }
                        stackNotFullCondition.signal();
                        return;
                    } finally {
                        this.stackTop.set(0);
                    }
                } else {
                    return;
                }
            }

            spin = Condition.progressiveYield(spin);
        }
    }

    private boolean isFull() {
        return size == stackTop.get();
    }

    // condition used for signaling queue is full
    private final class WaitingStackNotFull extends AbstractWaitingCondition {

        @Override
        // @return boolean - true if the queue is full
        public boolean test() {
            return isFull();
        }
    }

    // condition used for signaling queue is empty
    private final class WaitingStackNotEmpty extends AbstractWaitingCondition {
        @Override
        // @return boolean - true if the queue is empty
        public boolean test() {
            return isEmpty();
        }
    }

    // condition used for signaling queue is full
    private final class SpinningStackNotFull extends AbstractSpinningCondition {

        @Override
        // @return boolean - true if the queue is full
        public boolean test() {
            return isFull();
        }
    }

    // condition used for signaling queue is empty
    private final class SpinningStackNotEmpty extends AbstractSpinningCondition {
        @Override
        // @return boolean - true if the queue is empty
        public boolean test() {
            return isEmpty();
        }
    }

    // condition used for signaling queue is full
    private final class StackNotFull extends AbstractCondition {

        @Override
        // @return boolean - true if the queue is full
        public boolean test() {
            return isFull();
        }
    }

    // condition used for signaling queue is empty
    private final class StackNotEmpty extends AbstractCondition {
        @Override
        // @return boolean - true if the queue is empty
        public boolean test() {
            return isEmpty();
        }
    }

}
