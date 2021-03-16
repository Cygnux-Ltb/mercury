package io.mercury.serialization.wire;

import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.mercury.common.log.CommonLogConfigurator;
import io.mercury.common.log.CommonLogConfigurator.LogLevel;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.core.pool.ClassAliasPool;
import net.openhft.chronicle.wire.BinaryWire;
import net.openhft.chronicle.wire.TextWire;
import net.openhft.chronicle.wire.Wire;
import net.openhft.chronicle.wire.Wires;

/**
 * Write a message with a sequence of records
 * 
 * @author yellow013
 *
 */
public class Test6 {
	
	static {
		CommonLogConfigurator.setLogLevel(LogLevel.ERROR);
	}

	public static void main(String[] args) {

		// Bytes which wraps a ByteBuffer which is resized as needed.
		Bytes<ByteBuffer> bytes = Bytes.elasticByteBuffer();

		Wire wire = new TextWire(bytes);

		ClassAliasPool.CLASS_ALIASES.addAlias(Data.class);

		Data[] data = { new Data("Hello World", 98765, TimeUnit.HOURS, 1.5),
				new Data("G'Day All", 1212121, TimeUnit.MINUTES, 12.34),
				new Data("Howyall", 1234567890L, TimeUnit.SECONDS, 1000) };
		wire.writeDocument(false, w -> w.write(() -> "mydata").sequence(v -> Stream.of(data).forEach(v::object)));
		System.out.println(Wires.fromSizePrefixedBlobs(bytes));

		List<Data> dataList = new ArrayList<>();
		assertTrue(wire.readDocument(null, w -> w.read(() -> "mydata").sequence(dataList, (list, v) -> {
			while (v.hasNextSequenceItem())
				dataList.add(v.object(Data.class));
		})));
		dataList.forEach(System.out::println);

//		prints
//
//		--- !!data
//		mydata: [
//		  !Data {
//		    message: Hello World,
//		    number: 98765,
//		    timeUnit: HOURS,
//		    price: 1.5
//		},
//		  !Data {
//		    message: G'Day All,
//		    number: 1212121,
//		    timeUnit: MINUTES,
//		    price: 12.34
//		},
//		  !Data {
//		    message: Howyall,
//		    number: 1234567890,
//		    timeUnit: SECONDS,
//		    price: 1000
//		}
//		]
//
//		Data{message='Hello World', number=98765, timeUnit=HOURS, price=1.5}
//		Data{message='G'Day All', number=1212121, timeUnit=MINUTES, price=12.34}
//		Data{message='Howyall', number=1234567890, timeUnit=SECONDS, price=1000.0}

		/**
		 * To write in binary instead
		 */

		Bytes<ByteBuffer> bytes2 = Bytes.elasticByteBuffer();
		Wire wire2 = new BinaryWire(bytes2);

		wire2.writeDocument(false, w -> w.write(() -> "mydata").sequence(v -> Stream.of(data).forEach(v::object)));
		System.out.println(Wires.fromSizePrefixedBlobs(bytes2));

		List<Data> dataList2 = new ArrayList<>();
		assertTrue(wire2.readDocument(null, w -> w.read(() -> "mydata").sequence(dataList2, (list, v) -> {
			while (v.hasNextSequenceItem())
				dataList2.add(v.object(Data.class));
		})));
		dataList2.forEach(System.out::println);

//		prints
//
//		--- !!data #binary
//		mydata: [
//		  !Data {
//		    message: Hello World,
//		    number: 98765,
//		    timeUnit: HOURS,
//		    price: 1.5
//		},
//		  !Data {
//		    message: G'Day All,
//		    number: 1212121,
//		    timeUnit: MINUTES,
//		    price: 12.34
//		},
//		  !Data {
//		    message: Howyall,
//		    number: 1234567890,
//		    timeUnit: SECONDS,
//		    price: 1000
//		}
//		]
//
//		Data{message='Hello World', number=98765, timeUnit=HOURS, price=1.5}
//		Data{message='G'Day All', number=1212121, timeUnit=MINUTES, price=12.34}
//		Data{message='Howyall', number=1234567890, timeUnit=SECONDS, price=1000.0}	

	}

}
