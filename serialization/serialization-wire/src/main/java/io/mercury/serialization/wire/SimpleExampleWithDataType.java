package io.mercury.serialization.wire;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.wire.BinaryWire;
import net.openhft.chronicle.wire.TextWire;
import net.openhft.chronicle.wire.Wire;

public class SimpleExampleWithDataType {

	public static void main(String[] args) {

//		simple example with a data type

//		See below for the code for Data. It is much the same and the previous section, with the code required wrapped in a method.

		// Bytes which wraps a ByteBuffer which is resized as needed.
		Bytes<ByteBuffer> bytes = Bytes.elasticByteBuffer();

		Wire wire = new TextWire(bytes);

		Data data = new Data("Hello World", 1234567890L, TimeUnit.NANOSECONDS, 10.50);
		data.writeMarshallable(wire);
		System.out.println(bytes);

		Data data2 = new Data();
		data2.readMarshallable(wire);
		System.out.println(data2);

//		prints
//
//		message: Hello World
//		number: 1234567890
//		code: NANOSECONDS
//		price: 10.5
//
//		Data{message='Hello World', number=1234567890, timeUnit=NANOSECONDS, price=10.5}

//		To write in binary instead

		Bytes<ByteBuffer> bytes2 = Bytes.elasticByteBuffer();
		Wire wire2 = new BinaryWire(bytes2);

		data.writeMarshallable(wire2);
		System.out.println(bytes2.toHexString());

		Data data3 = new Data();
		data3.readMarshallable(wire2);
		System.out.println(data3);

//		prints
//
//		00000000 C7 6D 65 73 73 61 67 65  EB 48 65 6C 6C 6F 20 57 ·message ·Hello W
//		00000010 6F 72 6C 64 C6 6E 75 6D  62 65 72 A3 D2 02 96 49 orld·num ber····I
//		00000020 C8 74 69 6D 65 55 6E 69  74 EB 4E 41 4E 4F 53 45 ·timeUni t·NANOSE
//		00000030 43 4F 4E 44 53 C5 70 72  69 63 65 90 00 00 28 41 CONDS·pr ice···(A
//
//		Data{message='Hello World', number=1234567890, timeUnit=NANOSECONDS, price=10.5}

		
		
		
		
//		In this example we the data is marshalled as a nested data structure.

		// Bytes which wraps a ByteBuffer which is resized as needed.
		Bytes<ByteBuffer> bytes = Bytes.elasticByteBuffer();

		Wire wire = new TextWire(bytes);

		Data data = new Data("Hello World", 1234567890L, TimeUnit.NANOSECONDS, 10.50);
		wire.write(() -> "mydata").marshallable(data);
		System.out.println(bytes);

		Data data2= new Data();
		wire.read(() -> "mydata").marshallable(data2);
		System.out.println(data2);

//		prints
//
//		mydata: {
//		  message: Hello World,
//		  number: 1234567890,
//		  timeUnit: NANOSECONDS,
//		  price: 10.5
//		}
//
//		Data{message='Hello World', number=1234567890, timeUnit=NANOSECONDS, price=10.5}

		To write in binary instead

		Bytes<ByteBuffer> bytes2 = Bytes.elasticByteBuffer();
		Wire wire2 = new BinaryWire(bytes2);

		wire2.write(() -> "mydata").marshallable(data);
		System.out.println(bytes2.toHexString());

		Data data3= new Data();
		wire2.read(() -> "mydata").marshallable(data3);
		System.out.println(data3);

//		prints
//
//		00000000 C6 6D 79 64 61 74 61 82  40 00 00 00 C7 6D 65 73 ·mydata· @····mes
//		00000010 73 61 67 65 EB 48 65 6C  6C 6F 20 57 6F 72 6C 64 sage·Hel lo World
//		00000020 C6 6E 75 6D 62 65 72 A3  D2 02 96 49 C8 74 69 6D ·number· ···I·tim
//		00000030 65 55 6E 69 74 EB 4E 41  4E 4F 53 45 43 4F 4E 44 eUnit·NA NOSECOND
//		00000040 53 C5 70 72 69 63 65 90  00 00 28 41             S·price· ··(A  
//
//		Data{message='Hello World', number=1234567890, timeUnit=NANOSECONDS, price=10.5}

		
		
		
		
		
	}

}
