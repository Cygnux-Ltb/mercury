package io.mercury.library.ignite.learning.javarun.database;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.IgniteConfiguration;

/**
 * @program : ignite_learning
 * @create : 2018-06-08 16:40
 **/
public class SQLTest {

    public static void main(String[] args) {

        IgniteConfiguration icfg = new IgniteConfiguration();

        icfg.setIgniteInstanceName("firstIgnite");

        icfg.setPeerClassLoadingEnabled(true);

        Ignite ignite = Ignition.start(icfg);

        IgniteCache<?, ?> cache = ignite.getOrCreateCache("Person");

        cache.query(new SqlFieldsQuery("CREATE TABLE Person " +
                "(id int primary key, firstName varchar, lastName varchar)").setSchema("PUBLIC"));

        cache.query(new SqlFieldsQuery("INSERT INTO Person(id, firstName, lastName)" +
                " VALUES(?, ?, ?)").setArgs(1, "John", "Smith"));

        cache.query(new SqlFieldsQuery("INSERT INTO Person(id, firstName, lastName)" +
                " VALUES(?, ?, ?)").setArgs(2, "Tailang", "Li"));

        cache.query(new SqlFieldsQuery("INSERT INTO Person(id, firstName, lastName)" +
                " VALUES(?, ?, ?)").setArgs(2, "Xuan", "Yang"));


//        cache.query(new SqlFieldsQuery("CREATE TABLE City " +
//                "(id int primary key, name varchar, region varchar)"));
//
//        cache.query(new SqlFieldsQuery("CREATE TABLE Organization " +
//                "(id int primary key, name varchar, cityName varchar)"));
//
//        SqlFieldsQuery qry = new SqlFieldsQuery("SELECT o.name from Organization o " +
//                "inner join \"Person\".Person p on o.id = p.orgId " +
//                "inner join City c on c.name = o.cityName " +
//                "where p.age > 25 and c.region <> 'Texas'");

//        qry.setSchema("PUBLIC");
//
//        cache.query(qry);
    }
}
