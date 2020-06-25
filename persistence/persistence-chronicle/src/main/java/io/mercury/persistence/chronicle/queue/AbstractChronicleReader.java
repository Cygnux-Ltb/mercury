package io.mercury.persistence.chronicle.queue;

import static io.mercury.common.thread.ThreadTool.sleep;
import static io.mercury.common.thread.ThreadTool.startNewThread;
import static io.mercury.common.util.Assertor.greaterThan;
import static io.mercury.common.util.Assertor.nonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.annotation.lang.ProtectedAbstractMethod;
import io.mercury.common.annotation.lang.ThrowsRuntimeException;
import io.mercury.common.datetime.TimeConst;
import io.mercury.persistence.chronicle.exception.ChronicleReadException;
import io.mercury.persistence.chronicle.queue.AbstractChronicleQueue.CloseableChronicleAccessor;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.TailerState;

@Immutable
@NotThreadSafe
public abstract class AbstractChronicleReader<T> extends CloseableChronicleAccessor implements Runnable {

	private final String readerName;
	private final FileCycle fileCycle;

	protected ReaderParam readerParam;

	protected final Logger logger;
	protected final ExcerptTailer excerptTailer;

	private final Consumer<T> consumer;

	AbstractChronicleReader(long allocateSeq, String readerName, FileCycle fileCycle, ReaderParam readerParam,
			Logger logger, ExcerptTailer excerptTailer, Consumer<T> consumer) {
		super(allocateSeq);
		this.readerName = readerName;
		this.fileCycle = fileCycle;
		this.readerParam = readerParam;
		this.logger = logger;
		this.excerptTailer = excerptTailer;
		this.consumer = consumer;
	}

	public ExcerptTailer excerptTailer() {
		return excerptTailer;
	}

	public boolean moveTo(LocalDate date) {
		return moveTo(date.toEpochDay() * TimeConst.SECONDS_PER_DAY);
	}

	public boolean moveTo(LocalDateTime dateTime, ZoneId zoneId) {
		return moveTo(ZonedDateTime.of(dateTime, zoneId));
	}

	public boolean moveTo(ZonedDateTime dateTime) {
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

	public void toStart() {
		excerptTailer.toStart();
	}

	public void toEnd() {
		excerptTailer.toEnd();
	}

	public int cycle() {
		return excerptTailer.cycle();
	}

	public long epochSecond() {
		return ((long) excerptTailer.cycle()) * fileCycle.getSeconds();
	}

	public long index() {
		return excerptTailer.index();
	}

	public TailerState state() {
		return excerptTailer.state();
	}

	public String readerName() {
		return readerName;
	}

	public Thread runningOnNewThread() {
		return runningOnNewThread(readerName);
	}

	public Thread runningOnNewThread(String threadName) {
		return startNewThread(this, threadName);
	}

	/**
	 * 
	 * @return
	 */
	@ProtectedAbstractMethod
	protected abstract T next0();

	/**
	 * Get next element of current cursor position.
	 * 
	 * @return
	 * @throws IllegalStateException
	 * @throws ChronicleReadException
	 */
	@ThrowsRuntimeException({ IllegalStateException.class, ChronicleReadException.class })
	@CheckForNull
	public T next() throws IllegalStateException, ChronicleReadException {
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
		boolean readFailLogging = readerParam.readFailLogging;
		boolean readFailCrash = readerParam.readFailCrash;
		boolean waitingData = readerParam.waitingData;
		TimeUnit readIntervalUnit = readerParam.readIntervalUnit;
		long readIntervalTime = readerParam.readIntervalTime;
		if (readerParam.delayReadTime > 0)
			sleep(readerParam.delayReadUnit, readerParam.delayReadTime);
		for (;;) {
			if (isClose) {
				logger.info("ChronicleReader is cloesd, execute exit()");
				exit();
				break;
			}
			T next = null;
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
					sleep(readIntervalUnit, readIntervalTime);
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
		Runnable exitRunnable = readerParam.exitRunnable;
		if (exitRunnable != null) {
			if (readerParam.asyncExit) {
				// 异步执行退出函数
				startNewThread(exitRunnable, readerName + "-exit");
			} else {
				// 同步执行退出函数
				exitRunnable.run();
			}
		}
		logger.info("reader -> {} running exit", readerName);
	}

	@Override
	protected void close0() {

	}

	public static final class ReaderParam {

		// 是否读取失败后关闭线程
		private boolean readFailCrash;
		// 是否读取失败后记录日志
		private boolean readFailLogging;
		// 读取间隔时间单位
		private TimeUnit readIntervalUnit;
		// 读取时间
		private long readIntervalTime;
		// 延迟读取时间单位
		private TimeUnit delayReadUnit;
		// 延迟读取时间
		private long delayReadTime;
		// 是否等待数据写入
		private boolean waitingData;
		// 是否以异步方式退出
		private boolean asyncExit;
		// 退出函数
		private Runnable exitRunnable;

		private ReaderParam(Builder builder) {
			this.readFailCrash = builder.readFailCrash;
			this.readFailLogging = builder.readFailLogging;
			this.readIntervalUnit = builder.readIntervalUnit;
			this.readIntervalTime = builder.readIntervalTime;
			this.delayReadUnit = builder.delayReadUnit;
			this.delayReadTime = builder.delayReadTime;
			this.waitingData = builder.waitingData;
			this.asyncExit = builder.asyncExit;
			this.exitRunnable = builder.exitRunnable;
		}

		public static Builder newBuilder() {
			return new Builder();
		}

		static ReaderParam defaultParam() {
			return new Builder().build();
		}

		public static class Builder {

			private boolean readFailCrash = false;
			private boolean readFailLogging = true;
			private boolean waitingData = true;

			// 缺省读取间隔
			private TimeUnit readIntervalUnit = TimeUnit.MILLISECONDS;
			private long readIntervalTime = 100;

			// 缺省延迟时间
			private TimeUnit delayReadUnit = TimeUnit.MILLISECONDS;
			private long delayReadTime = 0;

			// 缺省退出方式
			private boolean asyncExit = false;
			private Runnable exitRunnable;

			public Builder readFailCrash(boolean readFailCrash) {
				this.readFailCrash = readFailCrash;
				return this;
			}

			public Builder readFailLogging(boolean readFailLogging) {
				this.readFailLogging = readFailLogging;
				return this;
			}

			public Builder waitingData(boolean waitingData) {
				this.waitingData = waitingData;
				return this;
			}

			public Builder readInterval(TimeUnit timeUnit, long time) {
				this.readIntervalUnit = nonNull(timeUnit, "timeUnit");
				this.readIntervalTime = greaterThan(time, 0, "time");
				return this;
			}

			public Builder delayRead(TimeUnit timeUnit, long time) {
				this.delayReadUnit = nonNull(timeUnit, "timeUnit");
				this.delayReadTime = greaterThan(time, 0, "time");
				return this;
			}

			public Builder asyncExit(boolean asyncExit) {
				this.asyncExit = asyncExit;
				return this;
			}

			public Builder exitRunnable(@Nullable Runnable exitRunnable) {
				this.exitRunnable = nonNull(exitRunnable, "exitRunnable");
				return this;
			}

			public ReaderParam build() {
				return new ReaderParam(this);
			}
		}
	}

}
