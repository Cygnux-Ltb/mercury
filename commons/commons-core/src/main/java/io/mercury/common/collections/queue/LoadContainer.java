package io.mercury.common.collections.queue;

/**
 * @param <E>
 * @author yellow013
 */
public class LoadContainer<E> {

    private int type;

    private E e;

    /**
     * @param e E
     */
    public void loading(E e) {
        this.type = 0;
        this.e = e;
    }

    /**
     * @param type int
     * @param e    E
     */
    public void loading(int type, E e) {
        this.type = type;
        this.e = e;
    }

    public int type() {
        return type;
    }

    public E unloading() {
        return e;
    }

}