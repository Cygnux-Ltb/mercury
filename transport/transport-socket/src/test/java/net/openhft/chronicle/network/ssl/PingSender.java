package net.openhft.chronicle.network.ssl;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import net.openhft.chronicle.core.threads.EventHandler;
import net.openhft.chronicle.network.NetworkContext;
import net.openhft.chronicle.network.connection.CoreFields;

final class PingSender implements EventHandler {

	private final Supplier<NetworkContext<?>> nc;
	@SuppressWarnings("unused")
	private final IntSupplier local;
	@SuppressWarnings("unused")
	private final IntSupplier remote;
	private final long cid;
	private long lastPublish = 0;
	private int counter;

	PingSender(final Supplier<NetworkContext<?>> nc, final IntSupplier local, final IntSupplier remote,
			final long cid) {
		this.nc = nc;
		this.local = local;
		this.remote = remote;
		this.cid = cid;
	}

	@Override
	public boolean action() {
		if (lastPublish < System.currentTimeMillis() - 5000L) {

			nc.get().wireOutPublisher().put(null, wireOut -> {
				wireOut.writeDocument(true, d -> d.write(CoreFields.cid).int64(cid));
				wireOut.writeDocument(false, d -> d.writeEventName("ping").int32(counter++));

			});
			lastPublish = System.currentTimeMillis();
		}
		return false;
	}
}
