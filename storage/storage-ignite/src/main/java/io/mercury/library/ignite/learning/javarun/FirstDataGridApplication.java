package io.mercury.library.ignite.learning.javarun;

import io.mercury.library.ignite.learning.model.Address;
import io.mercury.library.ignite.learning.model.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

/**
 * @program : ignite_learning
 * @create : 2018-06-06 20:41
 * <p>
 * 使用配置启动Ignite节点，并且向Ignite节点中创建myCache这个Cache，并向其中放入十个Person对象
 * 用于测试并启动一个特定的Ignite节点，并尝试ignite简单的的put以及get操作，以及将对象直接作为值放入Cache的操作
 * <p>
 * 在IgniteCacheAccess.java中在对这个放入的对象进行访问
 **/
public class FirstDataGridApplication {
    public static void main(String[] args) {

        // 设置一个Ignite配置，将Ignite的节点名称设置
        IgniteConfiguration igniteCfg = new IgniteConfiguration();

        igniteCfg.setIgniteInstanceName("firstIgnite");

        igniteCfg.setPeerClassLoadingEnabled(true);

        // 启动Ignite节点
        Ignite ignite = Ignition.start(igniteCfg);

        // 新生成一个Cache的配置，用于Ignite节点新建一个Cache
        CacheConfiguration<String, Person> cfg = new CacheConfiguration<>();

        // 设置Cache的名称为myCache
        cfg.setName("myCache");

        //cfg.setAtomicityMode(TRANSACTIONAL);

        // 以之前配好的Cache配置启动Ignite节点
        IgniteCache<String, Person> cache = ignite.getOrCreateCache(cfg);

        // 创建三个City对象用于测试
//        City wuhan = new City(1, "Wuhan");
//        City beijing = new City(2, "Beijing");
//        City shanghai = new City(3, "Shanghai");

        // 创建一个Address对象用于测试
        Address address = new Address("Test_Address");

        // 创建两个Person对象用于测试
        Person alice = new Person(1, "Alice", 1, address);
        Person bob = new Person(2, "Bob", 2, address);

        cache.put(alice.getName(), alice);
        cache.put(bob.getName(), bob);
    }
}
