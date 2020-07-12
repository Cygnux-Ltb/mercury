package io.mercury.common.param.map;

import static io.mercury.common.param.JointKeySupporter.mergeJointKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

import io.mercury.common.datetime.DateTimeUtil;
import io.mercury.common.param.JointKey;

public class JointKeyParamMap<K extends JointKey> {

	private MutableLongObjectMap<String> paramMap = new LongObjectHashMap<>();

	public JointKeyParamMap() {

	}

	public void put(K key, boolean b) {
		paramMap.put(mergeJointKey(key.key0(), key.key1()), Boolean.toString(b));
	}

	public void put(K key, int i) {
		paramMap.put(mergeJointKey(key.key0(), key.key1()), Integer.toString(i));
	}

	public void put(K key, long l) {
		paramMap.put(mergeJointKey(key.key0(), key.key1()), Long.toString(l));
	}

	public void put(K key, double d) {
		paramMap.put(mergeJointKey(key.key0(), key.key1()), Double.toString(d));
	}

	public void put(K key, String s) {
		paramMap.put(mergeJointKey(key.key0(), key.key1()), s);
	}

	public void put(K key, LocalDate date) {
		put(key, DateTimeUtil.date(date));
	}

	public void put(K key, LocalTime time) {
		put(key, DateTimeUtil.timeOfSecond(time));
	}

	public void put(K key, LocalDateTime datetime) {
		put(key, DateTimeUtil.datetimeOfSecond(datetime));
	}

	public boolean getBoolean(K key) {
		return Boolean.parseBoolean(paramMap.get(mergeJointKey(key.key0(), key.key1())));
	}

}
