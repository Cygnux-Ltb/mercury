package io.mercury.common.sequence;

public interface Serial<S extends Serial<S>> extends Comparable<S> {

	long getSerialId();

	@Override
	default int compareTo(S o) {
		return getSerialId() < o.getSerialId() ? -1 : getSerialId() > o.getSerialId() ? 1 : 0;
	}

}
