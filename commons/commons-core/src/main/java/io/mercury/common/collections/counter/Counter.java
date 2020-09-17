package io.mercury.common.collections.counter;

/**
 * 累加计数器接口
 * 
 * @author yellow013
 *
 * @param <T>
 */
public interface Counter<T extends Counter<T>> {

	/**
	 * 设置一个值, tag应使用唯一值, 重复的tag将覆盖已有tag
	 * 
	 * @param tag
	 * @param delta
	 * @return
	 */
	T add(long tag, long delta);

	/**
	 * 
	 * @param tag
	 * @param delta
	 * @return
	 */
	default T subtract(long tag, long delta) {
		return add(tag, -delta);
	}

	/**
	 * 移除一个tag和tag指向的值
	 * 
	 * @param tag
	 * @return
	 */
	T remove(long tag);

	/**
	 * 在历史delta上添加新的delta
	 * 
	 * @param tag
	 * @param delta
	 * @return
	 */
	T historyDeltaAdd(long tag, long delta);

	/**
	 * 
	 * @param tag
	 * @param delta
	 * @return
	 */
	default T historyDeltaSubtract(long tag, long delta) {
		return historyDeltaAdd(tag, -delta);
	}

	/**
	 * 
	 * @param tag
	 * @return
	 */
	default T increment(long tag) {
		return add(tag, 1);
	}

	/**
	 * 
	 * @param tag
	 * @return
	 */
	default T decrement(long tag) {
		return subtract(tag, 1);
	}

	/**
	 * 
	 * @return
	 */
	long getValue();

}
