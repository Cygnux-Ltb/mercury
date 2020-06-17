package io.mercury.common.concurrent.disruptor;

public enum BufferSize {

	POW2_6(64),

	POW2_7(128),

	POW2_8(256),

	POW2_9(512),

	POW2_10(1024),

	POW2_11(2048),

	POW2_12(4096),

	POW2_13(8192),

	POW2_14(16384),

	POW2_15(32768),

	POW2_16(65536),

	;

	int size;

	private BufferSize(int size) {
		this.size = size;
	}

	int value() {
		return size;
	}

}
