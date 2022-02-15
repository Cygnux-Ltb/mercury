/* Generated SBE (Simple Binary Encoding) message codec. */
package extension;

import org.agrona.DirectBuffer;

@SuppressWarnings("all")
public final class EngineDecoder
{
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 1;
    public static final int ENCODED_LENGTH = 10;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private DirectBuffer buffer;

    public EngineDecoder wrap(final DirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;

        return this;
    }

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public static int capacityEncodingOffset()
    {
        return 0;
    }

    public static int capacityEncodingLength()
    {
        return 2;
    }

    public static int capacitySinceVersion()
    {
        return 0;
    }

    public static int capacityNullValue()
    {
        return 65535;
    }

    public static int capacityMinValue()
    {
        return 0;
    }

    public static int capacityMaxValue()
    {
        return 65534;
    }

    public int capacity()
    {
        return (buffer.getShort(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
    }


    public static int numCylindersEncodingOffset()
    {
        return 2;
    }

    public static int numCylindersEncodingLength()
    {
        return 1;
    }

    public static int numCylindersSinceVersion()
    {
        return 0;
    }

    public static short numCylindersNullValue()
    {
        return (short)255;
    }

    public static short numCylindersMinValue()
    {
        return (short)0;
    }

    public static short numCylindersMaxValue()
    {
        return (short)254;
    }

    public short numCylinders()
    {
        return ((short)(buffer.getByte(offset + 2) & 0xFF));
    }


    public static int maxRpmEncodingOffset()
    {
        return 3;
    }

    public static int maxRpmEncodingLength()
    {
        return 0;
    }

    public static int maxRpmSinceVersion()
    {
        return 0;
    }

    public static int maxRpmNullValue()
    {
        return 65535;
    }

    public static int maxRpmMinValue()
    {
        return 0;
    }

    public static int maxRpmMaxValue()
    {
        return 65534;
    }

    public int maxRpm()
    {
        return 9000;
    }

    public static int manufacturerCodeEncodingOffset()
    {
        return 3;
    }

    public static int manufacturerCodeEncodingLength()
    {
        return 3;
    }

    public static int manufacturerCodeSinceVersion()
    {
        return 0;
    }

    public static byte manufacturerCodeNullValue()
    {
        return (byte)0;
    }

    public static byte manufacturerCodeMinValue()
    {
        return (byte)32;
    }

    public static byte manufacturerCodeMaxValue()
    {
        return (byte)126;
    }

    public static int manufacturerCodeLength()
    {
        return 3;
    }


    public byte manufacturerCode(final int index)
    {
        if (index < 0 || index >= 3)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 3 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String manufacturerCodeCharacterEncoding()
    {
        return "US-ASCII";
    }

    public int getManufacturerCode(final byte[] dst, final int dstOffset)
    {
        final int length = 3;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 3, dst, dstOffset, length);

        return length;
    }

    public String manufacturerCode()
    {
        final byte[] dst = new byte[3];
        buffer.getBytes(offset + 3, dst, 0, 3);

        int end = 0;
        for (; end < 3 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getManufacturerCode(final Appendable value)
    {
        for (int i = 0; i < 3; ++i)
        {
            final int c = buffer.getByte(offset + 3 + i) & 0xFF;
            if (c == 0)
            {
                return i;
            }

            try
            {
                value.append(c > 127 ? '?' : (char)c);
            }
            catch (final java.io.IOException ex)
            {
                throw new java.io.UncheckedIOException(ex);
            }
        }

        return 3;
    }


    public static int fuelEncodingOffset()
    {
        return 6;
    }

    public static int fuelEncodingLength()
    {
        return 0;
    }

    public static int fuelSinceVersion()
    {
        return 0;
    }

    public static byte fuelNullValue()
    {
        return (byte)0;
    }

    public static byte fuelMinValue()
    {
        return (byte)32;
    }

    public static byte fuelMaxValue()
    {
        return (byte)126;
    }


    private static final byte[] FUEL_VALUE = { 80, 101, 116, 114, 111, 108 };

    public static int fuelLength()
    {
        return 6;
    }


    public byte fuel(final int index)
    {
        return FUEL_VALUE[index];
    }

    public int getFuel(final byte[] dst, final int offset, final int length)
    {
        final int bytesCopied = Math.min(length, 6);
        System.arraycopy(FUEL_VALUE, 0, dst, offset, bytesCopied);

        return bytesCopied;
    }

    public String fuel()
    {
        return "Petrol";
    }


    public static int efficiencyEncodingOffset()
    {
        return 6;
    }

    public static int efficiencyEncodingLength()
    {
        return 1;
    }

    public static int efficiencySinceVersion()
    {
        return 0;
    }

    public static byte efficiencyNullValue()
    {
        return (byte)-128;
    }

    public static byte efficiencyMinValue()
    {
        return (byte)0;
    }

    public static byte efficiencyMaxValue()
    {
        return (byte)100;
    }

    public byte efficiency()
    {
        return buffer.getByte(offset + 6);
    }


    public static int boosterEnabledEncodingOffset()
    {
        return 7;
    }

    public static int boosterEnabledEncodingLength()
    {
        return 1;
    }

    public static int boosterEnabledSinceVersion()
    {
        return 0;
    }

    public short boosterEnabledRaw()
    {
        return ((short)(buffer.getByte(offset + 7) & 0xFF));
    }

    public BooleanType boosterEnabled()
    {
        return BooleanType.get(((short)(buffer.getByte(offset + 7) & 0xFF)));
    }


    public static int boosterEncodingOffset()
    {
        return 8;
    }

    public static int boosterEncodingLength()
    {
        return 2;
    }

    public static int boosterSinceVersion()
    {
        return 0;
    }

    private final BoosterDecoder booster = new BoosterDecoder();

    public BoosterDecoder booster()
    {
        booster.wrap(buffer, offset + 8);
        return booster;
    }

    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        return appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        builder.append('(');
        builder.append("capacity=");
        builder.append(capacity());
        builder.append('|');
        builder.append("numCylinders=");
        builder.append(numCylinders());
        builder.append('|');
        builder.append("manufacturerCode=");
        for (int i = 0; i < manufacturerCodeLength() && manufacturerCode(i) > 0; i++)
        {
            builder.append((char)manufacturerCode(i));
        }
        builder.append('|');
        builder.append("efficiency=");
        builder.append(efficiency());
        builder.append('|');
        builder.append("boosterEnabled=");
        builder.append(boosterEnabled());
        builder.append('|');
        builder.append("booster=");
        final BoosterDecoder booster = booster();
        if (booster != null)
        {
            booster.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append(')');

        return builder;
    }
}
