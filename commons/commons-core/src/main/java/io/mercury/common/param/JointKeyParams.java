package io.mercury.common.param.map;

import static io.mercury.common.datetime.DateTimeUtil.date;
import static io.mercury.common.datetime.DateTimeUtil.datetimeOfSecond;
import static io.mercury.common.datetime.DateTimeUtil.timeOfSecond;
import static io.mercury.common.datetime.DateTimeUtil.toLocalDate;
import static io.mercury.common.datetime.DateTimeUtil.toLocalDateTime;
import static io.mercury.common.datetime.DateTimeUtil.toLocalTime;
import static io.mercury.common.param.JointKeySupporter.mergeJointKey;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

import io.mercury.common.param.JointKey;

public class JointKeyParamMap<K extends JointKey> {

	private final MutableLongObjectMap<String> param = new LongObjectHashMap<>();

	public JointKeyParamMap<K> put(K key, boolean b) {
		param.put(mergeJointKey(key.key0(), key.key1()), Boolean.toString(b));
		return this;
	}

	public boolean getBoolean(K key) {
		return parseBoolean(param.get(mergeJointKey(key.key0(), key.key1())));
	}

	public JointKeyParamMap<K> put(K key, int i) {
		param.put(mergeJointKey(key.key0(), key.key1()), Integer.toString(i));
		return this;
	}

	public int getInt(K key) {
		return parseInt(param.get(mergeJointKey(key.key0(), key.key1())));
	}

	public JointKeyParamMap<K> put(K key, long l) {
		param.put(mergeJointKey(key.key0(), key.key1()), Long.toString(l));
		return this;
	}

	public long getLong(K key) {
		return parseLong(param.get(mergeJointKey(key.key0(), key.key1())));
	}

	public JointKeyParamMap<K> put(K key, double d) {
		param.put(mergeJointKey(key.key0(), key.key1()), Double.toString(d));
		return this;
	}

	public double getDouble(K key) {
		return parseDouble(param.get(mergeJointKey(key.key0(), key.key1())));
	}

	public JointKeyParamMap<K> put(K key, String str) {
		param.put(mergeJointKey(key.key0(), key.key1()), str);
		return this;
	}

	public String getString(K key) {
		return param.get(mergeJointKey(key.key0(), key.key1()));
	}

	public JointKeyParamMap<K> put(K key, LocalDate date) {
		put(key, date(date));
		return this;
	}

	public LocalDate getLocalDate(K key) {
		return toLocalDate(parseInt(param.get(mergeJointKey(key.key0(), key.key1()))));
	}

	public JointKeyParamMap<K> put(K key, LocalTime time) {
		put(key, timeOfSecond(time));
		return this;
	}

	public LocalTime getLocalTime(K key) {
		return toLocalTime(parseInt(param.get(mergeJointKey(key.key0(), key.key1()))));
	}

	public JointKeyParamMap<K> put(K key, LocalDateTime datetime) {
		put(key, datetimeOfSecond(datetime));
		return this;
	}

	public LocalDateTime getLocalDateTime(K key) {
		return toLocalDateTime(parseInt(param.get(mergeJointKey(key.key0(), key.key1()))));
	}

}
