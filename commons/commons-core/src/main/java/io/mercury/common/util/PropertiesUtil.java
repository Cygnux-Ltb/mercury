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
import javax.annotation.Nullable;

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
	public static Properties loadProperties(@Nonnull final File file) throws IOException {
		Assertor.nonNull(file, "file");
		return loadProperties(new FileInputStream(file));
	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static final Properties loadProperties(@Nonnull final String text) throws IOException {
		return loadProperties(text, Charsets.UTF8);
	}

	/**
	 * 
	 * @param text
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static final Properties loadProperties(@Nonnull final String text, @Nonnull final Charset charset)
			throws IOException {
		Assertor.nonNull(text, "text");
		Assertor.nonNull(charset, "charset");
		if (StringUtil.nonEmpty(text))
			return loadProperties(new ByteArrayInputStream(text.getBytes(charset)));
		return new Properties();
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static Properties loadProperties(@Nonnull final InputStream inputStream) throws IOException {
		Assertor.nonNull(inputStream, "inputStream");
		Properties props = new Properties();
		try (final InputStream in = inputStream) {
			props.load(in);
		}
		return props;
	}

	/**
	 * 
	 * @param props
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static final String getProperty(@Nonnull final Properties props, final String key)
			throws NumberFormatException {
		return props.getProperty(key, "");
	}

	/**
	 * 
	 * @param props
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static final boolean getBoobleProperty(@Nonnull final Properties props, final String key)
			throws NumberFormatException {
		String prop = props.getProperty(key);
		return StringUtil.isNullOrEmpty(prop) ? false : Boolean.parseBoolean(prop);
	}

	/**
	 * 
	 * @param props
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static final int getIntProperty(@Nonnull final Properties props, final String key)
			throws NumberFormatException {
		String prop = props.getProperty(key);
		return StringUtil.isNullOrEmpty(prop) ? 0 : Integer.parseInt(prop);
	}

	/**
	 * 
	 * @param props
	 * @param key
	 * @return
	 * @throws NumberFormatException
	 */
	public static final double getDoubleProperty(@Nonnull final Properties props, final String key)
			throws NumberFormatException {
		String prop = props.getProperty(key);
		return StringUtil.isNullOrEmpty(prop) ? 0.0D : Double.parseDouble(prop);
	}

	/**
	 * 
	 * @param props
	 */
	public static final void showProperties(@Nonnull final Properties props) {
		showProperties(props, null);
	}

	/**
	 * 
	 * @param props
	 * @param log
	 */
	public static final void showProperties(@Nonnull final Properties props, @Nullable final Logger log) {
		List<Entry<Object, Object>> list = new ArrayList<>(props.entrySet());
		for (int i = 0; i < list.size(); i++) {
			Entry<Object, Object> entry = list.get(i);
			if (log == null)
				System.out.println(
						"Property " + (i + 1) + " : key -> " + entry.getKey() + ", value -> " + entry.getValue());
			else
				log.info("Property {} : key -> {}, value -> {}", (i + 1), entry.getKey(), entry.getValue());
		}

	}

}
