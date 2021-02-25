package io.mercury.persistence.chronicle.queue.multitype;

import java.util.function.Consumer;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.persistence.chronicle.queue.FileCycle;
import io.mercury.persistence.chronicle.queue.multitype.ChronicleMultitypeStringQueue.JsonPacket;
import io.mercury.serialization.json.JsonParseException;
import io.mercury.serialization.json.JsonParser;
import net.openhft.chronicle.queue.ExcerptTailer;

@Immutable
@NotThreadSafe
public final class ChronicleMultitypeStringReader extends AbstractChronicleMultitypeReader<JsonPacket> {

	ChronicleMultitypeStringReader(long allocateSeq, String readerName, FileCycle fileCycle, ReaderParam param,
			Logger logger, ExcerptTailer excerptTailer, Consumer<JsonPacket> consumer) {
		super(allocateSeq, readerName, fileCycle, param, logger, excerptTailer, consumer);
	}

	@Override
	protected JsonPacket next0() {
		String next = excerptTailer.readText();
		if (next == null)
			return null;
		try {
			return JsonParser.toObject(next, JsonPacket.class);
		} catch (JsonParseException e) {
			logger.info("Parse error from JSON -> {}", next);
			return null;
		}
	}

}
