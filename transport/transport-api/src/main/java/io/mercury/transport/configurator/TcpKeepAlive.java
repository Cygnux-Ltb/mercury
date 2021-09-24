package io.mercury.transport.configurator;

public final class TcpKeepAlive {

	private final KeepAlive keepAlive;
	private int keepAliveCount;
	private int keepAliveIdle;
	private int keepAliveInterval;

	private TcpKeepAlive(KeepAlive keepAlive) {
		this.keepAlive = keepAlive;
	}

	public KeepAlive getKeepAlive() {
		return keepAlive;
	}

	public int getKeepAliveCount() {
		return keepAliveCount;
	}

	public TcpKeepAlive setKeepAliveCount(int keepAliveCount) {
		this.keepAliveCount = keepAliveCount;
		return this;
	}

	public int getKeepAliveIdle() {
		return keepAliveIdle;
	}

	public TcpKeepAlive setKeepAliveIdle(int keepAliveIdle) {
		this.keepAliveIdle = keepAliveIdle;
		return this;
	}

	public int getKeepAliveInterval() {
		return keepAliveInterval;
	}

	public TcpKeepAlive setKeepAliveInterval(int keepAliveInterval) {
		this.keepAliveInterval = keepAliveInterval;
		return this;
	}

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

	public static enum KeepAlive {

		Enable(1), Disable(0), SysDefault(-1)

		;

		private final int code;

		private KeepAlive(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

	}

}
