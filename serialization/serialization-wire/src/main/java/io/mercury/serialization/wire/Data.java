package io.mercury.serialization.wire;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import io.mercury.serialization.json.JsonWrapper;
import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireOut;

/**
 * The code for the class Data
 */
public final class Data implements Marshallable {

	private String message;
	private long number;
	private TimeUnit timeUnit;
	private double price;

	private static final String Field1 = "message";
	private static final String Field2 = "number";
	private static final String Field3 = "timeUnit";
	private static final String Field4 = "price";

	public Data() {
	}

	public Data(String message, long number, TimeUnit timeUnit, double price) {
		this.message = message;
		this.number = number;
		this.timeUnit = timeUnit;
		this.price = price;
	}

	@Override
	public void readMarshallable(@Nonnull WireIn wire) throws IllegalStateException {
		wire.read(() -> Field1).text(this, (obj, s) -> this.message = s)
			.read(() -> Field2).int64(this, (obj, i) -> this.number = i)
			.read(() -> Field3).asEnum(TimeUnit.class, e -> this.timeUnit = e)
			.read(() -> Field4).float64(this, (obj, d) -> this.price = d);
	}

	@Override
	public void writeMarshallable(@Nonnull WireOut wire) {
		wire.write(() -> Field1).text(message)
			.write(() -> Field2).int64(number)
			.write(() -> Field3).asEnum(timeUnit)
			.write(() -> Field4).float64(price);
	}

	@Override
	public String toString() {
		return JsonWrapper.toJson(this);
	}

}
