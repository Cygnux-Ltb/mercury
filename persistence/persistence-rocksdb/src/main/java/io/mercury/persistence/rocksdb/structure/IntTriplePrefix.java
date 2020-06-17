package io.mercury.persistence.rocksdb.structure;

import java.nio.ByteBuffer;

public class IntTriplePrefix {

	public static final int PrefixLength = Integer.BYTES * 3;

	private final int first;
	private final int second;
	private final int third;

	private final ByteBuffer byteBuffer;

	public IntTriplePrefix(int first, int second, int third) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.byteBuffer = ByteBuffer.allocate(PrefixLength).putInt(first).putInt(second).putInt(third);
	}

	public IntTriplePrefix(int first, int second, int third, int extLength) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.byteBuffer = ByteBuffer.allocate(PrefixLength + extLength).putInt(first).putInt(second).putInt(third);
	}

	public int first() {
		return first;
	}

	public int second() {
		return second;
	}

	public int third() {
		return third;
	}

	public ByteBuffer byteBuffer() {
		return byteBuffer;
	}

	public byte[] bytes() {
		return byteBuffer.array();
	}

}
