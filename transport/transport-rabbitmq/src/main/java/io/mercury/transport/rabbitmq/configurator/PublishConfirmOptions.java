package io.mercury.transport.rabbitmq.configurator;

import lombok.Getter;

public final class PublishConfirmOptions {

	// 是否执行发布确认, 默认false
	@Getter
	private boolean confirm = false;

	// 发布确认超时毫秒数, 默认5000毫秒
	@Getter
	private long confirmTimeout = 5000;

	// 发布确认重试次数, 默认3次
	@Getter
	private int confirmRetry = 3;

	/**
	 * 使用默认参数
	 * 
	 * @return
	 */
	public static final PublishConfirmOptions defaultOption() {
		return new PublishConfirmOptions();
	}

	/**
	 * 指定具体参数
	 * 
	 * @return
	 */
	public static final PublishConfirmOptions withOption(boolean confirm, long confirmTimeout, int confirmRetry) {
		return new PublishConfirmOptions(confirm, confirmTimeout, confirmRetry);
	}

	private PublishConfirmOptions() {
	}

	private PublishConfirmOptions(boolean confirm, long confirmTimeout, int confirmRetry) {
		this.confirm = confirm;
		this.confirmTimeout = confirmTimeout;
		this.confirmRetry = confirmRetry;
	}

	public PublishConfirmOptions setConfirm(boolean confirm) {
		this.confirm = confirm;
		return this;
	}

	public PublishConfirmOptions setConfirmTimeout(long confirmTimeout) {
		this.confirmTimeout = confirmTimeout;
		return this;
	}

	public PublishConfirmOptions setConfirmRetry(int confirmRetry) {
		this.confirmRetry = confirmRetry;
		return this;
	}

}
