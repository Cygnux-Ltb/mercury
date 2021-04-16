package io.mercury.serialization.kryo;

import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import io.mercury.common.serialization.Serializer;
import io.netty.buffer.ByteBuf;

public class KryoSerializer<T> implements Serializer<T, ByteBuf> {

	private final Kryo kryo;
	private final Output output;

	public KryoSerializer(Class<T> clazz, int buffSize) {
		this.kryo = new Kryo();
		this.kryo.register(clazz);
		this.output = new Output(new ByteArrayOutputStream(buffSize));
	}

	public KryoSerializer(Class<T> clazz) {
		this(clazz, 8192);
	}

	@Override
	public ByteBuf serialization(T source) {

		kryo.writeObject(output, source);

		// TODO Auto-generated method stub
		return null;
	}

}
