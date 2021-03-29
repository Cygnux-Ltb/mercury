package io.mercury.common.collections;

import java.util.Map;

public final class MapUtil {

	private MapUtil() {
	}

	/**
	 * 
	 * @param map0
	 * @param map1
	 * @return
	 */
	public static final boolean isEquals(Map<?, ?> map0, Map<?, ?> map1) {
		if (map0 == null && map1 == null)
			return true;
		else if ((map0 != null && map1 == null) || (map1 != null && map0 == null))
			return false;
		else
			return map0.equals(map1);
	}

}
