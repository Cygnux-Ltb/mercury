package io.mercury.common.concurrent.disruptor.dynamic.core;

/**
 * @author : Rookiex
 * @version :
 * @date : Created in 2019/11/8 10:31
 * @describe :
 */
public class HandlerEvent {

    private int id;
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

}
