package io.mercury.persistence.chronicle.queue;

import java.util.function.Consumer;

import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import net.openhft.chronicle.queue.ExcerptTailer;

@NotThreadSafe
public final class ChronicleStringReader extends AbstractChronicleReader<String> {

	ChronicleStringReader(long allocationNo, String readerName, FileCycle fileCycle, ReaderParam readerParam, Logger logger,
			ExcerptTailer excerptTailer, Consumer<String> consumer) {
		super(allocationNo, readerName, fileCycle, readerParam, logger, excerptTailer, consumer);
	}

	@Override
	protected String next0() {
		return excerptTailer.readText();
	}

}
