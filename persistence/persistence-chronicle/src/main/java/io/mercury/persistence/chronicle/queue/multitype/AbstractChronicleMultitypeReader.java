package io.mercury.persistence.chronicle.queue.multitype;

import static io.mercury.common.lang.Assertor.greaterThan;
import static io.mercury.common.lang.Assertor.nonNull;
import static io.mercury.common.thread.SleepSupport.sleep;
import static io.mercury.common.thread.ThreadSupport.startNewThread;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.datetime.TimeConst;
import io.mercury.persistence.chronicle.exception.ChronicleReadException;
import io.mercury.persistence.chronicle.queue.FileCycle;
import io.mercury.persistence.chronicle.queue.multitype.AbstractChronicleMultitypeQueue.CloseableChronicleAccessor;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.TailerState;

@Immutable
@NotThreadSafe
public abstract class AbstractChronicleMultitypeReader<OUT> extends CloseableChronicleAccessor implements Runnable {

	private final String readerName;
	private final FileCycle fileCycle;

	protected final ReaderParam param;

	protected final Logger logger;
	protected final ExcerptTailer excerptTailer;

	private final Consumer<OUT> consumer;

	/**
	 * 
	 * @param allocateSeq
	 * @param readerName
	 * @param fileCycle
	 * @param param
	 * @param logger
	 * @param excerptTailer
	 * @param consumer
	 */
	protected AbstractChronicleMultitypeReader(long allocateSeq, String readerName, FileCycle fileCycle,
			ReaderParam param, Logger logger, ExcerptTailer excerptTailer, Consumer<OUT> consumer) {
		super(allocateSeq);
		this.readerName = readerName;
		this.fileCycle = fileCycle;
		this.param = param;
		this.logger = logger;
		this.excerptTailer = excerptTailer;
		this.consumer = consumer;
	}

	public ExcerptTailer excerptTailer() {
		return excerptTailer;
	}

	public boolean moveTo(@Nonnull LocalDate date) {
		return moveTo(date.toEpochDay() * TimeConst.SECONDS_PER_DAY);
	}

	public boolean moveTo(@Nonnull LocalDateTime dateTime, @Nonnull ZoneId zoneId) {
		return moveTo(ZonedDateTime.of(dateTime, zoneId));
	}

	public boolean moveTo(@Nonnull ZonedDateTime dateTime) {
		return moveTo(dateTime.toEpochSecond());
	}

	/**
	 * Move cursor to input epoch seconds.
	 * 
	 * @param epochSecond
	 * @return
	 */
	public boolean moveTo(long epochSecond) {
		return excerptTailer.moveToIndex(fileCycle.toIndex(epochSecond));
	}

	/**
	 * 移动到开头
	 */
	public void toStart() {
		excerptTailer.toStart();
	}

	/**
	 * 移动到结尾
	 */
	public void toEnd() {
		excerptTailer.toEnd();
	}

	/**
	 * 
	 * @return int
	 */
	public int cycle() {
		return excerptTailer.cycle();
	}

	/**
	 * 
	 * @return long
	 */
	public long epochSecond() {
		return ((long) excerptTailer.cycle()) * fileCycle.getSeconds();
	}

	/**
	 * 
	 * @return long
	 */
	public long index() {
		return excerptTailer.index();
	}

	/**
	 * 
	 * @return TailerState
	 */
	public TailerState state() {
		return excerptTailer.state();
	}

	/**
	 * 
	 * @return String
	 */
	public String readerName() {
		return readerName;
	}

	/**
	 * 
	 * @return Thread
	 */
	public Thread runningOnNewThread() {
		return runningOnNewThread(readerName);
	}

	/**
	 * 
	 * @param threadName
	 * @return Thread
	 */
	public Thread runningOnNewThread(String threadName) {
		return startNewThread(threadName, this);
	}

	/**
	 * 
	 * @return
	 */
	@AbstractFunction
	protected abstract OUT next0();

	/**
	 * Get next element of current cursor position.
	 * 
	 * @return
	 * @throws IllegalStateException
	 * @throws ChronicleReadException
	 */
	@CheckForNull
	public OUT next() throws IllegalStateException, ChronicleReadException {
		if (isClose) {
			throw new IllegalStateException("Unable to read next, Chronicle queue is closed");
		}
		try {
			return next0();
		} catch (Exception e) {
			throw new ChronicleReadException(e.getMessage(), e);
		}
	}

