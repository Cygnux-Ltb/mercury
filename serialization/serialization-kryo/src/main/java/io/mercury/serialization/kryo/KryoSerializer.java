package io.mercury.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import io.mercury.common.serialization.api.Serializer;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class KryoSerializer<T> implements Serializer<T, ByteBuffer> {

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
	public ByteBuffer serialization(@Nonnull T source) {
		kryo.writeObject(output, source);
		return ByteBuffer.wrap(output.toBytes());
	}

}
