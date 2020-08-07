package io.mercury.common.file;

import java.io.Serializable;

public interface Dumpable<T> extends Serializable {

	T dump();

}
