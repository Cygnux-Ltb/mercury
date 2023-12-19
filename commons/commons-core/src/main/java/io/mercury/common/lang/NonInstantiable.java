package io.mercury.common.lang;

public abstract class NonInstantiable {

    protected NonInstantiable() {
        throw new IllegalStateException(
                STR."class[\{this.getClass().getName()}] is can't Instantiable!");
    }

}
