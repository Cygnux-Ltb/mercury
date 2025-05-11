package io.mercury.storage.couchdb;

import io.mercury.common.http.JreHttpClient;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.util.PropertiesUtil;
import io.mercury.serialization.json.JsonReader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

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
     * @param database String
     * @param document CouchDocument
     * @return String
     */
    public String getCouchBeanValue(String database, CouchDocument document) {
        CouchBean couchBean = JsonReader.toObject(sendGetRequest(database, document.documentId()), CouchBean.class);
        return couchBean.getValue();
    }

    /**
     * @param database   String
     * @param documentId String
     * @return String
     */
    private String sendGetRequest(String database, String documentId) {
        log.info("sendGetRequest() -> database==[{}], documentId==[{}]", database, documentId);
        try {
            return JreHttpClient.doGet(couchdbUrl + "/" + database + "/" + documentId).body();
        } catch (Exception e) {
            log.error("sendGetRequest() -> database==[{}], documentId==[{}]", database, documentId, e);
            return "";
        }
    }

}
