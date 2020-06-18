package io.mercury.common.sequence;

public interface Serial extends Comparable<Serial> {

	long serialId();

	@Override
	default int compareTo(Serial o) {
		return serialId() < o.serialId() ? -1 : serialId() > o.serialId() ? 1 : 0;
	}

}
