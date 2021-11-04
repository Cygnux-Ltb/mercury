package io.mercury.common.thread;

import static io.mercury.common.datetime.pattern.spec.DateTimePattern.YYYY_MM_DD_HH_MM_SS_SSS;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.log.CommonLoggerFactory;

@ThreadSafe
public abstract class RunnableComponent {

	private static final Logger log = CommonLoggerFactory.getLogger(RunnableComponent.class);

	/**
	 * Running flag
	 */
	protected final AtomicBoolean isRunning = new AtomicBoolean(false);

	/**
	 * Closed flag
	 */
	protected final AtomicBoolean isClosed = new AtomicBoolean(false);

	/**
	 * name
	 */
	protected String name = "Component-[" + YYYY_MM_DD_HH_MM_SS_SSS.format(LocalDateTime.now()) + "]";

	protected RunnableComponent() {
	}

	public boolean isRunning() {
		return isRunning.get();
	}

	public boolean isClosed() {
		return isClosed.get();
	}

	public void start() {
		if (isRunning.compareAndSet(false, true)) {
			try {
				start0();
			} catch (Exception e) {
				isRunning.set(false);
				log.error("Component start0 throw Exception -> {}", e.getMessage(), e);
				throw new RuntimeException("start0 function have exception", e);
			}
		} else {
			log.warn("Error in function call, Component -> [{}] already started", name);
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	@AbstractFunction
	protected abstract void start0() throws Exception;

	public void stop() {
		this.isRunning.set(false);
		if (isClosed.compareAndSet(false, true)) {
			try {
				stop0();
			} catch (Exception e) {
				log.error("Component stop0 throw Exception -> {}", e.getMessage(), e);
				throw new RuntimeException("stop0 function have exception", e);
			}
		} else {
			log.warn("Error in function call, Component -> [{}] already stopped", name);
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	@AbstractFunction
	protected abstract void stop0() throws Exception;

	public String getName() {
		return name;
	}

	protected abstract String getComponentType();

	protected void startWith(StartMode mode) {
		switch (mode) {
		case Auto:
			start();
			break;
		case Manual:
			log.info("{} -> {}, Start mode is [Manual], wating call start...", getComponentType(), name);
			break;
		case Delay:
			// TODO 添加延迟启动逻辑
		}
	}

	public static enum StartMode {

		Auto, Manual, Delay

	}

}
