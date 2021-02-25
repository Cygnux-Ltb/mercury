package io.mercury.configuration.nacos;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.PropertiesUtil;
import io.mercury.common.util.StringUtil;

/**
 * NACOS信息读取
 * 
 * 使用NACOS读取properties配置文件
 * 
 * @author: yellow013
 */
public class NacosReader {

	private final ConfigService service;

	private static final Logger log = CommonLoggerFactory.getLogger(NacosReader.class);

	public static final Properties getProperties(String serverAddr, String group, String dataId)
			throws NacosReadException {
		return connection(serverAddr).getProperties0(group, dataId);
	}

	public static final Properties getProperties(String serverAddr, String namespace, String group, String dataId)
			throws NacosReadException {
		return connection(serverAddr, namespace).getProperties0(group, dataId);
	}

	public static final String getSaved(String serverAddr, String group, String dataId) throws NacosReadException {
		return connection(serverAddr).getSaved0(group, dataId);
	}

	public static final String getSaved(String serverAddr, String namespace, String group, String dataId)
			throws NacosReadException {
		return connection(serverAddr, namespace).getSaved0(group, dataId);
	}

	private static final NacosReader connection(String serverAddr) throws NacosReadException {
		Properties prop = new Properties();
		prop.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
		return new NacosReader(prop);
	}

	private static final NacosReader connection(String serverAddr, String namespace) throws NacosReadException {
		Properties prop = new Properties();
		prop.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
		prop.put(PropertyKeyConst.NAMESPACE, namespace);
		return new NacosReader(prop);
	}

	private NacosReader(Properties prop) throws NacosReadException {
		try {
			log.info("Connection ConfigService -> serverAddr==[{}]", prop.get(PropertyKeyConst.SERVER_ADDR));
			log.info("Connection ConfigService -> namespace==[{}]", prop.get(PropertyKeyConst.NAMESPACE));
			this.service = ConfigFactory.createConfigService(prop);
		} catch (NacosException e) {
			log.error("createConfigService error -> {}", e.getMessage(), e);
			throw new NacosReadException("ConfigFactory.createConfigService call error", e);
		}
	}

	private String getSaved0(String group, String dataId) throws NacosReadException {
		try {
			log.info("Reading nacos ->  group==[{}], dataId==[{}]", group, dataId);
			String saved = service.getConfig(dataId, group, 10000);
			if (StringUtil.isNullOrEmpty(saved)) {
				log.info("Read nacos saved is null or empty");
				throw new NacosReadException("Read nacos saved is null or empty");
			}
			return saved;
		} catch (NacosException e) {
			log.error("Read nacos error -> {}", e.getMessage());
			throw new NacosReadException("ConfigService.getConfig call error", e);
		}
	}

	private Properties getProperties0(String group, String dataId) throws NacosReadException {
		String saved = getSaved0(group, dataId);
		try {
			return PropertiesUtil.toProperties(saved);
		} catch (IOException e) {
			log.error("PropertiesUtil.toProperties throw exception -> {}", e.getMessage());
			throw new NacosReadException(e);
		}
	}

}
