package io.mercury.persistence.chronicle.queue.multitype;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;

import io.mercury.common.codec.Envelope;
import io.mercury.common.sequence.EpochSeqAllocator;
import io.mercury.persistence.chronicle.queue.multitype.AbstractChronicleMultitypeReader.ReaderParam;
import io.mercury.persistence.chronicle.queue.multitype.ChronicleMultitypeStringQueue.JsonPacket;

@Immutable
public class ChronicleMultitypeStringQueue<E extends Envelope> extends
		AbstractChronicleMultitypeQueue<E, String, JsonPacket, ChronicleMultitypeStringAppender<E>, ChronicleMultitypeStringReader> {

	private ChronicleMultitypeStringQueue(Builder<E> builder) {
		super(builder);
	}

	public static <E extends Envelope> Builder<E> newBuilder(Class<E> envelopeClass) {
		return new Builder<>();
	}

	@Override
	protected ChronicleMultitypeStringReader createReader(String readerName, ReaderParam readerParam, Logger logger,
			Consumer<JsonPacket> consumer) throws IllegalStateException {
		return new ChronicleMultitypeStringReader(EpochSeqAllocator.allocate(), readerName, fileCycle(), readerParam,
				logger, internalQueue.createTailer(), consumer);
	}

	@Override
	protected ChronicleMultitypeStringAppender<E> acquireAppender(String appenderName, Logger logger,
			Supplier<String> supplier) throws IllegalStateException {
		return new ChronicleMultitypeStringAppender<>(EpochSeqAllocator.allocate(), appenderName, logger,
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

		public ChronicleMultitypeStringQueue<E> build() {
			return new ChronicleMultitypeStringQueue<>(this);
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

	}

}
