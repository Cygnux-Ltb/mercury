package io.mercury.serialization.protostuff;

import java.io.IOException;
import java.util.UUID;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.WireFormat.FieldType;
import io.protostuff.runtime.Delegate;

public final class ProtostuffDemo {

    public static final class Singleton {
        public static final Singleton INSTANCE = new Singleton();

        private Singleton() {
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj && obj == INSTANCE;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(this);
        }
    }

    public static final Delegate<Singleton> SINGLETON_DELEGATE = new Delegate<>() {

        @Override
        public Class<?> typeClass() {
            return Singleton.class;
        }

        @Override
        public FieldType getFieldType() {
            return FieldType.UINT32;
        }

        @Override
        public void writeTo(Output output, int number, Singleton value, boolean repeated) throws IOException {
            output.writeUInt32(number, 0, repeated);
        }

        @Override
        public Singleton readFrom(Input input) throws IOException {
            if (0 != input.readUInt32())
                throw new ProtostuffException("Corrupt input.");
            return Singleton.INSTANCE;
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }
    };

    public static final Delegate<UUID> UUID_DELEGATE = new Delegate<>() {

        @Override
        public FieldType getFieldType() {
            return FieldType.BYTES;
        }

        @Override
        public Class<?> typeClass() {
            return UUID.class;
        }

        @Override
        public UUID readFrom(Input input) throws IOException {
            final byte[] buf = input.readByteArray();
            if (buf.length != 16)
                throw new ProtostuffException("Corrupt input.");
            return new UUID(toInt64(buf, 0), toInt64(buf, 8));
        }

        @Override
        public void writeTo(Output output, int number, UUID value, boolean repeated) throws IOException {
            final byte[] buf = new byte[16];
            writeInt64(value.getMostSignificantBits(), buf, 0);
            writeInt64(value.getLeastSignificantBits(), buf, 8);
            output.writeByteArray(number, buf, repeated);
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
            input.transferByteRangeTo(output, false, number, repeated);
        }
    };

    public static final Delegate<short[]> SHORT_ARRAY_DELEGATE = new Delegate<>() {

        @Override
        public Class<short[]> typeClass() {
            return short[].class;
        }

        @Override
        public FieldType getFieldType() {
            return FieldType.BYTES;
        }

        @Override
        public void writeTo(Output output, int number, short[] value, boolean repeated) throws IOException {
            byte[] buffer = new byte[value.length * 2];
            for (int i = 0, offset = 0; i < value.length; i++) {
                short s = value[i];
                buffer[offset++] = (byte) ((s >>> 8) & 0xFF);
                buffer[offset++] = (byte) ((s) & 0xFF);
            }
            output.writeByteArray(number, buffer, repeated);
        }

        @Override
        public short[] readFrom(Input input) throws IOException {
            byte[] buffer = input.readByteArray();
            short[] s = new short[buffer.length / 2];
            for (int i = 0, offset = 0; i < s.length; i++) {
                s[i] = (short) ((buffer[offset++] & 0xFF) << 8 | (buffer[offset++] & 0xFF));
            }
            return s;
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
            input.transferByteRangeTo(output, false, number, repeated);
        }
    };

    static long toInt64(final byte[] buffer, int offset) {
        final byte b1 = buffer[offset++];
        final byte b2 = buffer[offset++];
        final byte b3 = buffer[offset++];
        final byte b4 = buffer[offset++];
        final byte b5 = buffer[offset++];
        final byte b6 = buffer[offset++];
        final byte b7 = buffer[offset++];
        final byte b8 = buffer[offset];

        return (((long) b8 & 0xff)) | (((long) b7 & 0xff) << 8) | (((long) b6 & 0xff) << 16)
                | (((long) b5 & 0xff) << 24) | (((long) b4 & 0xff) << 32) | (((long) b3 & 0xff) << 40)
                | (((long) b2 & 0xff) << 48) | (((long) b1 & 0xff) << 56);
    }

    static void writeInt64(final long value, final byte[] buffer, int offset) {
        buffer[offset++] = (byte) (value >>> 56);
        buffer[offset++] = (byte) (value >>> 48);
        buffer[offset++] = (byte) (value >>> 40);
        buffer[offset++] = (byte) (value >>> 32);
        buffer[offset++] = (byte) (value >>> 24);
        buffer[offset++] = (byte) (value >>> 16);
        buffer[offset++] = (byte) (value >>> 8);
        buffer[offset] = (byte) (value);
    }

}
