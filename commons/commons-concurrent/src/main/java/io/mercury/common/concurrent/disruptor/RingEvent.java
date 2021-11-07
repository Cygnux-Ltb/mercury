package io.mercury.common.disruptor;

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

		private RingInt() {
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

		private RingLong() {
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

		private RingDouble() {
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
