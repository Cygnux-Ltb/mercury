package io.mercury.common.collections;

import java.util.Collection;

public final class CollectionUtil {

	/**
	 * 
	 * @param <E>
	 * @param coll
	 * @return
	 */
	public static final <E> boolean isOnlyOneElement(Collection<E> coll) {
		if (coll != null && coll.size() == 1)
			return true;
		return false;
	}

	/**
	 * 
	 * @param <E>
	 * @param coll
	 * @return
	 */
	public static final <E> boolean notEmpty(Collection<E> coll) {
		if (coll != null && coll.size() > 1)
			return true;
		return false;
	}

}
