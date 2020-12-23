package io.mercury.persistence.arangodb;

import static io.mercury.common.util.StringUtil.toStringWithReflection;

import org.slf4j.Logger;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.Protocol;
import com.arangodb.entity.BaseDocument;
import com.arangodb.model.DBCreateOptions;
import com.arangodb.velocypack.VPackSlice;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.log.LogConfigurator;
import io.mercury.common.log.LogConfigurator.LogLevel;

public class ArangoDemo {

	private static final Logger log = CommonLoggerFactory.getLogger(ArangoDemo.class);

	public static void main(final String[] args) {

		LogConfigurator.logLevel(LogLevel.INFO);

		final ArangoDB arangoDB = new ArangoDB.Builder().host("127.0.0.1", 8529).user("root").password("root")
				.useProtocol(Protocol.VST).build();

		log.info("ArangoDB Info: Version -> {}, Engine -> {}", toStringWithReflection(arangoDB.getVersion()),
				toStringWithReflection(arangoDB.getEngine()));

		final DBCreateOptions options = new DBCreateOptions().name("test_db");
		final String collection = "userdata";

		try {

			ArangoDatabase database = arangoDB.db(options.getName());

			if (!database.exists()) {
				arangoDB.createDatabase(options);
				log.info("Create Database: {}", options.getName());
			}

			arangoDB.getDatabases().forEach(dbName -> log.info("DB Name -> {}", dbName));

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