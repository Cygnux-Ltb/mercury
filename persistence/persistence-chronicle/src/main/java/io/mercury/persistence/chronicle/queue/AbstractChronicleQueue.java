package io.mercury.persistence.chronicle.queue;

import static io.mercury.common.datetime.DateTimeUtil.datetimeOfSecond;
import static io.mercury.common.number.ThreadSafeRandoms.randomUnsignedInt;

import java.io.File;
import java.lang.Thread.State;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.collections.api.map.ConcurrentMutableMap;
import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.thread.RuntimeInterruptedException;
import io.mercury.common.thread.ShutdownHooks;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.common.util.StringSupport;
import io.mercury.persistence.chronicle.queue.AbstractChronicleReader.ReaderParam;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

@Immutable
public abstract class AbstractChronicleQueue<T, RT extends AbstractChronicleReader<T>, AT extends AbstractChronicleAppender<T>>
		implements net.openhft.chronicle.core.io.Closeable {

	private final String rootPath;
	private final String folder;
	private final boolean readOnly;
	private final long epoch;
	private final FileCycle fileCycle;

	// 文件清理周期
	private final int fileClearCycle;
	// 存储文件释放回调
	private final ObjIntConsumer<File> storeFileListener;

	private final File savePath;
	private final String queueName;

	protected final SingleChronicleQueue internalQueue;

	protected Logger logger = CommonLoggerFactory.getLogger(getClass());;

	AbstractChronicleQueue(AbstractQueueBuilder<?> builder) {
		this.rootPath = builder.rootPath;
		this.folder = builder.folder;
		this.readOnly = builder.readOnly;
		this.epoch = builder.epoch;
		this.fileCycle = builder.fileCycle;
		this.fileClearCycle = builder.fileClearCycle <= 0 ? 0 : builder.fileClearCycle < 3 ? 3 : builder.fileClearCycle;
		this.storeFileListener = builder.storeFileListener;
		if (builder.logger != null) {
			this.logger = builder.logger;
		}
		this.savePath = new File(rootPath + "chronicle-queue/" + folder);
		this.queueName = folder.replaceAll("/", "");
		this.internalQueue = buildChronicleQueue();
		createFileClearThread();
		logger.info("{} initialized -> name==[{}], desc==[{}]", getClass().getSimpleName(), queueName,
				fileCycle.getDesc());
	}

	private SingleChronicleQueue buildChronicleQueue() {
		if (!savePath.exists()) {
			// 创建存储路径
			savePath.mkdirs();
		}
		final SingleChronicleQueueBuilder builder = SingleChronicleQueueBuilder.single(savePath)
				// 文件滚动周期
				.rollCycle(fileCycle.getRollCycle())
				// 是否只读
				.readOnly(readOnly)
				// 设置时区
				// .rollTimeZone(rollTimeZone)
				// 文件存储回调
				.storeFileListener(this::storeFileHandle);
		if (epoch > 0L) {
			builder.epoch(epoch);
		}
		// TODO 待解决CPU缓存行填充问题
		ShutdownHooks.addShutdownHook("ChronicleQueue-Cleanup", this::shutdownHandle);
		return builder.build();
	}

	/**
	 * 关闭
	 */
	private void shutdownHandle() {
		// System.out.println("ChronicleQueue ShutdownHook of " + name + " start");
		logger.info("ChronicleQueue [{}] shutdown hook started", queueName);
		try {
			close();
		} catch (Exception e) {
			logger.error("ChronicleQueue [{}] shutdown hook throw exception: {}", queueName, e.getMessage(), e);
		}
		// System.out.println("ChronicleQueue ShutdownHook of " + name + " finished");
		logger.info("ChronicleQueue [{}] shutdown hook finished", queueName);
	}

	/**
	 ******************************** 文件清理程序 START ********************************
	 */
	// 最后文件周期
	private AtomicInteger lastCycle;
	// 周期文件存储Map
	private ConcurrentMutableMap<Integer, String> cycleFileMap;
	// 周期文件清理线程
	private Thread fileClearThread;
	// 周期文件清理线程运行状态
	private AtomicBoolean isClearRunning = new AtomicBoolean(true);

	/**
	 * 创建文件清理线程
	 */
	private void createFileClearThread() {
		if (fileClearCycle > 0) {
			this.lastCycle = new AtomicInteger();
			this.cycleFileMap = MutableMaps.newConcurrentHashMap();
			// 周期文件清理间隔
			long delay = fileCycle.getSeconds() * fileClearCycle;
			// 创建文件清理线程
			this.fileClearThread = Threads.startNewThread(queueName + "-FileClearThread", () -> {
				do {
					try {
						SleepSupport.sleep(TimeUnit.SECONDS, delay);
					} catch (RuntimeInterruptedException e) {
						logger.info("Last execution fileClearTask");
						fileClearTask();
						logger.info("Thread -> {} quit now", Threads.getCurrentThreadName());
					}
					if (isClearRunning.get()) {
						fileClearTask();
					}
				} while (isClearRunning.get());
			});
			// singleThreadScheduleWithFixedDelay(delay, delay, TimeUnit.SECONDS,
			// this::runFileClearTask);
			logger.info("Build clear thread is finished");
		}
	}

	/**
	 * 文件清理任务
	 */
	private void fileClearTask() {
		int last = lastCycle.get();
		// 计算需要删除的基准线
		int deleteBaseline = last - fileClearCycle;
		logger.info("Execute clear schedule : lastCycle==[{}], deleteBaseline==[{}]", last, deleteBaseline);
		// 获取全部存储文件的Key
		Set<Integer> keySet = cycleFileMap.keySet();
		for (int saveCycle : keySet) {
			// 小于基准线的文件被删除
			if (saveCycle < deleteBaseline) {
				String fileAbsolutePath = cycleFileMap.get(saveCycle);
				logger.info("Delete cycle file : cycle==[{}], fileAbsolutePath==[{}]", saveCycle, fileAbsolutePath);
				File file = new File(fileAbsolutePath);
				if (file.exists()) {
					// 删除文件
					if (file.delete()) {
						cycleFileMap.remove(saveCycle);
						logger.info("File : [{}] successfully deleted", file.getAbsolutePath());
					} else {
						logger.warn("File : [{}] delete failure", fileAbsolutePath);
					}
				} else {
					logger.error("File not exists, Please check the ChronicleQueue save path : [{}]",
							savePath.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * 
	 * @param cycle
	 * @param file
	 */
	private void storeFileHandle(int cycle, File file) {
		logger.info("Released file : cycle==[{}], file==[{}]", cycle, file.getAbsolutePath());
		if (storeFileListener != null) {
			// 调用存储文件释放回调
			storeFileListener.accept(file, cycle);
		}
		if (fileClearCycle > 0) {
			// 如果设置了文件清理周期, 记录文件路径和最后一个文件的周期
			cycleFileMap.put(cycle, file.getAbsolutePath());
			lastCycle.set(cycle);
		}
	}

	public String queueName() {
		return queueName;
	}

	public String rootPath() {
		return rootPath;
	}

	public String folder() {
		return folder;
	}

	public File savePath() {
		return savePath;
	}

	public FileCycle fileCycle() {
		return fileCycle;
	}

	public SingleChronicleQueue internalQueue() {
		return internalQueue;
	}

	public boolean isClosed() {
		return internalQueue.isClosed();
	}

	@Override
	public void close() {
		if (isClosed()) {
			return;
		}
		// 关闭外部访问器
		closeAllAccessor();
		// 关闭队列
		internalQueue.close();
		// 停止运行文件清理线程
		isClearRunning.set(false);
		// 中断正在休眠的文件清理线程
		if (fileClearThread != null) {
			fileClearThread.interrupt();
			while (fileClearThread.getState() != State.TERMINATED)
				SleepSupport.sleep(5);
		}
	}

	private String generateReaderName() {
		return queueName + "-reader-" + randomUnsignedInt();
	}

	private static final String EMPTY_CONSUMER_MSG = "Reader consumer is an empty implementation.";

	/**
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	public RT createReader() throws IllegalStateException {
		return createReader(generateReaderName(), ReaderParam.defaultParam(), t -> logger.info(EMPTY_CONSUMER_MSG));
	}

	/**
	 * 
	 * @param dataConsumer
	 * @return
	 * @throws IllegalStateException
	 */
	public RT createReader(@Nonnull Consumer<T> dataConsumer) throws IllegalStateException {
		return createReader(generateReaderName(), ReaderParam.defaultParam(), dataConsumer);
	}

	/**
	 * 
	 * @param readerName
	 * @param dataConsumer
	 * @return
	 * @throws IllegalStateException
	 */
	public RT createReader(@Nonnull String readerName, @Nonnull Consumer<T> dataConsumer) throws IllegalStateException {
		return createReader(readerName, ReaderParam.defaultParam(), dataConsumer);
	}

	/**
	 * 
	 * @param param
	 * @param dataConsumer
	 * @return
	 * @throws IllegalStateException
	 */
	public RT createReader(@Nonnull ReaderParam param, @Nonnull Consumer<T> dataConsumer) throws IllegalStateException {
		return createReader(generateReaderName(), param, dataConsumer);
	}

	/**
	 * 
	 * @param readerName
	 * @param param
	 * @param dataConsumer
	 * @return
	 * @throws IllegalStateException
	 */
	public RT createReader(@Nonnull String readerName, @Nonnull ReaderParam param, @Nonnull Consumer<T> dataConsumer)
			throws IllegalStateException {
		if (isClosed()) {
			throw new IllegalStateException("Cannot be create reader, Chronicle queue is closed");
		}
		Assertor.nonNull(readerName, "readerName");
		Assertor.nonNull(param, "param");
		Assertor.nonNull(dataConsumer, "dataConsumer");
		RT reader = createReader(readerName, param, logger, dataConsumer);
		addAccessor(reader);
		return reader;
	}

	/**
	 * 
	 * @param readerName
	 * @param param
	 * @param log
	 * @param consumer
	 * @return
	 * @throws IllegalStateException
	 */
	@AbstractFunction
	protected abstract RT createReader(@Nonnull String readerName, @Nonnull ReaderParam param, @Nonnull Logger log,
			@Nonnull Consumer<T> consumer) throws IllegalStateException;

	/**
	 * 
	 * @return
	 */
	private String generateAppenderName() {
		return queueName + "-appender-" + randomUnsignedInt();
	}

	/**
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	public AT acquireAppender() throws IllegalStateException {
		return acquireAppender(generateAppenderName(), null);
	}

	/**
	 * 
	 * @param appenderName
	 * @return
	 * @throws IllegalStateException
	 */
	public AT acquireAppender(@Nonnull String appenderName) throws IllegalStateException {
		Assertor.nonNull(appenderName, "appenderName");
		return acquireAppender(appenderName, null);
	}

	/**
	 * 
	 * @param dataProducer
	 * @return
	 * @throws IllegalStateException
	 */
	public AT acquireAppender(@Nonnull Supplier<T> dataProducer) throws IllegalStateException {
		Assertor.nonNull(dataProducer, "dataProducer");
		return acquireAppender(generateAppenderName(), dataProducer);
	}

	/**
	 * 
	 * @param appenderName
	 * @param dataProducer
	 * @return
	 * @throws IllegalStateException
	 */
	public AT acquireAppender(@Nonnull String appenderName, @CheckForNull Supplier<T> dataProducer)
			throws IllegalStateException {
		if (isClosed())
			throw new IllegalStateException("Cannot be acquire appender, Chronicle queue is closed");
		Assertor.nonNull(appenderName, "appenderName");
		AT appender = acquireAppender(appenderName, logger, dataProducer);
		addAccessor(appender);
		return appender;
	}

	/**
	 * 
	 * @param appenderName
	 * @param logger
	 * @param supplier
	 * @return
	 * @throws IllegalStateException
	 */
	@AbstractFunction
	protected abstract AT acquireAppender(@Nonnull String appenderName, @Nonnull Logger logger,
			@CheckForNull Supplier<T> dataProducer) throws IllegalStateException;

	/**
	 * 已分配的访问器
	 */
	private ConcurrentMutableMap<Long, CloseableChronicleAccessor> allocatedAccessor = MutableMaps
			.newConcurrentHashMap();

	/**
	 * 添加访问器
	 * 
	 * @param accessor
	 */
	private void addAccessor(CloseableChronicleAccessor accessor) {
		allocatedAccessor.put(accessor.allocateSeq, accessor);
	}

	/**
	 * 关闭全部访问器
	 */
	private void closeAllAccessor() {
		for (CloseableChronicleAccessor accessor : allocatedAccessor.values()) {
			if (!accessor.isClosed())
				accessor.close();
		}
	}

	/**
	 * Queue 构建器
	 * 
	 * @author yellow013
	 *
	 * @param <B>
	 */
	protected abstract static class AbstractQueueBuilder<B extends AbstractQueueBuilder<B>> {

		private String rootPath = SysProperties.JAVA_IO_TMPDIR + "/";
		private String folder = "auto-create-" + datetimeOfSecond() + "/";
		private boolean readOnly = false;
		private long epoch = 0L;
		private FileCycle fileCycle = FileCycle.SMALL_DAILY;
		private ObjIntConsumer<File> storeFileListener;
		private int fileClearCycle = 0;

		private Logger logger;

		public B saveToUserHome() {
			rootPath(SysProperties.USER_HOME);
			return self();
		}

		public B saveToTmpDir() {
			rootPath(SysProperties.JAVA_IO_TMPDIR);
			return self();
		}

		public B rootPath(@Nonnull String rootPath) {
			this.rootPath = StringSupport.fixPath(rootPath);
			return self();
		}

		public B folder(@Nonnull String folder) {
			this.folder = StringSupport.fixPath(folder);
			return self();
		}

		public B topic(@Nonnull String topic) {
			return topic(topic, "");
		}

		public B topic(@Nonnull String topic, @Nullable String... subtopics) {
			this.folder = StringSupport.fixPath(topic);
			return self();
		}

		public B enableReadOnly() {
			this.readOnly = true;
			return self();
		}

		public B epoch(long epoch) {
			this.epoch = epoch;
			return self();
		}

		public B fileCycle(@Nonnull FileCycle fileCycle) {
			Assertor.nonNull(fileCycle, "fileCycle");
			this.fileCycle = fileCycle;
			return self();
		}

		public B fileClearCycle(int fileClearCycle) {
			this.fileClearCycle = fileClearCycle;
			return self();
		}

		public B storeFileListener(@Nonnull ObjIntConsumer<File> storeFileListener) {
			Assertor.nonNull(storeFileListener, "storeFileListener");
			this.storeFileListener = storeFileListener;
			return self();
		}

		public B logger(@Nonnull Logger logger) {
			Assertor.nonNull(logger, "logger");
			this.logger = logger;
			return self();
		}

		@AbstractFunction
		protected abstract B self();

	}

	/**
	 * 通用访问器抽象类
	 * 
	 * @author yellow013
	 *
	 */
	public static abstract class CloseableChronicleAccessor implements net.openhft.chronicle.core.io.Closeable {

		protected volatile boolean isClose = false;

		private final long allocateSeq;

		protected CloseableChronicleAccessor(long allocateSeq) {
			this.allocateSeq = allocateSeq;
		}

		@Override
		public void close() {
			this.isClose = true;
			close0();
		}

		@Override
		public void notifyClosing() {
			// TODO 添加关闭访问器回调通知
		}

		@Override
		public boolean isClosed() {
			return isClose;
		}

		@AbstractFunction
		protected abstract void close0();

	}

}
