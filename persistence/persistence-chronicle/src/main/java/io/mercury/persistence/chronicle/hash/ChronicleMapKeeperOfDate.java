package io.mercury.persistence.chronicle.hash;

import java.time.LocalDate;

import io.mercury.common.annotation.lang.ThrowsRuntimeException;
import io.mercury.common.datetime.DateTimeUtil;
import io.mercury.common.util.Assertor;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.map.ChronicleMap;

public final class ChronicleMapKeeperOfDate<K, V> extends ChronicleMapKeeper<K, V> {

	public ChronicleMapKeeperOfDate(ChronicleMapConfigurator<K, V> configurator) {
		super(configurator);
	}

	@ThrowsRuntimeException(ChronicleIOException.class)
	public ChronicleMap<K, V> acquire(LocalDate date) throws ChronicleIOException {
		return super.acquire(String.valueOf(DateTimeUtil.date(Assertor.nonNull(date, "date"))));
	}

}
