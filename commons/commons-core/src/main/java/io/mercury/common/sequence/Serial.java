package io.mercury.common.sequence;

public interface Serial extends Comparable<Serial> {

	long getSerialId();

	@Override
	default int compareTo(Serial o) {
		return getSerialId() < o.getSerialId() ? -1 : getSerialId() > o.getSerialId() ? 1 : 0;
	}

}