	@Override
	public void run() {
		final boolean readFailLogging = param.isReadFailLogging();
		final boolean readFailCrash = param.isReadFailCrash();
		final boolean waitingData = param.isWaitingData();
		final boolean spinWaiting = param.isSpinWaiting();
		final TimeUnit readIntervalUnit = param.getReadIntervalUnit();
		final long readIntervalTime = param.getReadIntervalTime();
		if (param.delayReadTime > 0)
			sleep(param.getDelayReadUnit(), param.getDelayReadTime());
		for (;;) {
			if (isClose) {
				logger.info("ChronicleReader is cloesd, execute exit()");
				exit();
				break;
			}
			OUT next = null;
			try {
				next = next();
			} catch (ChronicleReadException e) {
				if (readFailLogging)
					logger.error("{} call next throw exception -> {}", readerName, e.getMessage(), e);
				if (readFailCrash)
					throw e;
			}
			if (next == null) {
				// 等待新数据
				if (waitingData) {
					// 非自旋等待
					if (!spinWaiting) {
						sleep(readIntervalUnit, readIntervalTime);
					}
				} else {
					// 数据读取完毕, 退出线程
					exit();
					break;
				}
			} else {
				consumer.accept(next);
			}
		}
	}

	private void exit() {
		final Runnable exitRunnable = param.exitRunnable;
		if (exitRunnable != null) {
			if (param.asyncExit) {
				// 异步执行退出函数
				startNewThread(readerName + "-exit", exitRunnable);
			} else {
				// 同步执行退出函数
				exitRunnable.run();
			}
		}
		logger.info("reader -> {} running exit", readerName);
	}

	@Override
	protected void close0() {
		// TODO NONE
	}

	public static final class ReaderParam {

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
		private final Runnable exitRunnable;

		private ReaderParam(Builder builder) {
			this.readFailCrash = builder.readFailCrash;
			this.readFailLogging = builder.readFailLogging;
			this.readIntervalUnit = builder.readIntervalUnit;
			this.readIntervalTime = builder.readIntervalTime;
			this.delayReadUnit = builder.delayReadUnit;
			this.delayReadTime = builder.delayReadTime;
			this.waitingData = builder.waitingData;
			this.spinWaiting = builder.spinWaiting;
			this.asyncExit = builder.asyncExit;
			this.exitRunnable = builder.exitRunnable;
		}

		public static Builder newBuilder() {
			return new Builder();
		}

		static ReaderParam defaultParam() {
			return new Builder().build();
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

		public Runnable getExitRunnable() {
			return exitRunnable;
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
			private Runnable exitRunnable;

			/**
			 * 设置读取失败崩溃
			 * 
			 * @param readFailCrash
			 * @return
			 */
			public Builder readFailCrash(boolean readFailCrash) {
				this.readFailCrash = readFailCrash;
				return this;
			}

			/**
			 * 设置读取失败记录
			 * 
			 * @param readFailLogging
			 * @return
			 */
			public Builder readFailLogging(boolean readFailLogging) {
				this.readFailLogging = readFailLogging;
				return this;
			}

			/**
			 * 设置是否等待新数据
			 * 
			 * @param waitingData
			 * @return
			 */
			public Builder waitingData(boolean waitingData) {
				this.waitingData = waitingData;
				return this;
			}

			/**
			 * 设置是否自旋等待新数据
			 * 
			 * @param spinWaiting
			 * @return
			 */
			public Builder spinWaiting(boolean spinWaiting) {
				this.spinWaiting = spinWaiting;
				return this;
			}

			/**
			 * 设置读取等待间隔
			 * 
			 * @param timeUnit
			 * @param time
			 * @return
			 */
			public Builder readInterval(@Nonnull TimeUnit timeUnit, long time) {
				this.readIntervalUnit = nonNull(timeUnit, "timeUnit");
				this.readIntervalTime = greaterThan(time, 0, "time");
				return this;
			}

			/**
			 * 设置开始读取延迟时间
			 * 
			 * @param timeUnit
			 * @param time
			 * @return
			 */
			public Builder delayRead(@Nonnull TimeUnit timeUnit, long time) {
				this.delayReadUnit = nonNull(timeUnit, "timeUnit");
				this.delayReadTime = greaterThan(time, 0, "time");
				return this;
			}

			/**
			 * 设置是否异步推出
			 * 
			 * @param asyncExit
			 * @return
			 */
			public Builder asyncExit(boolean asyncExit) {
				this.asyncExit = asyncExit;
				return this;
			}

			/**
			 * 退出读取任务执行线程
			 * 
			 * @param exitRunnable
			 * @return
			 */
			public Builder exitRunnable(@Nonnull Runnable exitRunnable) {
				this.exitRunnable = nonNull(exitRunnable, "exitRunnable");
				return this;
			}

			/**
			 * 
			 * @return
			 */
			public ReaderParam build() {
				return new ReaderParam(this);
			}
		}
	}

}
