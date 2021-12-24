package io.mercury.common.param;

import io.mercury.common.param.Params.ValueType;

/**
 * 
 * @author yellow013
 */
public interface ParamKey {

	int getParamId();

	String getParamName();

	ValueType getValueType();

}
