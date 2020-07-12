package io.mercury.common.io;

import java.io.Serializable;

public interface Dumper<DT extends Serializable> extends Serializable {

	DT dump();

}
