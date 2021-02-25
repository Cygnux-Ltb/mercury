package io.mercury.persistence.chronicle.queue.multitype;

import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.codec.Envelope;
import io.mercury.persistence.chronicle.queue.multitype.ChronicleMultitypeJsonQueue.JsonPacket;
import io.mercury.serialization.json.JsonWrapper;
import net.openhft.chronicle.queue.ExcerptAppender;

@Immutable
@NotThreadSafe
public final class ChronicleMultitypeJsonAppender<E extends Envelope>
		extends AbstractChronicleMultitypeAppender<E, String> {

	ChronicleMultitypeJsonAppender(long allocateSeq, String appenderName, Logger logger,
			ExcerptAppender excerptAppender, Supplier<String> supplier) {
		super(allocateSeq, appenderName, logger, excerptAppender, supplier);
	}

	// 內建JsonPacket对象
	private JsonPacket packet = new JsonPacket();

	@Override
	protected void append0(E envelope, String t) {
		excerptAppender.writeText(
				// JsonPacket序列化为JSON
				JsonWrapper.toJson(
						// 设置信封
						packet.setEnvelope(envelope.getCode())
								// 设置内容
								.setContent(t)));
	}

}
