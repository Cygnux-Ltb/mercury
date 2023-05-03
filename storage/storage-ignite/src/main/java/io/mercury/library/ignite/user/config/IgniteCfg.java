package io.mercury.library.ignite.user.config;

import io.mercury.library.ignite.user.model.Person;
import io.mercury.library.ignite.user.model.Role;
import io.mercury.library.ignite.user.service.PersonService;
import jakarta.annotation.Resource;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 **/
@Configuration
public class IgniteCfg {

    /**
     * 初始化ignite节点信息
     *
     * @return Ignite
     */
    @Bean
    public Ignite igniteInstance() {
        // 配置一个节点的Configuration
        IgniteConfiguration cfg = new IgniteConfiguration();

        // 设置该节点名称
        cfg.setIgniteInstanceName("springDataNode");

        // 启用Peer类加载器
        cfg.setPeerClassLoadingEnabled(true);

        // 创建一个Cache的配置，名称为PersonCache
        CacheConfiguration<?, ?> cacheCfg = new CacheConfiguration<>("PersonCache");

        // 设置这个Cache的键值对模型
        cacheCfg.setIndexedTypes(Long.class, Person.class);

        // 把这个Cache放入springDataNode这个Node中
        cfg.setCacheConfiguration(cacheCfg);

        // 启动这个节点
        return Ignition.start(cfg);
    }


    @Resource
    PersonService personService;

    /**
     * Add few people in ignite for testing easily
     */
    @Bean
    public int addPerson() {
        // Give a default role : MEMBER
        List<Role> roles = new ArrayList<>();
        roles.add(Role.MEMBER);

        // add data
        personService.save(new Person("test1", "test1", roles));
        personService.save(new Person("test2", "test2", roles));
        personService.save(new Person("test3", "test3", roles));

        return 0;
    }
}
