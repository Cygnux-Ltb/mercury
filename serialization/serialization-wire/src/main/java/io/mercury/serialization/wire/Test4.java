package io.mercury.serialization.wire;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.core.pool.ClassAliasPool;
import net.openhft.chronicle.wire.BinaryWire;
import net.openhft.chronicle.wire.TextWire;
import net.openhft.chronicle.wire.Wire;

/**
 * simple example with a data type with a type
 * 
 * @author yellow013
 *
 */
public class Test4 {

	public static void main(String[] args) {

		/**
		 * In this example, the type is encoded with the data. Instead of showing the
		 * entire package name which will almost certainly not work on any other
		 * platform, an alias for the type is used. It also means the message is shorter
		 * and faster.
		 */

		// Bytes which wraps a ByteBuffer which is resized as needed.
		Bytes<ByteBuffer> bytes = Bytes.elasticByteBuffer();

		Wire wire = new TextWire(bytes);

		ClassAliasPool.CLASS_ALIASES.addAlias(Data.class);

		Data data = new Data("Hello World", 1234567890L, TimeUnit.NANOSECONDS, 10.50);
		wire.write(() -> "mydata").object(data);
		System.out.println(bytes);

		Data data2 = wire.read(() -> "mydata").object(Data.class);
		System.out.println(data2);

//		prints
//
//		mydata: !Data {
//		  message: Hello World,
//		  number: 1234567890,
//		  timeUnit: NANOSECONDS,
//		  price: 10.5
//		}
//
//		Data{message='Hello World', number=1234567890, timeUnit=NANOSECONDS, price=10.5}

		/**
		 * To write in binary instead
		 */

		Bytes<ByteBuffer> bytes2 = Bytes.elasticByteBuffer();
		Wire wire2 = new BinaryWire(bytes2);

		wire2.write(() -> "mydata").object(data);
		System.out.println(bytes2.toHexString());

		Data data3 = wire2.read(() -> "mydata").object(Data.class);
		System.out.println(data3);

//		prints
//
//		00000000 C6 6D 79 64 61 74 61 B6  04 44 61 74 61 82 40 00 ·mydata· ·Data·@·
//		00000010 00 00 C7 6D 65 73 73 61  67 65 EB 48 65 6C 6C 6F ···messa ge·Hello
//		00000020 20 57 6F 72 6C 64 C6 6E  75 6D 62 65 72 A3 D2 02  World·n umber···
//		00000030 96 49 C8 74 69 6D 65 55  6E 69 74 EB 4E 41 4E 4F ·I·timeU nit·NANO
//		00000040 53 45 43 4F 4E 44 53 C5  70 72 69 63 65 90 00 00 SECONDS· price···
//		00000050 28 41                                            (A               
//
//		Data{message='Hello World', number=1234567890, timeUnit=NANOSECONDS, price=10.5}

	}

}
