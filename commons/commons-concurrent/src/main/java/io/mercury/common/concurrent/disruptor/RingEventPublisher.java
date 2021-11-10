package io.mercury.common.concurrent.disruptor;

import java.util.function.Supplier;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
public interface RingEvent {

	public static class RingInt implements RingEvent {

		private int value;

		public RingInt() {
		}

		public int getValue() {
			return value;
		}

		public RingInt setValue(int value) {
			this.value = value;
			return this;
		}

		static Supplier<RingInt> getEventSupplier() {
			return RingInt::new;
		}
	}

	public static class RingLong implements RingEvent {

		private long value;

		public RingLong() {
		}

		public long getValue() {
			return value;
		}

		public RingLong setValue(long value) {
			this.value = value;
			return this;
		}

		static Supplier<RingLong> getEventSupplier() {
			return RingLong::new;
		}
	}

	public static class RingDouble implements RingEvent {

		private double value;

		public RingDouble() {
		}

		public double getValue() {
			return value;
		}

		public RingDouble setValue(double value) {
			this.value = value;
			return this;
		}

		static Supplier<RingDouble> getEventSupplier() {
			return RingDouble::new;
		}
	}

}
