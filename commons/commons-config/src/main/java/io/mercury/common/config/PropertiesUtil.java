package io.mercury.common.config;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

import io.mercury.common.character.Charsets;
import io.mercury.common.lang.Assertor;
import io.mercury.common.sys.LibraryPathManager;
import io.mercury.common.util.StringSupport;

public final class PropertiesUtil {

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static Properties load(final URL url) throws IOException {
		Assertor.nonNull(url, "url");
		return load(url.openStream());
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Properties load(@Nonnull final File file) throws IOException {
		Assertor.nonNull(file, "file");
		return load(new FileInputStream(file));
	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static final Properties load(@Nonnull final String text) throws IOException {
		return load(text, Charsets.UTF8);
	}

	/**
	 * 
	 * @param text
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static final Properties load(@Nonnull final String text, @Nonnull final Charset charset) throws IOException {
		Assertor.nonNull(text, "text");
		Assertor.nonNull(charset, "charset");
		if (StringSupport.nonEmpty(text))
			return load(new ByteArrayInputStream(text.getBytes(charset)));
		return new Properties();
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static Properties load(@Nonnull final InputStream inputStream) throws IOException {
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
	 */
	public static final String getProperty(@Nonnull final Properties props, final String key) {
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
		return StringSupport.isNullOrEmpty(prop) ? false : parseBoolean(prop);
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
		return StringSupport.isNullOrEmpty(prop) ? 0 : parseInt(prop);
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
		return StringSupport.isNullOrEmpty(prop) ? 0.0D : parseDouble(prop);
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
		ArrayList<Entry<Object, Object>> list = new ArrayList<>(props.entrySet());
		for (int i = 0; i < list.size(); i++) {
			Entry<Object, Object> entry = list.get(i);
			if (log == null)
				out.println("Property " + i + " : key -> " + entry.getKey() + ", value -> " + entry.getValue());
			else
				log.info("Property {} : key -> {}, value -> {}", i, entry.getKey(), entry.getValue());
		}

	}

	public static void main(String[] args) {
		System.out.println("---old---");
		PropertiesUtil.showProperties(System.getProperties());
		try {
			LibraryPathManager.addLibraryDir("~/java_lib");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("---new---");
		PropertiesUtil.showProperties(System.getProperties());
	}

}
