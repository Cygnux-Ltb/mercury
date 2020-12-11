package io.mercury.serialization.wire;

import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireOut;

/**
 * The code for the class Data
 */
public class Data implements Marshallable {

	private String message;
	private long number;
	private TimeUnit timeUnit;
	private double price;

	public Data() {
	}

	public Data(String message, long number, TimeUnit timeUnit, double price) {
		this.message = message;
		this.number = number;
		this.timeUnit = timeUnit;
		this.price = price;
	}

	@Override
	public void readMarshallable(@NotNull WireIn wire) throws IllegalStateException {
		wire.read(() -> "message").text(this, (obj, s) -> this.message = s)
			.read(() -> "number").int64(this, (obj, i) -> this.number = i)
			.read(() -> "timeUnit").asEnum(TimeUnit.class, e -> this.timeUnit = e)
			.read(() -> "price").float64(this, (obj, d) -> this.price = d);
	}

	@Override
	public void writeMarshallable(WireOut wire) {
		wire.write(() -> "message").text(message)
			.write(() -> "number").int64(number)
			.write(() -> "timeUnit").asEnum(timeUnit)
			.write(() -> "price").float64(price);
	}

	@Override
	public String toString() {
		return "Data{" + "message='" + message + '\'' + ", number=" + number + ", timeUnit=" + timeUnit + ", price="
				+ price + '}';
	}

}
