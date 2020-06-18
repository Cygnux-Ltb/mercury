package io.mercury.common.collections;

import java.util.Map;
import java.util.Set;

public final class MapUtil {

	public static boolean isEquals(Map<?, ?> mapA, Map<?, ?> mapB) {
		if (mapA == null && mapB == null)
			return true;
		else if ((mapA != null && mapB == null) || (mapB != null && mapA == null))
			return false;
		else {
			Set<?> keySetA = mapA.keySet();
			Set<?> keySetB = mapB.keySet();
			if (keySetA.size() != keySetB.size())
				return false;
			
			
			return false;
		}
	}

}
