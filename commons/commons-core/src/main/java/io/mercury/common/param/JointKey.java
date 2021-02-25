package io.mercury.common.param;

import io.mercury.common.param.Params.ValueType;

/**
 * Inner Key type
 */
public interface JointKey {

	int key0();

	int key1();

	ValueType getValuetype();

}