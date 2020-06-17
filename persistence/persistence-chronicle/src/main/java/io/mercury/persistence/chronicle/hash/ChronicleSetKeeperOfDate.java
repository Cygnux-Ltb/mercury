package io.mercury.persistence.chronicle.hash;

import java.time.LocalDate;

import io.mercury.common.annotation.lang.ThrowsRuntimeException;
import io.mercury.common.datetime.DateTimeUtil;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.set.ChronicleSet;

public final class ChronicleSetKeeperOfDate<K> extends ChronicleSetKeeper<K> {

	public ChronicleSetKeeperOfDate(ChronicleSetConfigurator<K> options) {
		super(options);
	}

	@ThrowsRuntimeException(ChronicleIOException.class)
	public ChronicleSet<K> acquire(LocalDate date) throws ChronicleIOException {
		return super.acquire(String.valueOf(DateTimeUtil.date(date)));
	}

}
