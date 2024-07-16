package io.mercury.persistence.arangodb;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.Protocol;
import com.arangodb.entity.BaseDocument;
import com.arangodb.model.DBCreateOptions;
import com.arangodb.velocypack.VPackSlice;
import io.mercury.common.log4j2.Log4j2Configurator;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import org.slf4j.Logger;

import static io.mercury.common.util.StringSupport.toStringShortPrefixStyle;

public class ArangodbDemo {

	static {
		Log4j2Configurator.useInfoLogLevel();
	}

	private static final Logger log = Log4j2LoggerFactory.getLogger(ArangodbDemo.class);

	public static void main(final String[] args) {

		final ArangoDB arangoDB = new ArangoDB.Builder()
				.host("127.0.0.1", 8529)
				.user("root").password("root")
				.protocol(Protocol.VST)
				.build();

		log.info("ArangoDB Info: Version -> {}, Engine -> {}",
				toStringShortPrefixStyle(arangoDB.getVersion()),
				toStringShortPrefixStyle(arangoDB.getEngine()));

		final DBCreateOptions options = new DBCreateOptions().name("test_db");
		final String collection = "userdata";

		try {
			ArangoDatabase database = arangoDB.db("test_db");
			if (!database.exists()) {
				arangoDB.createDatabase(options);
				log.info("Create Database: {}", options.getName());
			}
			arangoDB.getDatabases().forEach(dbName0 -> log.info("DB Name -> {}", dbName0));
			handleArangoDatabase(database, collection);
		} catch (ArangoDBException e) {
			log.error("Failed to create database: " + options.getName() + "; " + e.getMessage());
		} finally {
			arangoDB.shutdown();
		}
	}

	private static void handleArangoDatabase(ArangoDatabase database, String collection) {
		ArangoCollection collect = database.collection(collection);
		try {
			for (int i = 0; i < 40; i++) {
				String key = "user" + i;
				BaseDocument user = new BaseDocument(key);
				user.addAttribute("username", "name" + i);
				user.addAttribute("age", 30);
				if (!collect.documentExists(key)) {
					collect.insertDocument(user);
				}
			}
			log.info("collection count -> {}", collect.count().getCount());
			VPackSlice pack = collect.getDocument("user0", VPackSlice.class);
			log.info("Key: {}", pack.get("_key").getAsString());
			log.info("Attribute username: {}", pack.get("username").getAsString());
			log.info("Attribute age: {}", pack.get("age").getAsInt());
		} catch (ArangoDBException e) {
			log.error("Failed to create document -> {}", e.getMessage(), e);
		}
	}

}