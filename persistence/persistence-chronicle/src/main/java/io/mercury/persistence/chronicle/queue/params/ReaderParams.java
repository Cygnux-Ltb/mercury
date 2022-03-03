package io.mercury.persistence.chronicle.queue.params;

import static io.mercury.common.lang.Assertor.greaterThan;
import static io.mercury.common.lang.Assertor.nonNull;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

public final class ReaderParams {

    // 是否读取失败后关闭线程
    private final boolean readFailCrash;
    // 是否读取失败后记录日志
    private final boolean readFailLogging;
    // 读取间隔时间单位
    private final TimeUnit readIntervalUnit;
    // 读取时间
    private final long readIntervalTime;
    // 延迟读取时间单位
    private final TimeUnit delayReadUnit;
    // 延迟读取时间
    private final long delayReadTime;
    // 是否等待数据写入
    private final boolean waitingData;
    // 是否自旋等待
    private final boolean spinWaiting;
    // 是否以异步方式退出
    private final boolean asyncExit;
    // 退出函数
    private final Runnable exitTask;

    private ReaderParams(Builder builder) {
        this.readFailCrash = builder.readFailCrash;
        this.readFailLogging = builder.readFailLogging;
        this.readIntervalUnit = builder.readIntervalUnit;
        this.readIntervalTime = builder.readIntervalTime;
        this.delayReadUnit = builder.delayReadUnit;
        this.delayReadTime = builder.delayReadTime;
        this.waitingData = builder.waitingData;
        this.spinWaiting = builder.spinWaiting;
        this.asyncExit = builder.asyncExit;
        this.exitTask = builder.exitTask;
    }

    public boolean isReadFailCrash() {
        return readFailCrash;
    }

    public boolean isReadFailLogging() {
        return readFailLogging;
    }

    public TimeUnit getReadIntervalUnit() {
        return readIntervalUnit;
    }

    public long getReadIntervalTime() {
        return readIntervalTime;
    }

    public TimeUnit getDelayReadUnit() {
        return delayReadUnit;
    }

    public long getDelayReadTime() {
        return delayReadTime;
    }

    public boolean isWaitingData() {
        return waitingData;
    }

    public boolean isSpinWaiting() {
        return spinWaiting;
    }

    public boolean isAsyncExit() {
        return asyncExit;
    }

    public Runnable getExitTask() {
        return exitTask;
    }

    /**
     * @return Builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * @return ReaderParams
     */
    public static ReaderParams defaultParams() {
        return new Builder().build();
    }

    public static class Builder {

        // 读取失败崩溃
        private boolean readFailCrash = false;
        // 读取失败打印Log
        private boolean readFailLogging = true;
        // 是否等待新数据
        private boolean waitingData = true;
        // 是否自旋等待
        private boolean spinWaiting = false;

        // 读取间隔
        private TimeUnit readIntervalUnit = TimeUnit.MILLISECONDS;
        private long readIntervalTime = 10;

        // 开始读取延迟时间
        private TimeUnit delayReadUnit = TimeUnit.MILLISECONDS;
        private long delayReadTime = 0;

        // 退出方式
        private boolean asyncExit = false;
        // 退出函数
        private Runnable exitTask = null;

        /**
         * 设置读取失败后崩溃
         *
         * @param readFailCrash :
         * @return Builder
         */
        public Builder isReadFailCrash(boolean readFailCrash) {
            this.readFailCrash = readFailCrash;
            return this;
        }

        /**
         * 设置读取失败进行记录
         *
         * @param readFailLogging :
         * @return Builder
         */
        public Builder isReadFailLogging(boolean readFailLogging) {
            this.readFailLogging = readFailLogging;
            return this;
        }

        /**
         * 设置是否等待新数据
         *
         * @param waitingData :
         * @return Builder
         */
        public Builder isWaitingData(boolean waitingData) {
            this.waitingData = waitingData;
            return this;
        }

        /**
         * 设置是否自旋等待新数据
         *
         * @param spinWaiting :
         * @return Builder
         */
        public Builder isSpinWaiting(boolean spinWaiting) {
            this.spinWaiting = spinWaiting;
            return this;
        }

        /**
         * 设置读取等待间隔
         *
         * @param timeUnit :
         * @param time     :
         * @return Builder
         */
        public Builder setReadInterval(@Nonnull TimeUnit timeUnit, long time) {
            this.readIntervalUnit = nonNull(timeUnit, "timeUnit");
            this.readIntervalTime = greaterThan(time, 0, "time");
            return this;
        }

        /**
         * 设置开始读取延迟时间
         *
         * @param timeUnit :
         * @param time     :
         * @return Builder
         */
        public Builder setDelayRead(@Nonnull TimeUnit timeUnit, long time) {
            this.delayReadUnit = nonNull(timeUnit, "timeUnit");
            this.delayReadTime = greaterThan(time, 0, "time");
            return this;
        }

        /**
         * 设置是否异步推出
         *
         * @param asyncExit :
         * @return Builder
         */
        public Builder isAsyncExit(boolean asyncExit) {
            this.asyncExit = asyncExit;
            return this;
        }

        /**
         * 退出读取任务执行线程
         *
         * @param exitTask :
         * @return Builder
         */
        public Builder setExitTask(@Nonnull Runnable exitTask) {
            this.exitTask = nonNull(exitTask, "exitTask");
            return this;
        }

        /**
         * @return ReaderParams
         */
        public ReaderParams build() {
            return new ReaderParams(this);
        }
    }
}
