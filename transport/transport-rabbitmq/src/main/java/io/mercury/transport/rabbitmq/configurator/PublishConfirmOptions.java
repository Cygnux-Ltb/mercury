package io.mercury.transport.rabbitmq.configurator;

public final class PublishConfirmOptions {

	/*
	 * 是否执行发布确认, 默认false
	 */
	private boolean confirm = false;
	/*
	 * 发布确认超时毫秒数, 默认5000毫秒
	 */
	private long confirmTimeout = 5000;
	/*
	 * 发布确认重试次数, 默认3次
	 */
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

	private PublishConfirmOptions(boolean confirm, long confirmTimeout, int confirmRetry) {
		super();
		this.confirm = confirm;
		this.confirmTimeout = confirmTimeout;
		this.confirmRetry = confirmRetry;
	}

	private PublishConfirmOptions() {
	}

	public boolean isConfirm() {
		return confirm;
	}

	public long getConfirmTimeout() {
		return confirmTimeout;
	}

	public int getConfirmRetry() {
		return confirmRetry;
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
