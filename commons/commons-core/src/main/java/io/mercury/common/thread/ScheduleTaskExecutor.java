package io.mercury.common.thread;

import static io.mercury.common.thread.ThreadSupport.newMaxPriorityThread;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.sys.CurrentRuntime;

public final class ScheduleTaskExecutor {

    private ScheduleTaskExecutor() {
    }

    private static final Logger log = Log4j2LoggerFactory.getLogger(ScheduleTaskExecutor.class);

    /**
     * @param firstTime
     * @param runnable
     * @return
     */
    @Deprecated
    public static Timer startDelayTask(LocalDateTime firstTime, Runnable runnable) {
        return startDelayTask(Duration.between(LocalDateTime.now(), firstTime).toMillis(), TimeUnit.MILLISECONDS,
                runnable);
    }

    /**
     * @param delay
     * @param unit
     * @param runnable
     * @return
     */
    @Deprecated
    public static Timer startDelayTask(long delay, TimeUnit unit, Runnable runnable) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    log.error("TimerTask runner throw Exception -> {}", e.getMessage(), e);
                }
            }
        }, unit.toMillis(delay));
        return timer;
    }

    /**
     * @param firstTime
     * @param period
     * @param unit
     * @param runnable
     * @return
     */
    @Deprecated
    public static Timer startCycleTask(LocalDateTime firstTime, long period, TimeUnit unit, Runnable runnable) {
        return startCycleTask(Duration.between(LocalDateTime.now(), firstTime).toMillis(), unit.toMillis(period),
                TimeUnit.MILLISECONDS, runnable);
    }

    /**
     * @param delay
     * @param period
     * @param unit
     * @param runnable
     * @return
     */
    @Deprecated
    public static Timer startCycleTask(long delay, long period, TimeUnit unit, Runnable runnable) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    log.error("TimerTask runner throw Exception -> {}", e.getMessage(), e);
                }
            }
        }, unit.toMillis(delay), unit.toMillis(period));
        return timer;
    }

    /**
     * @param firstTime
     * @param period
     * @param unit
     * @param runnable
     * @return
     */
    @Deprecated
    public static Timer startFixedRateCycleTask(LocalDateTime firstTime, long period, TimeUnit unit,
                                                Runnable runnable) {
        return startFixedRateCycleTask(Duration.between(LocalDateTime.now(), firstTime).toMillis(),
                unit.toMillis(period), TimeUnit.MILLISECONDS, runnable);
    }

    /**
     * @param delay
     * @param period
     * @param unit
     * @param runnable
     * @return
     */
    @Deprecated
    public static Timer startFixedRateCycleTask(long delay, long period, TimeUnit unit, Runnable runnable) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate((new TimerTask() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    log.error("TimerTask runner throw Exception -> {}", e.getMessage(), e);
                }
            }
        }), unit.toMillis(delay), unit.toMillis(period));
        return timer;
    }

    /**
     * SingleThreadExecutor
     */
    public static final ScheduledExecutorService SINGLE_THREAD_SCHEDULED_EXECUTOR = newSingleThreadScheduledExecutor(
            runnable -> newMaxPriorityThread("SingleThreadScheduledExecutor", runnable));

    /**
     * Creates and executes a one-shot action that becomes enabled after the given
     * delay.
     *
     * @param firstTime
     * @param runnable
     */
    public static void singleThreadSchedule(LocalDateTime firstTime, Runnable runnable) {
        singleThreadSchedule(Duration.between(LocalDateTime.now(), firstTime).toMillis(), TimeUnit.MILLISECONDS,
                runnable);
    }

    /**
     * Creates and executes a one-shot action that becomes enabled after the given
     * delay.
     *
     * @param timeUnit
     * @param delay
     * @param runnable
     */
    public static void singleThreadSchedule(long delay, TimeUnit timeUnit, Runnable runnable) {
        SINGLE_THREAD_SCHEDULED_EXECUTOR.schedule(runnable, delay, timeUnit);
    }

    /**
     * 在SingleThreadExecutor中创建并执行一个周期性动作.<br>
     * 该动作在给定的初始延迟之后首先被启用. 即执行将在initialDelay之后开始.<br>
     * 随后在<b> [一次执行的终止] </b>和<b> [下一次执行的开始] </b>之间延迟给定时间.<br>
     * 如果任务的任何执行遇到异常, 则后续执行结束.<br>
     * 否则, 任务仅可通过取消或终止Executor来终止.<br>
     * <br>
     * The given delay between the termination of one execution <br>
     * and the commencement of the next.
     *
     * @param firstTime
     * @param unit
     * @param period
     * @param runnable
     */
    public static void singleThreadScheduleWithFixedDelay(LocalDateTime firstTime, long period, TimeUnit unit,
                                                          Runnable runnable) {
        singleThreadScheduleWithFixedDelay(Duration.between(LocalDateTime.now(), firstTime).toMillis(),
                unit.toMillis(period), TimeUnit.MILLISECONDS, runnable);
    }

    /**
     * 在SingleThreadExecutor中创建并执行一个周期性动作.<br>
     * 该动作在给定的初始延迟之后首先被启用. 即执行将在initialDelay之后开始.<br>
     * 随后在<b> [一次执行的终止] </b>和<b> [下一次执行的开始] </b>之间延迟给定时间.<br>
     * 如果任务的任何执行遇到异常, 则后续执行结束.<br>
     * 否则, 任务仅可通过取消或终止Executor来终止.<br>
     * <br>
     * The given delay between the termination of one execution <br>
     * and the commencement of the next.
     *
     * @param initialDelay
     * @param delay
     * @param unit
     * @param runnable
     */
    public static void singleThreadScheduleWithFixedDelay(long initialDelay, long delay, TimeUnit unit,
                                                          Runnable runnable) {
        SINGLE_THREAD_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
    }

    /**
     * 在SingleThreadExecutor中创建并执行一个周期性动作.<br>
     * 该动作在给定的初始延迟之后首先被启用, 即执行将在initialDelay之后开始.<br>
     * 然后是initialDelay + period, 然后是initialDelay + 2 * period, 依此类推.<br>
     * 如果任务的任何执行遇到异常, 则后续执行结束.<br>
     * 否则, 任务仅可通过取消或终止Executor来终止.<br>
     * 如果此任务的执行时间超过其周期, 则后续执行可能会延迟, 但不会同时执行.<br>
     * <br>
     * That is executions will commence after initialDelay <br>
     * then initialDelay + period, then initialDelay + 2 * period, and so on. <br>
     *
     * @param firstTime
     * @param period
     * @param period
     * @param runnable
     */
    public static void singleThreadScheduleAtFixedRate(LocalDateTime firstTime, long period, TimeUnit unit,
                                                       Runnable runnable) {
        singleThreadScheduleAtFixedRate(Duration.between(LocalDateTime.now(), firstTime).toMillis(),
                unit.toMillis(period), TimeUnit.MILLISECONDS, runnable);
    }

    /**
     * 在SingleThreadExecutor中创建并执行一个周期性动作.<br>
     * 该动作在给定的初始延迟之后首先被启用, 即执行将在initialDelay之后开始.<br>
     * 然后是initialDelay + period, 然后是initialDelay + 2 * period, 依此类推.<br>
     * 如果任务的任何执行遇到异常, 则后续执行结束.<br>
     * 否则, 任务仅可通过取消或终止Executor来终止.<br>
     * 如果此任务的执行时间超过其周期, 则后续执行可能会延迟, 但不会同时执行.<br>
     * <br>
     * That is executions will commence after initialDelay <br>
     * then initialDelay + period, then initialDelay + 2 * period, and so on. <br>
     *
     * @param initialDelay
     * @param period
     * @param unit
     * @param runnable
     */
    public static void singleThreadScheduleAtFixedRate(long initialDelay, long period, TimeUnit unit,
                                                       Runnable runnable) {
        SINGLE_THREAD_SCHEDULED_EXECUTOR.scheduleAtFixedRate(runnable, initialDelay, period, unit);
    }

    /**
     * MultiThreadExecutor 线程数为: 核心数量 * 2
     */
    public static final ScheduledExecutorService MULTIPLE_THREAD_SCHEDULED_EXECUTOR =
            Executors.newScheduledThreadPool(CurrentRuntime.availableProcessors() * 2,
                    runnable -> new Thread(runnable, "MultipleThreadScheduledExecutor"));

    /**
     * Creates and executes a one-shot action that becomes enabled after the given
     * delay.
     *
     * @param firstTime
     * @param runnable
     */
    public static void multiThreadSchedule(LocalDateTime firstTime, Runnable runnable) {
        multiThreadSchedule(Duration.between(LocalDateTime.now(), firstTime).toMillis(), TimeUnit.MILLISECONDS,
                runnable);
    }

    /**
     * Creates and executes a one-shot action that becomes enabled after the given
     * delay.
     *
     * @param delay
     * @param unit
     * @param runnable
     */
    public static void multiThreadSchedule(long delay, TimeUnit unit, Runnable runnable) {
        MULTIPLE_THREAD_SCHEDULED_EXECUTOR.schedule(runnable, delay, unit);
    }

    /**
     * 在MultiThreadExecutor中创建并执行一个周期性动作.<br>
     * 该动作在给定的初始延迟之后首先被启用. 即执行将在initialDelay之后开始.<br>
     * 随后在<b> [一次执行的终止] </b>和<b> [下一次执行的开始] </b>之间延迟给定时间.<br>
     * 如果任务的任何执行遇到异常, 则后续执行结束.<br>
     * 否则, 任务仅可通过取消或终止Executor来终止.<br>
     * <br>
     * The given delay between the termination of one execution <br>
     * and the commencement of the next.
     *
     * @param firstTime
     * @param period
     * @param unit
     * @param runnable
     */
    public static void multiThreadScheduleWithFixedDelay(LocalDateTime firstTime, long period, TimeUnit unit,
                                                         Runnable runnable) {
        multiThreadScheduleWithFixedDelay(Duration.between(LocalDateTime.now(), firstTime).toMillis(),
                unit.toMillis(period), TimeUnit.MILLISECONDS, runnable);
    }

    /**
     * 在MultiThreadExecutor中创建并执行一个周期性动作.<br>
     * 该动作在给定的初始延迟之后首先被启用. 即执行将在initialDelay之后开始.<br>
     * 随后在<b> [一次执行的终止] </b>和<b> [下一次执行的开始] </b>之间延迟给定时间.<br>
     * 如果任务的任何执行遇到异常, 则后续执行结束.<br>
     * 否则, 任务仅可通过取消或终止Executor来终止.<br>
     * <br>
     * The given delay between the termination of one execution <br>
     * and the commencement of the next.
     *
     * @param delay
     * @param period
     * @param unit
     * @param runnable
     */
    public static void multiThreadScheduleWithFixedDelay(long delay, long period, TimeUnit unit, Runnable runnable) {
        MULTIPLE_THREAD_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(runnable, delay, period, unit);
    }

    /**
     * 在MultiThreadExecutor中创建并执行一个周期性动作.<br>
     * 该动作在给定的初始延迟之后首先被启用, 即执行将在initialDelay之后开始.<br>
     * 然后是initialDelay + period, 然后是initialDelay + 2 * period, 依此类推.<br>
     * 如果任务的任何执行遇到异常, 则后续执行结束.<br>
     * 否则, 任务仅可通过取消或终止Executor来终止.<br>
     * 如果此任务的执行时间超过其周期, 则后续执行可能会延迟, 但不会同时执行.<br>
     * <br>
     * That is executions will commence after initialDelay <br>
     * then initialDelay + period, then initialDelay + 2 * period, and so on. <br>
     *
     * @param firstTime
     * @param period
     * @param unit
     * @param runnable
     */
    public static void multiThreadScheduleAtFixedRate(LocalDateTime firstTime, long period, TimeUnit unit,
                                                      Runnable runnable) {
        multiThreadScheduleAtFixedRate(Duration.between(LocalDateTime.now(), firstTime).toMillis(),
                unit.toMillis(period), TimeUnit.MILLISECONDS, runnable);
    }

    /**
     * 在MultiThreadExecutor中创建并执行一个周期性动作.<br>
     * 该动作在给定的初始延迟之后首先被启用, 即执行将在initialDelay之后开始.<br>
     * 然后是initialDelay + period, 然后是initialDelay + 2 * period, 依此类推.<br>
     * 如果任务的任何执行遇到异常, 则后续执行结束.<br>
     * 否则, 任务仅可通过取消或终止Executor来终止.<br>
     * 如果此任务的执行时间超过其周期, 则后续执行可能会延迟, 但不会同时执行.<br>
     * <br>
     * That is executions will commence after initialDelay <br>
     * then initialDelay + period, then initialDelay + 2 * period, and so on. <br>
     *
     * @param delay
     * @param period
     * @param unit
     * @param runnable
     */
    public static void multiThreadScheduleAtFixedRate(long delay, long period, TimeUnit unit, Runnable runnable) {
        MULTIPLE_THREAD_SCHEDULED_EXECUTOR.scheduleAtFixedRate(runnable, delay, period, unit);
    }

    public static void main(String[] args) {
        ScheduleTaskExecutor.multiThreadScheduleAtFixedRate(LocalDateTime.now(), 15, TimeUnit.SECONDS,
                () -> {
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(now + " -> Add task delay 5 SECONDS, Exec time -> " + now.plusSeconds(10));
                    ScheduleTaskExecutor.multiThreadSchedule(10, TimeUnit.SECONDS,
                            () -> System.out.println(LocalDateTime.now() + " -> Task finish"));
                }
        );
    }

}
