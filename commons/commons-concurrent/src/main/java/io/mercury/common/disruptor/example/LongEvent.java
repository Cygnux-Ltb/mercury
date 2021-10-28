package io.mercury.common.disruptor.example;

import java.nio.ByteBuffer;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;

public class LongEvent {

	private long value;

	public LongEvent set(long value) {
		this.value = value;
		return this;
	}

	public long getValue() {
		return value;
	}


	public static final EventHandler<LongEvent> EventHandler = (event, sequence, endOfBatch) -> {
		System.out.println("event value -> " + event.getValue());
	};
	
	public static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
		System.out.println(event);
	}

	public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
		event.set(buffer.getLong(0));
	}

}