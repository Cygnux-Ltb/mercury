package io.mercury.common.param;

import io.mercury.common.param.Params.ParamType;

/**
 * Inner Key type
 */
public interface JointKey {

	int key0();

	int key1();

	ParamType type();

}