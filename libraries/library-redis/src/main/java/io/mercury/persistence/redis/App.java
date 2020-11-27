package io.mercury.persistence.redis;

import redis.clients.jedis.Jedis;

public class App {

	public static void main(String[] args) {

		Jedis jedis = new Jedis("localhost", 6379);
		jedis.auth("xxxx");
		String value = jedis.get("key");
		System.out.println(value);
		jedis.close();

	}

}
