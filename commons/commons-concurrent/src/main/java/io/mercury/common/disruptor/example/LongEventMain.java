package io.mercury.common.disruptor.example;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class LongEventMain {

	

	public static void main(String[] args) throws Exception {
		int bufferSize = 1024;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent.EventFactory, bufferSize,
				DaemonThreadFactory.INSTANCE);

		disruptor.handleEventsWith(LongEvent::handleEvent);
		disruptor.start();

		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		ByteBuffer bb = ByteBuffer.allocate(8);

		for (long l = 0; true; l++) {
			bb.putLong(0, l);
			// ringBuffer.publishEvent((event, sequence) -> event.set(bb.getLong(0)));
			ringBuffer.publishEvent((event, sequence, buffer) -> event.set(buffer.getLong(0)), bb);
			Thread.sleep(500);
		}
	}

}
