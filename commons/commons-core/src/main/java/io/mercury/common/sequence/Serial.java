package io.mercury.common.sequence;

@FunctionalInterface
public interface Serial<S extends Serial<S>> extends Comparable<S> {

	long getSerialId();

	@Override
	default int compareTo(S o) {
		return Long.compare(getSerialId(), o.getSerialId());
	}

}
