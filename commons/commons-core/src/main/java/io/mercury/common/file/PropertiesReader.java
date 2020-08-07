package io.mercury.common.io;

import static io.mercury.common.util.StringUtil.notDecimal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.slf4j.Logger;

import io.mercury.common.collections.MutableMaps;
import io.mercury.common.log.CommonLoggerFactory;

public final class PropertiesReader {

	private static final Logger log = CommonLoggerFactory.getLogger(PropertiesReader.class);

	// fileName-propertyName -> value
	private static final MutableMap<String, String> PropertiesItemMap = MutableMaps.newUnifiedMap();

	// fileName -> properties
	private static final MutableMap<String, Properties> PropertiesMap = MutableMaps.newUnifiedMap();

	private static final String FILE_SUFFIX = ".properties";

	static {
		MutableSet<File> allPropFile = FileLoader.depthFirst(
				new File(PropertiesReader.class.getResource("/").getPath()),
				file -> file.getName().endsWith(FILE_SUFFIX));
		try {
			for (File propFile : allPropFile) {
				log.info("Properties file -> [{}] start load", propFile);
				String fileName = propFile.getName();
				Properties prop = new Properties();
				prop.load(new FileInputStream(propFile));
				PropertiesMap.put(deleteSuffix(fileName), prop);
				for (String propName : prop.stringPropertyNames()) {
					String propKey = mergePropertiesKey(fileName, propName);
					String propValue = prop.getProperty(propName);
					String currentValue = PropertiesItemMap.get(propKey);
					if (currentValue != null) {
						log.warn("Current item value modified, propKey==[{}], currentValue==[{}], propValue==[{}]",
								propKey, currentValue, propValue);
					}
					log.info("Put property item, propKey==[{}], propValue==[{}]", propKey, propValue);
					PropertiesItemMap.put(propKey, propValue);
				}
			}
		} catch (FileNotFoundException e) {
			log.error("File -> [{}] is not found");
			throw new RuntimeException(e);
		} catch (IOException e) {
			log.error("File -> [{}] load failed");
			throw new RuntimeException(e);
		}
	}

	private static String mergePropertiesKey(String fileName, String propName) {
		return new StringBuilder(24).append(deleteSuffix(fileName)).append("-").append(propName).toString();
	}

	private static String deleteSuffix(String fileName) {
		if (fileName == null)
			return "";
		if (fileName.endsWith(FILE_SUFFIX))
			return fileName.split(FILE_SUFFIX)[0];
		return fileName;
	}

	public static Properties getProperty(String fileName) {
		Properties properties = PropertiesMap.get(deleteSuffix(fileName));
		if (properties == null)
			return new Properties();
		return properties;
	}

	public static String getProperty(String fileName, String propName) {
		String mergeKey = mergePropertiesKey(fileName, propName);
		String propValue = PropertiesItemMap.get(mergeKey);
		if (propValue == null) {
			log.error("Property name -> [{}] is not found of file name -> [{}], mergeKey==[{}]", propName, fileName,
					mergeKey);
			throw new RuntimeException("Read property error.");
		}
		return propValue;
	}

	public static int getIntProperty(String fileName, String propName) {
		String propValue = getProperty(fileName, propName);
		if (notDecimal(propValue)) {
			log.error("Property name -> [{}] is not decimal of file name -> [{}]", propName, fileName);
			throw new NumberFormatException("Read property error.");
		}
		try {
			return Integer.parseInt(propValue);
		} catch (NumberFormatException e) {
			log.error("Property name -> [{}], value -> [{}] from file name -> [{}] throw NumberFormatException",
					propName, propValue, fileName, e);
			throw e;
		}
	}

	public static long getLongProperty(String fileName, String propName) {
		String propValue = getProperty(fileName, propName);
		if (notDecimal(propValue)) {
			log.error("Property name -> [{}] is not decimal of file name -> [{}]", propName, fileName);
			throw new NumberFormatException("Read property error.");
		}
		try {
			return Long.parseLong(propValue);
		} catch (NumberFormatException e) {
			log.error("Property name -> [{}], value -> [{}] from file name -> [{}] throw NumberFormatException",
					propName, propValue, fileName, e);
			throw e;
		}
	}

	public static double getDoubleProperty(String fileName, String propName) {
		String propValue = getProperty(fileName, propName);
		if (notDecimal(propValue)) {
			log.error("Property name -> [{}] is not decimal of file name -> [{}]", propName, fileName);
			throw new NumberFormatException("Read property error.");
		}
		try {
			return Double.parseDouble(propValue);
		} catch (NumberFormatException e) {
			log.error("Property name -> [{}], value -> [{}] from file name -> [{}] throw NumberFormatException",
					propName, propValue, fileName, e);
			throw e;
		}
	}

	public static void main(String[] args) {

		File file = new File("");
		System.out.println(file.getPath());
		System.out.println(file.getAbsolutePath());
		File[] listFiles = file.listFiles();
		if (listFiles != null)
			for (File file2 : listFiles) {
				System.out.println(file2.getName());
			}
		System.out.println(System.getProperty("user.dir"));

	}

}
