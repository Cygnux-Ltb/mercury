package io.mercury.transport.http.couch;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;

import io.mercury.common.util.PropertiesUtil;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.sys.SysProperties;
import io.mercury.serialization.json.JsonParser;
import io.mercury.transport.http.SyncHttp;

public final class CouchConnector {

	private static final Logger log = Log4j2LoggerFactory.getLogger(CouchConnector.class);

	private String couchdbUrl;

	public static final CouchConnector Singleton = new CouchConnector();

	private CouchConnector() {
		try {
			File file = new File(SysProperties.USER_HOME + "/config/couchdb.properties");
			if (file.exists()) {
				Properties prop = PropertiesUtil.load(file);
				this.couchdbUrl = prop.getProperty("couchdb_url");
			} else
				this.couchdbUrl = "http://127.0.0.1:5984";
		} catch (IOException e) {
			log.error("", e);
		}
	}

	/**
	 * 发送Get请求获取数据
	 * 
	 * @param resultType
	 * @param uri
	 * @param params
	 * @return
	 */
	public String getCouchBeanValue(String database, CouchDocument document) {
		CouchBean couchBean = JsonParser.toObject(sendGetRequest(database, document.documentId()), CouchBean.class);
		return couchBean.getValue();
	}

	/**
	 * 
	 * @param database
	 * @param documentId
	 * @return
	 */
	private String sendGetRequest(String database, String documentId) {
		log.info("sendGetRequest() -> database==[{}], documentId==[{}]", database, documentId);
		try {
			return SyncHttp.sentGet(couchdbUrl + "/" + database + "/" + documentId);
		} catch (Exception e) {
			log.error("sendGetRequest() -> database==[{}], documentId==[{}]", database, documentId, e);
			return "";
		}
	}

}
