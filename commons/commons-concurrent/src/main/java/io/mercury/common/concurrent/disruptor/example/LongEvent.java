package io.mercury.common.concurrent.disruptor.example;

import java.nio.ByteBuffer;

import com.lmax.disruptor.EventHandler;

import io.mercury.common.concurrent.disruptor.RingEvent;

public class LongEvent implements RingEvent {

	private long value;

	public LongEvent set(long value) {
		this.value = value;
		return this;
	}

	public long get() {
		return value;
	}

	public static final EventHandler<LongEvent> EventHandler = (event, sequence, endOfBatch) -> {
		System.out.println("event value -> " + event.get());
	};

	public static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
		System.out.println(event);
	}

	public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
		event.set(buffer.getLong(0));
	}

}