package io.mercury.common.sequence;

public interface Serial<T extends Serial<T>> extends Comparable<T> {

	long getSerialId();

	@Override
	default int compareTo(T o) {
		return getSerialId() < o.getSerialId() ? -1 : getSerialId() > o.getSerialId() ? 1 : 0;
	}

}
