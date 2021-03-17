package io.mercury.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import io.mercury.common.character.Charsets;

public final class PropertiesUtil {

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static Properties loadProperties(final URL url) throws IOException {
		Assertor.nonNull(url, "url");
		return loadProperties(url.openStream());
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Properties loadProperties(final File file) throws IOException {
		Assertor.nonNull(file, "file");
		return loadProperties(new FileInputStream(file));
	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static final Properties loadProperties(String text) throws IOException {
		return loadProperties(text, Charsets.UTF8);
	}

	/**
	 * 
	 * @param text
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static final Properties loadProperties(@Nonnull String text, @Nonnull Charset charset) throws IOException {
		Assertor.nonNull(text, "text");
		Assertor.nonNull(charset, "charset");
		if (StringUtil.nonEmpty(text))
			return loadProperties(new ByteArrayInputStream(text.getBytes(charset)));
		return new Properties();
	}

	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static Properties loadProperties(@Nonnull final InputStream is) throws IOException {
		Assertor.nonNull(is, "InputStream");
		Properties prop = new Properties();
		try (final InputStream in = is) {
			prop.load(in);
		}
		return prop;
	}

	/**
	 * 
	 * @param properties
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static final String getProperty(@Nonnull Properties properties, String key) throws NumberFormatException {
		return properties.getProperty(key, "");
	}

	/**
	 * 
	 * @param properties
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static final boolean getBoobleProperty(@Nonnull Properties properties, String key)
			throws NumberFormatException {
		String property = properties.getProperty(key);
		return StringUtil.isNullOrEmpty(property) ? false : Boolean.parseBoolean(property);
	}

	/**
	 * 
	 * @param properties
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static final int getIntProperty(@Nonnull Properties properties, String key) throws NumberFormatException {
		String property = properties.getProperty(key);
		return StringUtil.isNullOrEmpty(property) ? 0 : Integer.parseInt(property);
	}

	/**
	 * 
	 * @param properties
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static final double getDoubleProperty(@Nonnull Properties properties, String key)
			throws NumberFormatException {
		String property = properties.getProperty(key);
		return StringUtil.isNullOrEmpty(property) ? 0.0D : Double.parseDouble(property);
	}

	/**
	 * 
	 * @param properties
	 */
	public static final void printProperties(@Nonnull Properties properties) {
		printProperties(properties, null);
	}

	/**
	 * 
	 * @param properties
	 * @param log
	 */
	public static final void printProperties(@Nonnull Properties properties, Logger log) {
		List<Entry<Object, Object>> list = new ArrayList<>(properties.entrySet());
		for (int i = 0; i < list.size(); i++) {
			Entry<Object, Object> entry = list.get(i);
			if (log == null)
				System.out.println(
						"Property " + i + 1 + " : key -> " + entry.getKey() + ", value -> " + entry.getValue());
			else
				log.info("Property {} : key -> {}, value -> {}", (i + 1), entry.getKey(), entry.getValue());
		}

	}

}
