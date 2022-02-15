package io.mercury.persistence.chronicle.queue;

import static io.mercury.common.datetime.DateTimeUtil.formatDateTime;
import static io.mercury.common.datetime.pattern.DateTimePattern.YY_MM_DD_HH_MM_SS_SSS;
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
import io.mercury.persistence.chronicle.queue.base.CloseableChronicleAccessor;
import io.mercury.persistence.chronicle.queue.params.ReaderParams;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.TailerState;

@Immutable
@NotThreadSafe
public abstract class AbstractChronicleReader<OUT> extends CloseableChronicleAccessor implements Runnable {

	private final String readerName;
	private final FileCycle fileCycle;

	protected final ReaderParams params;

	protected final Logger logger;
	protected final ExcerptTailer tailer;

	protected final Consumer<OUT> dataConsumer;

	/**
	 * 
	 * @param allocateSeq
	 * @param readerName
	 * @param fileCycle
	 * @param params
	 * @param logger
	 * @param tailer
	 * @param dataConsumer
	 */
	protected AbstractChronicleReader(long allocateSeq, String readerName, FileCycle fileCycle, ReaderParams params,
			Logger logger, ExcerptTailer tailer, Consumer<OUT> dataConsumer) {
		super(allocateSeq);
		this.readerName = readerName;
		this.fileCycle = fileCycle;
		this.params = params;
		this.logger = logger;
		this.tailer = tailer;
		this.dataConsumer = dataConsumer;
	}

	public ExcerptTailer getExcerptTailer() {
		return tailer;
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
		return tailer.moveToIndex(fileCycle.toIndex(epochSecond));
	}

	/**
	 * 移动到开头
	 */
	public void toStart() {
		tailer.toStart();
	}

	/**
	 * 移动到结尾
	 */
	public void toEnd() {
		tailer.toEnd();
	}

	/**
	 * 
	 * @return int
	 */
	public int cycle() {
		return tailer.cycle();
	}

	/**
	 * 
	 * @return long
	 */
	public long epochSecond() {
		return ((long) tailer.cycle()) * fileCycle.getSeconds();
	}

	/**
	 * 
	 * @return long
	 */
	public long index() {
		return tailer.index();
	}

	/**
	 * 
	 * @return TailerState
	 */
	public TailerState state() {
		return tailer.state();
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
	public Thread runWithNewThread() {
		return runWithNewThread(readerName);
	}

	/**
	 * 
	 * @param threadName
	 * @return Thread
	 */
	public Thread runWithNewThread(String threadName) {
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
		if (isClose)
			throw new IllegalStateException("Unable to read next, Chronicle queue is closed");
		try {
			return next0();
		} catch (Exception e) {
			throw new ChronicleReadException(e.getMessage(), e);
		}
	}

	@Override
	public void run() {
		logger.info("ChronicleReader -> [{}] is running at [{}]", readerName, formatDateTime(YY_MM_DD_HH_MM_SS_SSS));
		if (params.getDelayReadTime() > 0)
			sleep(params.getDelayReadUnit(), params.getDelayReadTime());
		boolean waitingData = params.isWaitingData();
		boolean spinWaiting = params.isSpinWaiting();
		TimeUnit readIntervalUnit = params.getReadIntervalUnit();
		long readIntervalTime = params.getReadIntervalTime();
		for (;;) {
			if (isClose) {
				logger.info("ChronicleReader -> [{}] is cloesd, execute exit function at [{}]", readerName,
						formatDateTime(YY_MM_DD_HH_MM_SS_SSS));
				exit();
				break;
			}
			OUT next = null;
			try {
				next = next();
			} catch (ChronicleReadException e) {
				if (params.isReadFailLogging())
					logger.error("ChronicleReader -> [{}] call next throw exception: [{}] at [{}]", readerName,
							e.getMessage(), formatDateTime(YY_MM_DD_HH_MM_SS_SSS), e);
				if (params.isReadFailCrash())
					throw e;
			}
			if (next == null) {
				// 等待新数据
				if (waitingData) {
					// 非自旋等待, 进入休眠
					if (!spinWaiting) {
						sleep(readIntervalUnit, readIntervalTime);
					}
				} else {
					// 数据读取完毕, 退出线程
					exit();
					break;
				}
			} else {
				dataConsumer.accept(next);
			}
		}
	}

	private void exit() {
		final Runnable exitTask = params.getExitTask();
		if (exitTask != null) {
			if (params.isAsyncExit())
				// 异步执行退出函数
				startNewThread(readerName + "-exit", exitTask);
			else
				// 同步执行退出函数
				exitTask.run();

		}
		logger.info("ChronicleReader -> [{}] running exit at {}", readerName, formatDateTime(YY_MM_DD_HH_MM_SS_SSS));
	}

	@Override
	protected void close0() {
		// TODO NONE
	}

}
