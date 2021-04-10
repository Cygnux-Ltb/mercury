package io.mercury.transport.configurator;

import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor(access = PRIVATE)
public final class TcpKeepAlive {

	private final KeepAlive keepAlive;
	private int keepAliveCount;
	private int keepAliveIdle;
	private int keepAliveInterval;

	/**
	 * 
	 * @return
	 */
	public static final TcpKeepAlive enable() {
		return new TcpKeepAlive(KeepAlive.Enable);
	}

	/**
	 * 
	 * @return
	 */
	public static final TcpKeepAlive disable() {
		return new TcpKeepAlive(KeepAlive.Disable);
	}

	/**
	 * 
	 * @return
	 */
	public static final TcpKeepAlive sysDefault() {
		return new TcpKeepAlive(KeepAlive.SysDefault);
	}

	@Getter
	@RequiredArgsConstructor
	public static enum KeepAlive {

		Enable(1), Disable(0), SysDefault(-1)

		;

		private final int code;

	}

}
