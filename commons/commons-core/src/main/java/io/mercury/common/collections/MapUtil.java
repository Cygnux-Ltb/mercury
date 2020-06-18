package io.mercury.common.collections;

import java.util.Map;

public final class MapUtil {

	public static boolean isEquals(Map<?, ?> mapA, Map<?, ?> mapB) {
		if (mapA == null && mapB == null)
			return true;
		else if ((mapA != null && mapB == null) || (mapB != null && mapA == null))
			return false;
		else
			return mapA.equals(mapB);
	}

}
