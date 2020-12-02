package net.openhft.performance.tests.vanilla.tcp;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import net.openhft.affinity.Affinity;
import net.openhft.chronicle.core.threads.EventLoop;
import net.openhft.chronicle.network.AcceptorEventHandler;
import net.openhft.chronicle.network.TcpEventHandler;
import net.openhft.chronicle.network.VanillaNetworkContext;
import net.openhft.chronicle.threads.EventGroup;
import net.openhft.chronicle.threads.Pauser;
import net.openhft.performance.tests.network.EchoHandler;

public class EchoServer2Main {
    public static <T extends VanillaNetworkContext<T>> void main(String[] args) throws IOException {
        System.setProperty("pauser.minProcessors", "1");
        Affinity.acquireCore();
        @SuppressWarnings("resource")
		@NotNull EventLoop eg = new EventGroup(false, Pauser.busy(), true);
        eg.start();

        @SuppressWarnings({ "rawtypes", "unchecked" })
		@NotNull AcceptorEventHandler<T> eah = new AcceptorEventHandler<T>("*:" + EchoClientMain.PORT,
                nc -> {
                    TcpEventHandler<T> teh = new TcpEventHandler<T>(nc);
                    teh.tcpHandler(new EchoHandler<>());
                    return teh;
                },
                () -> (T) new VanillaNetworkContext());
        eg.addHandler(eah);
    }
}
