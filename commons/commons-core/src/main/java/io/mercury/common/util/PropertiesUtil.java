package io.mercury.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import io.mercury.common.character.Charsets;

public final class PropertiesUtil {

	public static final Properties toProperties(String text) throws IOException {
		return toProperties(text, Charsets.UTF8);
	}

	public static final Properties toProperties(String text, Charset charset) throws IOException {
		Properties prop = new Properties();
		if (StringUtil.isNullOrEmpty(text))
			return prop;
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes(charset))) {
			prop.load(inputStream);
			return prop;
		} catch (IOException e) {
			throw e;
		}
	}

	public static final String getProperty(Properties prop, String key) throws NumberFormatException {
		return prop.getProperty(key, "");
	}

	public static final boolean getBoobleProperty(Properties prop, String key) throws NumberFormatException {
		String property = prop.getProperty(key);
		return StringUtil.isNullOrEmpty(property) ? false : Boolean.parseBoolean(property);
	}

	public static final int getIntProperty(Properties prop, String key) throws NumberFormatException {
		String property = prop.getProperty(key);
		return StringUtil.isNullOrEmpty(property) ? 0 : Integer.parseInt(property);
	}

	public static final double getDoubleProperty(Properties prop, String key) throws NumberFormatException {
		String property = prop.getProperty(key);
		return StringUtil.isNullOrEmpty(property) ? 0.0D : Double.parseDouble(property);
	}

	public static final void printProperties(@Nonnull Properties prop) {
		printProperties(prop, null, "Property");
	}

	public static final void printProperties(@Nonnull Properties prop, Logger log) {
		printProperties(prop, log, "Property");
	}

	public static final void printProperties(@Nonnull Properties prop, Logger log, String msg) {
		if (log == null)
			prop.forEach((key, value) -> System.out
					.println(StringUtil.toString(msg) + " : key -> " + key + ", value -> " + value));
		else
			prop.forEach((key, value) -> log.info("{} : key -> {}, value -> {}", msg, key, value));
	}

}
