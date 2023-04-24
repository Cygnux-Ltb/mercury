package io.mercury.library.ignite.learning.controller;

import jakarta.annotation.Resource;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @program : ignite_learning
 * @create : 2018-06-06 06:55
 **/
@RestController
public class TestIgnite {

    // 通过依赖注入的方式获取到Ignite对象
    @Resource
    private Ignite ignite;

    @GetMapping(value = "/")
    public String testInject() {
        IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCacheName");

        for (int i = 0; i < 10; i++) {
            cache.put(i, Integer.toString(i));
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("Got [key=" + i + ", val=" + cache.get(i) + ']');
        }
        return "";
    }

    public void setIgnite(Ignite ignite) {
        this.ignite = ignite;
    }
}
