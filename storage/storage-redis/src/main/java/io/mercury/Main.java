package io.mercury;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

public class Main {
	
    public static void main(String[] args) {
        try (JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
             Jedis jedis = pool.getResource()) {
            jedis.set("foo", "bar");
            String foobar = jedis.get("foo");
            jedis.zadd("lists", 0, "car");
            jedis.zadd("lists", 0, "bike");
            List<String> lists = jedis.zrange("lists", 0, -1);
        }
    }

}