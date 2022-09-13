package io.mercury.common.concurrent.disruptor.example;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class LongEventMain {

	public static void main(String[] args) throws Exception {
		int bufferSize = 1024;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

		disruptor.handleEventsWith(LongEvent::handleEvent);
		disruptor.start();

		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		ByteBuffer bb = ByteBuffer.allocate(8);

		for (long l = 0; true; l++) {
			bb.putLong(0, l);
			// ringBuffer.publishEvent((event, sequence) -> event.set(bb.getLong(0)));
			ringBuffer.publishEvent(LongEvent::translate, bb);
			Thread.sleep(500);
		}
	}

}
