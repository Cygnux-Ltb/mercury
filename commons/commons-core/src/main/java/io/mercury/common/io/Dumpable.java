package io.mercury.common.io;

import java.io.Serializable;

public interface Dumpable<T> extends Serializable {

	T dump();

}
