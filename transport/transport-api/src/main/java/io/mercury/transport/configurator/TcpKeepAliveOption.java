package io.mercury.transport.configurator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public final class TcpKeepAliveOption {

	private KeepAlive keepAlive = KeepAlive.OS_Default;
	private int keepAliveCount;
	private int keepAliveIdle;
	private int keepAliveInterval;

	@Getter
	@RequiredArgsConstructor
	public static enum KeepAlive {

		Enable(1), Disable(0), OS_Default(-1)

		;

		private final int code;

	}

}
