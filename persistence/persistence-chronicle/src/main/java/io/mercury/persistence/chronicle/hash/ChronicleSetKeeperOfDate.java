package io.mercury.persistence.chronicle.hash;

import java.time.LocalDate;

import io.mercury.common.datetime.DateTimeUtil;
import io.mercury.common.lang.Asserter;
import io.mercury.persistence.chronicle.exception.ChronicleIOException;
import net.openhft.chronicle.set.ChronicleSet;

public final class ChronicleSetKeeperOfDate<E> extends ChronicleSetKeeper<E> {

	public ChronicleSetKeeperOfDate(ChronicleSetConfigurator<E> cfg) {
		super(cfg);
	}

	public ChronicleSet<E> acquire(LocalDate date) throws ChronicleIOException {
		Asserter.nonNull(date, "date");
		return super.acquire(Integer.toString(DateTimeUtil.date(date)));
	}

}
