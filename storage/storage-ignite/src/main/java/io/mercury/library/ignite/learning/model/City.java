package io.mercury.library.ignite.learning.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * @program: ignite_learning
 * @create: 2018-06-05 11:14
 * <p>
 * 这个类用于表示城市，包含两个字段，用于ignite测试
 * @param: id   城市id
 * @param: name 城市名称
 **/
public class City {

    @QuerySqlField
    private int id;

    @QuerySqlField
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City() {
    }

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

}