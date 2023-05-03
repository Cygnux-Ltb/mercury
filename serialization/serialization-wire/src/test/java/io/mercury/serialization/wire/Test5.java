package io.mercury.serialization.wire;

import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import io.mercury.common.log4j2.Log4j2Configurator;
import io.mercury.common.log4j2.Log4j2Configurator.LogLevel;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.core.pool.ClassAliasPool;
import net.openhft.chronicle.wire.BinaryWire;
import net.openhft.chronicle.wire.TextWire;
import net.openhft.chronicle.wire.Wire;
import net.openhft.chronicle.wire.Wires;

/**
 * Write a message with a thread safe size prefix
 * 
 * @author yellow013
 *
 */
public class Test5 {
	
	static {
		Log4j2Configurator.setLogLevel(LogLevel.ERROR);
	}

	public static void main(String[] args) {

		/**
		 * 
		 * The benefits of using this approach are that <br>
		 * ~ the reader can block until the message is complete. <br>
		 * ~ if you have concurrent writers, they will block unless the size if know in
		 * which case it skip the message(s) still being written.
		 */

		// Bytes which wraps a ByteBuffer which is resized as needed.
		Bytes<ByteBuffer> bytes = Bytes.elasticByteBuffer();

		Wire wire = new TextWire(bytes);

		ClassAliasPool.CLASS_ALIASES.addAlias(Data.class);

		Data data = new Data("Hello World", 1234567890L, TimeUnit.NANOSECONDS, 10.50);
		wire.writeDocument(false, data);
		System.out.println(Wires.fromSizePrefixedBlobs(bytes));

		Data data2 = new Data();
		assertTrue(wire.readDocument(null, data2));
		System.out.println(data2);

//		prints
//
//		--- !!data
//		message: Hello World
//		number: 1234567890
//		timeUnit: NANOSECONDS
//		price: 10.5
//
//		Data{message='Hello World', number=1234567890, timeUnit=NANOSECONDS, price=10.5}

		/**
		 * To write in binary instead
		 */

		Bytes<ByteBuffer> bytes2 = Bytes.elasticByteBuffer();
		Wire wire2 = new BinaryWire(bytes2);

		wire2.writeDocument(false, data);
		System.out.println(Wires.fromSizePrefixedBlobs(bytes2));

		Data data3 = new Data();
		assertTrue(wire2.readDocument(null, data3));
		System.out.println(data3);

//		prints
//
//		--- !!data #binary
//		message: Hello World
//		number: 1234567890
//		timeUnit: NANOSECONDS
//		price: 10.5
//
//		Data{message='Hello World', number=1234567890, timeUnit=NANOSECONDS, price=10.5}	

	}

}
