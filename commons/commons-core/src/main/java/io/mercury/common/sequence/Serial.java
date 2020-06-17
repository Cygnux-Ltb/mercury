package io.mercury.common.sequence;

public interface Serial<T extends Serial<T>> extends Comparable<T> {

	long serialId();

	@Override
	default int compareTo(T o) {
		return serialId() < o.serialId() ? -1 : serialId() > o.serialId() ? 1 : 0;
	}

}
