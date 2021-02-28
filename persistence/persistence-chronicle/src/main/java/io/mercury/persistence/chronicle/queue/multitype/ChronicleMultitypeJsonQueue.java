package io.mercury.persistence.chronicle.queue.multitype;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;

import io.mercury.common.codec.Envelope;
import io.mercury.common.sequence.EpochSequence;
import io.mercury.persistence.chronicle.queue.multitype.AbstractChronicleMultitypeReader.ReaderParam;
import io.mercury.persistence.chronicle.queue.multitype.ChronicleMultitypeJsonQueue.JsonPacket;
import io.mercury.serialization.json.JsonWrapper;

@Immutable
public class ChronicleMultitypeJsonQueue<E extends Envelope> extends
		AbstractChronicleMultitypeQueue<E, String, JsonPacket, ChronicleMultitypeJsonAppender<E>, ChronicleMultitypeJsonReader> {

	private ChronicleMultitypeJsonQueue(Builder<E> builder) {
		super(builder);
	}

	public static <E extends Envelope> Builder<E> newBuilder(Class<E> envelopeClass) {
		return new Builder<>();
	}

	@Override
	protected ChronicleMultitypeJsonReader createReader(String readerName, ReaderParam readerParam, Logger logger,
			Consumer<JsonPacket> consumer) throws IllegalStateException {
		return new ChronicleMultitypeJsonReader(EpochSequence.allocate(), readerName, fileCycle(), readerParam,
				logger, internalQueue.createTailer(), consumer);
	}

	@Override
	protected ChronicleMultitypeJsonAppender<E> acquireAppender(String appenderName, Logger logger,
			Supplier<String> supplier) throws IllegalStateException {
		return new ChronicleMultitypeJsonAppender<>(EpochSequence.allocate(), appenderName, logger,
				internalQueue.acquireAppender(), supplier);
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

	/**
	 * JSON包装对象
	 * 
	 * @author yellow013
	 */
	public static final class JsonPacket {

		private int envelope;
		private String content;

		public int getEnvelope() {
			return envelope;
		}

		public String getContent() {
			return content;
		}

		public JsonPacket setEnvelope(int envelope) {
			this.envelope = envelope;
			return this;
		}

		public JsonPacket setContent(String content) {
			this.content = content;
			return this;
		}

		@Override
		public String toString() {
			return JsonWrapper.toJson(this);
		}

	}

}
