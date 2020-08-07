package io.mercury.common.serialization;

import java.io.Serializable;

public interface Dumpable<T> extends Serializable {

	T dump();

}
