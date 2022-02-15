package io.mercury.persistence.chronicle.queue.multitype;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.codec.Envelope;
import io.mercury.common.sequence.EpochSequence;
import io.mercury.persistence.chronicle.queue.AbstractChronicleReader;
import io.mercury.persistence.chronicle.queue.FileCycle;
import io.mercury.persistence.chronicle.queue.multitype.ChronicleMultitypeJsonQueue.ChronicleMultitypeJsonAppender;
import io.mercury.persistence.chronicle.queue.multitype.ChronicleMultitypeJsonQueue.ChronicleMultitypeJsonReader;
import io.mercury.persistence.chronicle.queue.params.ReaderParams;
import io.mercury.serialization.json.JsonMsg;
import io.mercury.serialization.json.JsonParser.JsonParseException;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;

@Immutable
public class ChronicleMultitypeJsonQueue<E extends Envelope> extends
		AbstractChronicleMultitypeQueue<E, String, JsonMsg, ChronicleMultitypeJsonAppender<E>, ChronicleMultitypeJsonReader> {

	private ChronicleMultitypeJsonQueue(Builder<E> builder) {
		super(builder);
	}

	public static <E extends Envelope> Builder<E> newBuilder(Class<E> envelopeType) {
		return new Builder<>();
	}

	@Override
	protected ChronicleMultitypeJsonReader createReader(String readerName, ReaderParams params, Logger logger,
			Consumer<JsonMsg> dataConsumer) throws IllegalStateException {
		return new ChronicleMultitypeJsonReader(EpochSequence.allocate(), readerName, fileCycle(), params, logger,
				internalQueue.createTailer(), dataConsumer);
	}

	@Override
	protected ChronicleMultitypeJsonAppender<E> acquireAppender(String appenderName, Logger logger,
			Supplier<String> dataProducer) throws IllegalStateException {
		return new ChronicleMultitypeJsonAppender<>(EpochSequence.allocate(), appenderName, logger,
				internalQueue.acquireAppender(), dataProducer);
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class Builder<E extends Envelope> extends QueueBuilder<Builder<E>> {

		private Builder() {
		}

		public ChronicleMultitypeJsonQueue<E> build() {
			return new ChronicleMultitypeJsonQueue<>(this);
		}

		@Override
		protected Builder<E> self() {
			return this;
		}

	}

	@Immutable
	@NotThreadSafe
	public static final class ChronicleMultitypeJsonAppender<E extends Envelope>
			extends AbstractChronicleMultitypeAppender<E, String> {

		ChronicleMultitypeJsonAppender(long allocateSeq, String appenderName, Logger logger, ExcerptAppender appender,
				Supplier<String> dataProducer) {
			super(allocateSeq, appenderName, logger, appender, dataProducer);
		}

		// 內建JsonMsg对象
		private final JsonMsg jsonMsg = new JsonMsg();

		@Override
		protected void append0(E envelope, String t) {
			appender.writeText(
					// 设置信封
					jsonMsg.setEnvelope(envelope.getCode())
							// 设置内容
							.setContent(t)
							// JsonMsg序列化为JSON
							.toJson());
		}

	}

	@Immutable
	@NotThreadSafe
	public static final class ChronicleMultitypeJsonReader extends AbstractChronicleReader<JsonMsg> {

		ChronicleMultitypeJsonReader(long allocateSeq, String readerName, FileCycle fileCycle, ReaderParams params,
				Logger logger, ExcerptTailer tailer, Consumer<JsonMsg> dataConsumer) {
			super(allocateSeq, readerName, fileCycle, params, logger, tailer, dataConsumer);
		}

		@Override
		protected JsonMsg next0() {
			String next = tailer.readText();
			if (next == null)
				return null;
			try {
				return JsonMsg.fromJson(next);
			} catch (JsonParseException e) {
				logger.info("Parse error from JSON -> {}", next);
				return null;
			}
		}

	}

}
