package io.mercury.persistence.chronicle.queue;

import static io.mercury.common.datetime.DateTimeUtil.datetimeOfSecond;
import static io.mercury.common.number.RandomNumber.randomUnsignedInt;
import static io.mercury.common.util.Assertor.nonNull;

import java.io.File;
import java.lang.Thread.State;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;

import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;
import org.slf4j.Logger;

import io.mercury.common.annotation.lang.ProtectedAbstractMethod;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.thread.RuntimeInterruptedException;
import io.mercury.common.thread.ShutdownHooks;
import io.mercury.common.thread.ThreadTool;
import io.mercury.common.util.StringUtil;
import io.mercury.persistence.chronicle.queue.AbstractChronicleReader.ReaderParam;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

public abstract class AbstractChronicleQueue<T, R extends AbstractChronicleReader<T>, A extends AbstractChronicleAppender<T>>
		implements net.openhft.chronicle.core.io.Closeable {

	private final String rootPath;
	private final String folder;
	private final boolean readOnly;
	private final long epoch;
	private final FileCycle fileCycle;
	private final int fileClearCycle;
	private final ObjIntConsumer<File> storeFileListener;

	private final File savePath;
	private final String queueName;

	protected final SingleChronicleQueue internalQueue;

	protected Logger logger = CommonLoggerFactory.getLogger(getClass());;

	AbstractChronicleQueue(QueueBuilder<?> builder) {
		this.rootPath = builder.rootPath;
		this.folder = builder.folder;
		this.readOnly = builder.readOnly;
		this.epoch = builder.epoch;
		this.fileCycle = builder.fileCycle;
		this.fileClearCycle = builder.fileClearCycle <= 0 ? 0 : builder.fileClearCycle < 3 ? 3 : builder.fileClearCycle;
		this.storeFileListener = builder.storeFileListener;
		this.logger = builder.logger != null ? builder.logger : logger;
		this.savePath = new File(rootPath + "chronicle-queue/" + folder);
		this.queueName = folder.replaceAll("/", "");
		this.internalQueue = buildChronicleQueue();
		buildClearThread();
		logger.info("{} initialized -> name==[{}], desc==[{}]", getClass().getSimpleName(), queueName,
				fileCycle.getDesc());
	}

	private SingleChronicleQueue buildChronicleQueue() {
		if (!savePath.exists())
			savePath.mkdirs();
		SingleChronicleQueueBuilder queueBuilder = SingleChronicleQueueBuilder.single(savePath)
				.rollCycle(fileCycle.getRollCycle()).readOnly(readOnly).storeFileListener(this::storeFileHandle);
		if (epoch > 0L)
			queueBuilder.epoch(epoch);
		// TODO 解决CPU缓存行填充问题
		ShutdownHooks.addShutdownHookThread("ChronicleQueue-Cleanup", this::shutdownHandle);
		return queueBuilder.build();
	}

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

	private AtomicInteger lastCycle;
	private ConcurrentMap<Integer, String> cycleFileMap;
	private Thread fileClearThread;
	private AtomicBoolean isClearRunning = new AtomicBoolean(true);

	private void buildClearThread() {
		if (fileClearCycle > 0) {
			this.lastCycle = new AtomicInteger();
			this.cycleFileMap = new ConcurrentHashMap<>();
			long delay = fileCycle.getSeconds() * fileClearCycle;
			this.fileClearThread = ThreadTool.startNewThread(() -> {
				do {
					try {
						ThreadTool.sleep(TimeUnit.SECONDS, delay);
					} catch (RuntimeInterruptedException e) {
						logger.info("Last execution fileClearTask");
						fileClearTask();
						logger.info("{} exit now", ThreadTool.currentThreadName());
					}
					runFileClearTask();
				} while (isClearRunning.get());
			}, queueName + "-FileClear");
			// singleThreadScheduleWithFixedDelay(delay, delay, TimeUnit.SECONDS,
			// this::runFileClearTask);
			logger.info("Build clear thread is finished");
		}
	}

	private void runFileClearTask() {
		if (isClearRunning.get())
			fileClearTask();
	}

	private void fileClearTask() {
		int last = lastCycle.get();
		int delOffset = last - fileClearCycle;
		logger.info("Execute clear schedule : lastCycle==[{}], delOffset==[{}]", last, delOffset);
		Set<Integer> keySet = cycleFileMap.keySet();
		for (int saveCycle : keySet) {
			if (saveCycle < delOffset) {
				String fileAbsolutePath = cycleFileMap.get(saveCycle);
				logger.info("Delete cycle file : cycle==[{}], fileAbsolutePath==[{}]", saveCycle, fileAbsolutePath);
				File file = new File(fileAbsolutePath);
				if (file.exists()) {
					if (!file.delete()) {
						logger.warn("File delete failure !!!");
						cycleFileMap.remove(saveCycle);
					}
				} else {
					logger.error("File not exists, Please check the ChronicleQueue save path : [{}]",
							savePath.getAbsolutePath());
				}
			}
		}
	}

	private void storeFileHandle(int cycle, File file) {
		logger.info("Released file : cycle==[{}], file==[{}]", cycle, file.getAbsolutePath());
		if (storeFileListener != null) {
			storeFileListener.accept(file, cycle);
		}
		if (fileClearCycle > 0) {
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
		if (isClosed())
			return;
		// 关闭外部访问器
		closeAllAccessor();
		// 关闭队列
		internalQueue.close();
		// 停止运行文件清理线程
		isClearRunning.set(false);
		// 中断正在休眠的清理线程
		if (fileClearThread != null)
			fileClearThread.interrupt();
		while (fileClearThread.getState() != State.TERMINATED)
			;
	}

	private String generateReaderName() {
		return queueName + "-Reader-" + randomUnsignedInt();
	}

	private static final String EMPTY_CONSUMER_MSG = "Reader consumer is an empty implementation";

	/**
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	public R createReader() throws IllegalStateException {
		return createReader(generateReaderName(), ReaderParam.defaultParam(), o -> logger.info(EMPTY_CONSUMER_MSG));
	}

	/**
	 * 
	 * @param dataConsumer
	 * @return
	 * @throws IllegalStateException
	 */
	public R createReader(Consumer<T> dataConsumer) throws IllegalStateException {
		return createReader(generateReaderName(), ReaderParam.defaultParam(), dataConsumer);
	}

	/**
	 * 
	 * @param readerName
	 * @param dataConsumer
	 * @return
	 * @throws IllegalStateException
	 */
	public R createReader(String readerName, Consumer<T> dataConsumer) throws IllegalStateException {
		return createReader(readerName, ReaderParam.defaultParam(), dataConsumer);
	}

	/**
	 * 
	 * @param readerParam
	 * @param dataConsumer
	 * @return
	 * @throws IllegalStateException
	 */
	public R createReader(ReaderParam readerParam, Consumer<T> dataConsumer) throws IllegalStateException {
		return createReader(generateReaderName(), readerParam, dataConsumer);
	}

	/**
	 * 
	 * @param readerName
	 * @param readerParam
	 * @param dataConsumer
	 * @return
	 * @throws IllegalStateException
	 */
	public R createReader(String readerName, ReaderParam readerParam, Consumer<T> dataConsumer)
			throws IllegalStateException {
		if (isClosed())
			throw new IllegalStateException("Cannot be create reader, Chronicle queue is closed");
		R reader = createReader(readerName, readerParam, logger, dataConsumer);
		addAccessor(reader);
		return reader;
	}

	/**
	 * 
	 * @param readerName
	 * @param readerParam
	 * @param logger
	 * @param consumer
	 * @return
	 * @throws IllegalStateException
	 */
	@ProtectedAbstractMethod
	protected abstract R createReader(String readerName, ReaderParam readerParam, Logger logger, Consumer<T> consumer)
			throws IllegalStateException;

	private String generateAppenderName() {
		return queueName + "-Appender-" + randomUnsignedInt();
	}

	/**
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	public A acquireAppender() throws IllegalStateException {
		return acquireAppender(generateAppenderName(), null);
	}

	/**
	 * 
	 * @param writerName
	 * @return
	 * @throws IllegalStateException
	 */
	public A acquireAppender(String appenderName) throws IllegalStateException {
		return acquireAppender(appenderName, null);
	}

	/**
	 * 
	 * @param dataProducer
	 * @return
	 * @throws IllegalStateException
	 */
	public A acquireAppender(Supplier<T> dataProducer) throws IllegalStateException {
		return acquireAppender(generateAppenderName(), dataProducer);
	}

	/**
	 * 
	 * @param writerName
	 * @param dataProducer
	 * @return
	 * @throws IllegalStateException
	 */
	public A acquireAppender(String appenderName, Supplier<T> dataProducer) throws IllegalStateException {
		if (isClosed())
			throw new IllegalStateException("Cannot be acquire appender, Chronicle queue is closed");
		A appender = acquireAppender(appenderName, logger, dataProducer);
		addAccessor(appender);
		return appender;
	}

	/**
	 * 
	 * @param writerName
	 * @param logger
	 * @param supplier
	 * @return
	 * @throws IllegalStateException
	 */
	@ProtectedAbstractMethod
	protected abstract A acquireAppender(String appenderName, Logger logger, Supplier<T> dataProducer)
			throws IllegalStateException;

	/**
	 * 
	 */
	private MutableLongObjectMap<CloseableChronicleAccessor> allocatedAccessor = MutableMaps.newLongObjectHashMap();

	/**
	 * 添加访问器
	 * @param accessor
	 */
	private void addAccessor(CloseableChronicleAccessor accessor) {
		synchronized (allocatedAccessor) {
			allocatedAccessor.put(accessor.allocationNo, accessor);
		}
	}

	/**
	 * 关闭全部访问器
	 */
	private void closeAllAccessor() {
		synchronized (allocatedAccessor) {
			for (CloseableChronicleAccessor accessor : allocatedAccessor.values()) {
				if (!accessor.isClosed())
					accessor.close();
			}
		}
	}

	/**
	 * Queue 构建器
	 * 
	 * @author yellow013
	 *
	 * @param <B>
	 */
	protected abstract static class QueueBuilder<B extends QueueBuilder<B>> {

		private String rootPath = SysProperties.JAVA_IO_TMPDIR + "/";
		private String folder = "auto-create-" + datetimeOfSecond() + "/";
		private boolean readOnly = false;
		private long epoch = 0L;
		private FileCycle fileCycle = FileCycle.SMALL_DAILY;
		private ObjIntConsumer<File> storeFileListener;
		private int fileClearCycle = 0;

		private Logger logger;

		public B rootPath(String rootPath) {
			this.rootPath = StringUtil.fixPath(rootPath);
			return self();
		}

		public B folder(String folder) {
			this.folder = StringUtil.fixPath(folder);
			return self();
		}

		public B readOnly(boolean readOnly) {
			this.readOnly = readOnly;
			return self();
		}

		public B epoch(long epoch) {
			this.epoch = epoch;
			return self();
		}

		public B fileCycle(FileCycle fileCycle) {
			this.fileCycle = nonNull(fileCycle, "fileCycle");
			return self();
		}

		public B fileClearCycle(int fileClearCycle) {
			this.fileClearCycle = fileClearCycle;
			return self();
		}

		public B storeFileListener(ObjIntConsumer<File> storeFileListener) {
			this.storeFileListener = nonNull(storeFileListener, "storeFileListener");
			return self();
		}

		public B logger(Logger logger) {
			this.logger = logger;
			return self();
		}

		@ProtectedAbstractMethod
		protected abstract B self();

	}

	/**
	 * 通用访问器抽象
	 * 
	 * @author yellow013
	 *
	 */
	static abstract class CloseableChronicleAccessor implements net.openhft.chronicle.core.io.Closeable {

		protected volatile boolean isClose = false;

		private final long allocationNo;

		protected CloseableChronicleAccessor(long allocationNo) {
			this.allocationNo = allocationNo;
		}

		@Override
		public void close() {
			this.isClose = true;
			close0();
		}

		// TODO 添加关闭访问器回调通知
		@Override
		public void notifyClosing() {

		}

		@Override
		public boolean isClosed() {
			return isClose;
		}

		@ProtectedAbstractMethod
		protected abstract void close0();

	}

}
