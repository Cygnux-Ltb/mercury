package io.mercury.persistence.chronicle.queue;

import java.util.function.Consumer;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import net.openhft.chronicle.queue.ExcerptTailer;

@Immutable
@NotThreadSafe
public final class ChronicleStringReader extends AbstractChronicleReader<String> {

	ChronicleStringReader(long allocateSeq, String readerName, FileCycle fileCycle, ReaderParam param,
			Logger logger, ExcerptTailer excerptTailer, Consumer<String> consumer) {
		super(allocateSeq, readerName, fileCycle, param, logger, excerptTailer, consumer);
	}

	@Override
	protected String next0() {
		return excerptTailer.readText();
	}

}
