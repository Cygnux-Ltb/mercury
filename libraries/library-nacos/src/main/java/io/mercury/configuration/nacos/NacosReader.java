package io.mercury.configuration.nacos;

import java.io.IOException;
import java.util.Properties;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import io.mercury.common.config.PropertiesUtil;
import io.mercury.common.util.StringSupport;

/**
 * NACOS信息读取
 * 
 * 使用NACOS读取properties配置文件
 * 
 * @author: yellow013
 */
public class NacosReader {

	/**
	 * 
	 * @param serverAddr
	 * @param group
	 * @param dataId
	 * @return
	 * @throws NacosConnectionException
	 * @throws NacosReadException
	 */
	public static final Properties getProperties(String serverAddr, String group, String dataId)
			throws NacosConnectionException, NacosReadException {
		return getProperties0(connection(serverAddr), group, dataId);
	}

	/**
	 * 
	 * @param serverAddr
	 * @param namespace
	 * @param group
	 * @param dataId
	 * @return
	 * @throws NacosConnectionException
	 * @throws NacosReadException
	 */
	public static final Properties getProperties(String serverAddr, String namespace, String group, String dataId)
			throws NacosConnectionException, NacosReadException {
		return getProperties0(connection(serverAddr, namespace), group, dataId);
	}

	/**
	 * 
	 * @param serverAddr
	 * @param group
	 * @param dataId
	 * @return
	 * @throws NacosConnectionException
	 * @throws NacosReadException
	 */
	public static final String getSaved(String serverAddr, String group, String dataId)
			throws NacosConnectionException, NacosReadException {
		return getSaved0(connection(serverAddr), group, dataId);
	}

	/**
	 * 
	 * @param serverAddr
	 * @param namespace
	 * @param group
	 * @param dataId
	 * @return
	 * @throws NacosConnectionException
	 * @throws NacosReadException
	 */
	public static final String getSaved(String serverAddr, String namespace, String group, String dataId)
			throws NacosConnectionException, NacosReadException {
		return getSaved0(connection(serverAddr, namespace), group, dataId);
	}

	private static final ConfigService connection(String serverAddr) throws NacosConnectionException {
		Properties prop = new Properties();
		prop.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
		return createConfigService(prop);
	}

	private static final ConfigService connection(String serverAddr, String namespace) throws NacosConnectionException {
		Properties prop = new Properties();
		prop.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
		prop.put(PropertyKeyConst.NAMESPACE, namespace);
		return createConfigService(prop);
	}

	private static ConfigService createConfigService(Properties prop) throws NacosConnectionException {
		try {
			return ConfigFactory.createConfigService(prop);
		} catch (NacosException e) {
			throw new NacosConnectionException("ConfigFactory.createConfigService has error", e);
		}
	}

	private static Properties getProperties0(ConfigService service, String group, String dataId)
			throws NacosReadException {
		String saved = getSaved0(service, group, dataId);
		try {
			return PropertiesUtil.load(saved);
		} catch (IOException e) {
			throw new NacosReadException(e);
		}
	}

	private static String getSaved0(ConfigService service, String group, String dataId) throws NacosReadException {
		try {
			String saved = service.getConfig(dataId, group, 100000);
			if (StringSupport.isNullOrEmpty(saved)) {
				throw new NacosReadException("Read nacos saved is null or empty");
			}
			return saved;
		} catch (NacosException e) {
			throw new NacosReadException("ConfigService.getConfig call error", e);
		}
	}

}
