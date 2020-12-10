package io.mercury.serialization.wire;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.wire.BinaryWire;
import net.openhft.chronicle.wire.RawWire;
import net.openhft.chronicle.wire.TextWire;
import net.openhft.chronicle.wire.Wire;
import net.openhft.chronicle.wire.WireType;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		/**
		 * First you need to have a buffer to write to. This can be a byte[], a
		 * ByteBuffer, off-heap memory, or even an address and length that you have
		 * obtained from some other library.
		 */
		// Bytes which wraps a ByteBuffer which is resized as needed.
		Bytes<ByteBuffer> bytes = Bytes.elasticByteBuffer();

		/**
		 * Now you can choose which format you are using. As the wire formats are
		 * themselves unbuffered, you can use them with the same buffer, but in general
		 * using one wire format is easier.
		 */
		Wire wire = new TextWire(bytes);
		// or
		WireType wireType = WireType.TEXT;
		Wire wireB = wireType.apply(bytes);
		// or
		Bytes<ByteBuffer> bytes2 = Bytes.elasticByteBuffer();
		Wire wire2 = new BinaryWire(bytes2);
		// or
		Bytes<ByteBuffer> bytes3 = Bytes.elasticByteBuffer();
		Wire wire3 = new RawWire(bytes3);

		/**
		 * So now you can write to the wire with a simple document.
		 */
		wire.write("message").text("Hello World").write("number").int64(1234567890L).write("code")
				.asEnum(TimeUnit.SECONDS).write("price").float64(10.50);
		System.out.println(bytes);

		// the same code as for text wire
		wire2.write("message").text("Hello World").write("number").int64(1234567890L).write("code")
				.asEnum(TimeUnit.SECONDS).write("price").float64(10.50);
		System.out.println(bytes2.toHexString());

		/*
		 * Using RawWire strips away all the meta data to reduce the size of the
		 * message, and improve speed. The down-side is that we cannot easily see what
		 * the message contains.
		 */

		// the same code as for raw wire
		wire3.write("message").text("Hello World").write("number").int64(1234567890L).write("code")
				.asEnum(TimeUnit.SECONDS).write("price").float64(10.50);
		System.out.println(bytes3.toHexString());

	}
}
