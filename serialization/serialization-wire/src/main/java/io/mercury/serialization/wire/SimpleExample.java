package io.mercury.serialization.wire;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import io.mercury.common.log.LogConfigurator;
import io.mercury.common.log.LogConfigurator.LogLevel;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.wire.BinaryWire;
import net.openhft.chronicle.wire.RawWire;
import net.openhft.chronicle.wire.TextWire;
import net.openhft.chronicle.wire.Wire;
import net.openhft.chronicle.wire.WireType;

/**
 * Simple example
 * 
 * @author yellow013
 *
 */
public class SimpleExample {

	static {
		LogConfigurator.setLogLevel(LogLevel.ERROR);
	}
	
	public static void main(String[] args) {

		/**
		 * First you need to have a buffer to write to. This can be a byte[], <br>
		 * a ByteBuffer, off heap memory, or even an address and length you have
		 * obtained from some other library.
		 */

		// Bytes which wraps a ByteBuffer which is resized as needed.
		Bytes<ByteBuffer> textBytes = Bytes.elasticByteBuffer();

		/**
		 * Now you can choose which format you are using. As the wire formats are
		 * themselves unbuffered, you can use them with the same buffer, but in general
		 * using one wire format is easier.
		 */

		Wire textWire = new TextWire(textBytes);
		// or
		Wire textWire1 = WireType.TEXT.apply(textBytes);
		// or
		Bytes<ByteBuffer> binaryBytes = Bytes.elasticByteBuffer();
		Wire binaryWire = new BinaryWire(binaryBytes);
		// or
		Bytes<ByteBuffer> rawBytes = Bytes.elasticByteBuffer();
		Wire rawWire = new RawWire(rawBytes);

		/**
		 * So now you can write to the wire with a simple document.
		 */
		textWire.write(() -> "message").text("Hello World")
				.write(() -> "number").int64(1234567890L)
				.write(() -> "code").asEnum(TimeUnit.SECONDS)
				.write(() -> "price").float64(10.50);
		System.out.println(textBytes);
		System.out.println(textWire1);

//		prints
//
//		message: Hello World
//		number: 1234567890
//		code: SECONDS
//		price: 10.5

		/**
		 * Using toHexString prints out a binary file hex view of the buffer's contents.
		 */

		// the same code as for text wire
		binaryWire.write(() -> "message").text("Hello World")
				.write(() -> "number").int64(1234567890L)
				.write(() -> "code").asEnum(TimeUnit.SECONDS)
				.write(() -> "price").float64(10.50);
		System.out.println(binaryBytes.toHexString());

		// to obtain the underlying ByteBuffer to write to a Channel
		ByteBuffer byteBuffer = binaryBytes.underlyingObject();
		byteBuffer.position(0);
		byteBuffer.limit(binaryBytes.length());

//		prints
//
//		00000000 C7 6D 65 73 73 61 67 65  EB 48 65 6C 6C 6F 20 57 ·message ·Hello W
//		00000010 6F 72 6C 64 C6 6E 75 6D  62 65 72 A3 D2 02 96 49 orld·num ber····I
//		00000020 C4 63 6F 64 65 E7 53 45  43 4F 4E 44 53 C5 70 72 ·code·SE CONDS·pr
//		00000030 69 63 65 90 00 00 28 41                          ice···(A 

		/**
		 * Using the RawWire strips away all the meta data to reduce the size of the
		 * message, and improve speed. The down side is that we cannot easily see what
		 * the message contains.
		 */

		// the same code as for text wire
		rawWire.write(() -> "message").text("Hello World")
				.write(() -> "number").int64(1234567890L)
				.write(() -> "code").asEnum(TimeUnit.SECONDS)
				.write(() -> "price").float64(10.50);
		System.out.println(rawBytes.toHexString());

//		prints in RawWire
//
//		00000000 0B 48 65 6C 6C 6F 20 57  6F 72 6C 64 D2 02 96 49 ·Hello W orld···I
//		00000010 00 00 00 00 07 53 45 43  4F 4E 44 53 00 00 00 00 ·····SEC ONDS····
//		00000020 00 00 25 40                                      ··%@ 

	}

}
