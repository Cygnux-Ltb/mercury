package io.mercury.persistence.mapdb;

import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

public class MapDB {

	public static void main(String[] args) {

		DB db = DBMaker.fileDB("file.db").fileMmapEnable().make();

		ConcurrentMap<String, Long> map = db.hashMap("map", Serializer.STRING, Serializer.LONG).createOrOpen();

		map.put("something", 111L);

		db.close();
	}

}
