package io.mercury.common.lang;

public abstract class NonInstantiable {

    protected NonInstantiable() {
        throw new IllegalStateException(
                "class[" + this.getClass().getName() + "] is can't Instantiable!");
    }

}
