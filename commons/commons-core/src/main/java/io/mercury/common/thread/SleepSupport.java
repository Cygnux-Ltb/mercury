package io.mercury.common.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;

public final class SleepSupport {

    private static final Logger log = Log4j2LoggerFactory.getLogger(SleepSupport.class);

    private SleepSupport() {
    }

    /**
     * Use LockSupport.park()
     */
    public static void park() {
        LockSupport.park();
    }

    /**
     * Use LockSupport.parkNanos(1)
     */
    public static void parkNano() {
        LockSupport.parkNanos(1);
    }

    /**
     * Use LockSupport.parkNanos(long nanos)
     *
     * @param nanos the maximum number of nanoseconds to wait
     */
    public static void parkNanos(long nanos) {
        LockSupport.parkNanos(nanos);
    }

    /**
     * @param millis long
     */
    public static void sleepIgnoreInterrupts(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("SleepSupport::sleepIgnoreInterrupts(millis==[{}]) throw InterruptedException -> {}",
                    millis, e.getMessage(), e);
        }
    }

    /**
     * @param millis long
     * @param nanos  int
     */
    public static void sleepIgnoreInterrupts(long millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            log.error("SleepSupport::sleepIgnoreInterrupts(millis==[{}]) throw InterruptedException -> {}",
                    millis, e.getMessage(), e);
        }
    }

    /**
     * @param timeUnit TimeUnit
     * @param time     long
     */
    public static void sleepIgnoreInterrupts(@Nonnull TimeUnit timeUnit, long time) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
            log.error("SleepSupport::sleepIgnoreInterrupts(timeUnit==[{}], time==[{}]) throw InterruptedException -> {}",
                    timeUnit, time, e.getMessage(), e);
        }
    }

    /**
     * @param millis long
     * @throws RuntimeInterruptedException e
     */
    public static void sleep(long millis) throws RuntimeInterruptedException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("SleepSupport::sleep(millis==[{}]) throw InterruptedException -> {}",
                    millis, e.getMessage(), e);
            throw new RuntimeInterruptedException(e);
        }
    }

    /**
     * @param millis long
     * @param nanos  int
     * @throws RuntimeInterruptedException e
     */
    public static void sleep(long millis, int nanos) throws RuntimeInterruptedException {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            log.error("SleepSupport::sleep(millis==[{}], nanos==[{}]) throw InterruptedException -> {}",
                    millis, nanos, e.getMessage(), e);
            throw new RuntimeInterruptedException(e);
        }
    }

    /**
     * @param timeUnit TimeUnit
     * @param time     long
     * @throws RuntimeInterruptedException e
     */
    public static void sleep(@Nonnull TimeUnit timeUnit, long time) throws RuntimeInterruptedException {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
            log.error("SleepSupport::sleep(timeUnit==[{}], time==[{}]) throw InterruptedException -> {}",
                    timeUnit, time, e.getMessage(), e);
            throw new RuntimeInterruptedException(e);
        }
    }

}
