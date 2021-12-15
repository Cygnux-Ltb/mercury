package io.mercury.common.thread;

import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSS;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.log.Log4j2LoggerFactory;

@ThreadSafe
public abstract class RunnableComponent {

	private static final Logger log = Log4j2LoggerFactory.getLogger(RunnableComponent.class);

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
	protected String name = "Component-[" + YYYYMMDD_L_HHMMSSSSS.format(LocalDateTime.now()) + "]";

	protected RunnableComponent() {
	}

	/**
	 * 
	 * @return isRunning
	 */
	public boolean isRunning() {
		return isRunning.get();
	}

	/**
	 * 
	 * @return isClosed
	 */
	public boolean isClosed() {
		return isClosed.get();
	}

	/**
	 * 
	 * @throws Exception
	 */
	@AbstractFunction
	protected abstract void start0() throws Exception;

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
	protected abstract void stop0() throws Exception;

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

	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	@AbstractFunction
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
			break;
		}
	}

	/**
	 * 
	 * @author yellow013
	 */
	public static enum StartMode {
		Auto, Manual, Delay
	}

}
