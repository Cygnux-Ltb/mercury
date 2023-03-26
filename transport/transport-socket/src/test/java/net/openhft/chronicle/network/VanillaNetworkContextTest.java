package net.openhft.chronicle.network;

import net.openhft.chronicle.core.io.BackgroundResourceReleaser;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VanillaNetworkContextTest extends NetworkTestCommon {

	@Test
	public void testClose() {
		final VanillaNetworkContext<?> v = new VanillaNetworkContext<>();
		assertFalse(v.isClosed());
		v.close();
		assertTrue(v.isClosed());
		BackgroundResourceReleaser.releasePendingResources();
		assertTrue(v.isClosed());
		v.close();
		assertTrue(v.isClosed());
	}

}
