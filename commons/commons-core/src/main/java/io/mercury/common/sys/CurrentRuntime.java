package io.mercury.common.sys;

public final class CurrentRuntime {

	public static int availableProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}

	public static long freeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	public static long maxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	public static long totalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

}
