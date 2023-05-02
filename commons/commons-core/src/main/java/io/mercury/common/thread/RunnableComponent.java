package io.mercury.common.thread;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.concurrent.ThreadSafe;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSS;

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
     * @return isRunning
     */
    public boolean isRunning() {
        return isRunning.get();
    }

    /**
     * @return isClosed
     */
    public boolean isClosed() {
        return isClosed.get();
    }

    /**
     * @throws Exception e
     */
    @AbstractFunction
    protected abstract void start0() throws Exception;

    /**
     * 启动组件
     */
    public void start() {
        if (isRunning.compareAndSet(false, true)) {
            try {
                start0();
            } catch (Exception e) {
                isRunning.set(false);
                log.error("Component -> [{}] start0 throw exception -> {}", name, e.getMessage(), e);
                throw new ComponentRunningException(name, e.getMessage(), e);
            }
        } else
            log.warn("Error call, Component -> [{}] already started", name);
    }

    /**
     * @throws Exception e
     */
    @AbstractFunction
    protected abstract void stop0() throws Exception;

    /**
     * 停止运行
     */
    public void stop() {
        isRunning.set(false);
        if (isClosed.compareAndSet(false, true)) {
            try {
                stop0();
            } catch (Exception e) {
                log.error("Component -> {} stop0 throw exception -> {}", name, e.getMessage(), e);
                throw new ComponentStopException(name, e.getMessage(), e);
            }
        } else
            log.warn("Error call, Component -> [{}] already stopped", name);
    }

    public String getName() {
        return name;
    }


    protected void startWith(final StartMode mode) {
        if (mode.immediately) {
            start();
        } else if (mode.delayMillis > 0) {
            ThreadSupport.startNewMaxPriorityThread("", () -> {
                SleepSupport.sleep(mode.delayMillis);
                this.start();
            });
        } else {
            log.info("{}, Start mode is [Manual], waiting call start...", name);
        }
    }

    /**
     * @author yellow013
     */
    public static final class StartMode {

        public static StartMode auto() {
            return new StartMode(true, 0L);
        }

        public static StartMode manual() {
            return new StartMode(false, 0L);
        }

        public static StartMode delay(long delayMillis) {
            return new StartMode(false, delayMillis < 1 ? 1 : delayMillis);
        }

        private final boolean immediately;

        private final long delayMillis;

        private StartMode(boolean immediately, long delayMillis) {
            this.immediately = immediately;
            this.delayMillis = delayMillis;
        }

        @Override
        public String toString() {
            if (immediately)
                return "Auto";
            else if (delayMillis > 0)
                return "Delay(" + delayMillis + "ms)";
            else
                return "Manual";
        }

    }

    /**
     * @author yellow013
     */
    public static class ComponentRunningException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = -5059741051462133930L;

        public ComponentRunningException(String componentName, String msg, Throwable cause) {
            super("Component -> [" + componentName + "] start failed, Msg: " + msg, cause);
        }

    }

    /**
     * @author yellow013
     */
    public static class ComponentStopException extends RuntimeException {

        @Serial
        private static final long serialVersionUID = -5298836449881727747L;

        public ComponentStopException(String componentName, String msg, Throwable cause) {
            super("Component -> [" + componentName + "] start failed, Msg: " + msg, cause);
        }

    }

}
