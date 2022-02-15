package io.mercury.common.log;

import static io.mercury.common.log.Log4j2Configurator.getFileSizeOfMb;
import static io.mercury.common.log.Log4j2Configurator.getFilename;
import static io.mercury.common.log.Log4j2Configurator.getFolder;
import static io.mercury.common.log.Log4j2Configurator.getLogLevel;
import static io.mercury.common.log.Log4j2Configurator.setFileSizeOfMb;
import static io.mercury.common.log.Log4j2Configurator.setLogFilename;
import static io.mercury.common.log.Log4j2Configurator.setLogFolder;
import static io.mercury.common.log.Log4j2Configurator.setLogLevel;
import static io.mercury.common.log.Log4j2Configurator.LogLevel.ERROR;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import io.mercury.common.log.Log4j2Configurator.LogLevel;

/**
 * 
 * @author yellow013
 *
 */
public final class Log4j2LoggerFactory {

	/**
	 * default log filename
	 */
	public static final String DefaultFileName = "runtime-" + now().format(ofPattern("yyMMdd-HHmmss"));

	/**
	 * 
	 * @param <T>
	 * @param obj
	 * @return
	 */
	public static final org.slf4j.Logger getLogger(Object obj) {
		if (obj == null)
			return Log4j2LoggerFactory.getLogger("Unnamed-Logger");
		if (obj instanceof String)
			return Log4j2LoggerFactory.getLogger((String) obj);
		return Log4j2LoggerFactory.getLogger(obj.getClass());
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static final synchronized org.slf4j.Logger getLogger(Class<?> clazz) {
		checkAndSettings();
		return org.slf4j.LoggerFactory.getLogger(clazz);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static final synchronized org.slf4j.Logger getLogger(String name) {
		checkAndSettings();
		return org.slf4j.LoggerFactory.getLogger(name);
	}

	/**
	 * Internal Logger Settings
	 */
	private static final void checkAndSettings() {
		// 配置日志存储目录, 基于${user.home}
		String folder = getFolder();
		if (folder == null || folder.isEmpty())
			setLogFolder("default");
		// 配置日志文件名
		String filename = getFilename();
		if (filename == null || filename.isEmpty())
			setLogFilename(DefaultFileName);
		// 配置日志級別
		String level = getLogLevel();
		if (level == null || level.isEmpty())
			setLogLevel(ERROR);
		// 配置日志文件大小
		String sizeOfMb = getFileSizeOfMb();
		if (sizeOfMb == null || sizeOfMb.isEmpty())
			setFileSizeOfMb(255);
	}

	/**
	 * Logger Context对象
	 */
	private static volatile LoggerContext CTX;

	/**
	 * Configuration对象
	 */
	private static volatile Configuration CONF;

	/**
	 * 业务日志目录
	 */
	public static final String BizLogsDir = System.getProperty("java.io.tmpdir") + "/biz-logs";

	/**
	 * 启动一个动态的logger
	 *
	 * @param loggerName
	 * @return
	 */
	private static void buildAndUpdate(String loggerName, LogLevel logLevel) {
		if (CTX == null)
			CTX = (LoggerContext) LogManager.getContext(false);
		if (CONF == null)
			CONF = CTX.getConfiguration();
		// 基于时间分割日志文件
		TriggeringPolicy timeBased = TimeBasedTriggeringPolicy.newBuilder().withInterval(1).withModulate(true).build();

		// 基于文件大小分割日志文件
		TriggeringPolicy sizeBased = SizeBasedTriggeringPolicy.createPolicy("255MB");

		String logFile = BizLogsDir + File.separator + loggerName;

		RolloverStrategy strategy = DefaultRolloverStrategy.newBuilder().withMax("120").withConfig(CONF).build();

		// 日志样式
		Layout<String> layout = PatternLayout.newBuilder().withConfiguration(CONF).withCharset(UTF_8)
				.withHeader("The business log storage file with [" + loggerName + "]")
				.withPattern("%d{yy-MM-dd HH:mm:ss.SSS} %-5level [%t] [%C{1}.%M] %msg%n").build();

		Appender appender = RollingFileAppender.newBuilder().setName(loggerName).withFileName(logFile + ".log")
				.withFilePattern(logFile + ".%d{yyyyMMdd}.log").withAppend(true)
				.withPolicy(CompositeTriggeringPolicy.createPolicy(timeBased, sizeBased)).withStrategy(strategy)
				.setFilter(LogLevel.getCompositeFilterWith(logLevel)).setLayout(layout).setConfiguration(CONF).build();

		appender.start();

		LoggerConfig loggerConfig = LoggerConfig.createLogger(false, logLevel.getLevel(), loggerName, "true",
				new AppenderRef[] { AppenderRef.createAppenderRef(loggerName, null, null) }, null, CONF, null);
		loggerConfig.addAppender(appender, logLevel.getLevel(), null);

		CONF.addAppender(appender);
		CONF.addLogger(loggerName, loggerConfig);
		CTX.updateLoggers();
	}

	/**
	 * 关闭动态创建的logger，避免内存不够用或者文件打开太多
	 *
	 * @param loggerName
	 * @return
	 */
	public static void removeAndUpdate(String loggerName) {
		synchronized (CTX) {
			CONF.getAppender(loggerName).stop();
			CONF.getLoggerConfig(loggerName).removeAppender(loggerName);
			CONF.removeLogger(loggerName);
			CTX.updateLoggers();
		}
	}

	/**
	 * 获取具体的Business Logger
	 *
	 * @param loggerName
	 * @return
	 */
	public static org.slf4j.Logger getBizLogger(String loggerName) {
		return getBizLogger(loggerName, LogLevel.INFO);
	}

	/**
	 * 以指定日志级别, 获取具体的Business Logger
	 * 
	 * @param loggerName
	 * @param logLevel
	 * @return
	 */
	public static org.slf4j.Logger getBizLogger(String loggerName, LogLevel logLevel) {
		synchronized (CTX) {
			if (!CONF.getLoggers().containsKey(loggerName))
				buildAndUpdate(loggerName, logLevel);
		}
		return org.slf4j.LoggerFactory.getLogger(loggerName);
	}

	public static void main(String[] args) {

		System.out.println(System.getProperty("java.io.tmpdir"));

	}

}
