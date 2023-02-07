/* Generated SBE (Simple Binary Encoding) message codec. */
package baseline;

import org.agrona.DirectBuffer;

@SuppressWarnings("all")
public final class BoosterDecoder {
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final int ENCODED_LENGTH = 2;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private DirectBuffer buffer;

    public BoosterDecoder wrap(final DirectBuffer buffer, final int offset) {
        if (buffer != this.buffer) {
            this.buffer = buffer;
        }
        this.offset = offset;

        return this;
    }

    public DirectBuffer buffer() {
        return buffer;
    }

    public int offset() {
        return offset;
    }

    public int encodedLength() {
        return ENCODED_LENGTH;
    }

    public int sbeSchemaId() {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion() {
        return SCHEMA_VERSION;
    }

    public static int boostTypeEncodingOffset() {
        return 0;
    }

    public static int boostTypeEncodingLength() {
        return 1;
    }

    public static int boostTypeSinceVersion() {
        return 0;
    }

    public byte boostTypeRaw() {
        return buffer.getByte(offset + 0);
    }

    public BoostType boostType() {
        return BoostType.get(buffer.getByte(offset + 0));
    }


    public static int horsePowerEncodingOffset() {
        return 1;
    }

    public static int horsePowerEncodingLength() {
        return 1;
    }

    public static int horsePowerSinceVersion() {
        return 0;
    }

    public static short horsePowerNullValue() {
        return (short) 255;
    }

    public static short horsePowerMinValue() {
        return (short) 0;
    }

    public static short horsePowerMaxValue() {
        return (short) 254;
    }

    public short horsePower() {
        return ((short) (buffer.getByte(offset + 1) & 0xFF));
    }


    public String toString() {
        if (null == buffer) {
            return "";
        }

        return appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder) {
        if (null == buffer) {
            return builder;
        }

        builder.append('(');
        builder.append("boostType=");
        builder.append(boostType());
        builder.append('|');
        builder.append("horsePower=");
        builder.append(horsePower());
        builder.append(')');

        return builder;
    }
}
