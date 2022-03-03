package io.mercury.transport.attr;

import io.mercury.common.annotation.OnlyOverrideEquals;
import io.mercury.common.serialization.specific.JsonSerializable;
import io.mercury.serialization.json.JsonWrapper;

@OnlyOverrideEquals
public final class TcpKeepAlive implements JsonSerializable {

	private final KeepAliveType keepAlive;
	private int keepAliveCount;
	private int keepAliveIdle;
	private int keepAliveInterval;

	private TcpKeepAlive(KeepAliveType keepAlive) {
		this.keepAlive = keepAlive;
	}

	public KeepAliveType getKeepAlive() {
		return keepAlive;
	}

	public int getKeepAliveCount() {
		return keepAliveCount;
	}

	public int getKeepAliveIdle() {
		return keepAliveIdle;
	}

	public int getKeepAliveInterval() {
		return keepAliveInterval;
	}

	public TcpKeepAlive setKeepAliveCount(int keepAliveCount) {
		this.keepAliveCount = keepAliveCount;
		return this;
	}

	public TcpKeepAlive setKeepAliveIdle(int keepAliveIdle) {
		this.keepAliveIdle = keepAliveIdle;
		return this;
	}

	public TcpKeepAlive setKeepAliveInterval(int keepAliveInterval) {
		this.keepAliveInterval = keepAliveInterval;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof TcpKeepAlive)) {
			return false;
		} else {
			TcpKeepAlive o = (TcpKeepAlive) obj;
			if (!this.keepAlive.equals(o.getKeepAlive()))
				return false;
			if (this.keepAliveCount != o.getKeepAliveCount())
				return false;
			if (this.keepAliveIdle != o.getKeepAliveIdle())
				return false;
			if (this.keepAliveInterval != o.getKeepAliveInterval())
				return false;
			return true;
		}
	}

	/**
	 * 
	 * @return
	 */
	public static final TcpKeepAlive enable() {
		return new TcpKeepAlive(KeepAliveType.Enable);
	}

	/**
	 * 
	 * @return
	 */
	public static final TcpKeepAlive disable() {
		return new TcpKeepAlive(KeepAliveType.Disable);
	}

	/**
	 * 
	 * @return
	 */
	public static final TcpKeepAlive withDefault() {
		return new TcpKeepAlive(KeepAliveType.Default);
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static enum KeepAliveType {

		Enable(1), Disable(0), Default(-1),

		;

		private final int code;

		private KeepAliveType(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

	}

	@Override
	public String toJson() {
		return JsonWrapper.toJson(this);
	}

}
