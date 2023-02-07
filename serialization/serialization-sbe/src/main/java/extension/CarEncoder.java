/* Generated SBE (Simple Binary Encoding) message codec. */
package extension;

import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;


/**
 * Description of a basic Car
 */
@SuppressWarnings("all")
public final class CarEncoder {
    public static final int BLOCK_LENGTH = 62;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 1;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final CarEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
    private int initialOffset;
    private int offset;
    private int limit;

    public int sbeBlockLength() {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId() {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId() {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion() {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType() {
        return "";
    }

    public MutableDirectBuffer buffer() {
        return buffer;
    }

    public int initialOffset() {
        return initialOffset;
    }

    public int offset() {
        return offset;
    }

    public CarEncoder wrap(final MutableDirectBuffer buffer, final int offset) {
        if (buffer != this.buffer) {
            this.buffer = buffer;
        }
        this.initialOffset = offset;
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public CarEncoder wrapAndApplyHeader(
            final MutableDirectBuffer buffer, final int offset, final MessageHeaderEncoder headerEncoder) {
        headerEncoder
                .wrap(buffer, offset)
                .blockLength(BLOCK_LENGTH)
                .templateId(TEMPLATE_ID)
                .schemaId(SCHEMA_ID)
                .version(SCHEMA_VERSION);

        return wrap(buffer, offset + MessageHeaderEncoder.ENCODED_LENGTH);
    }

    public int encodedLength() {
        return limit - offset;
    }

    public int limit() {
        return limit;
    }

    public void limit(final int limit) {
        this.limit = limit;
    }

    public static int serialNumberId() {
        return 1;
    }

    public static int serialNumberSinceVersion() {
        return 0;
    }

    public static int serialNumberEncodingOffset() {
        return 0;
    }

    public static int serialNumberEncodingLength() {
        return 8;
    }

    public static String serialNumberMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public static long serialNumberNullValue() {
        return 0xffffffffffffffffL;
    }

    public static long serialNumberMinValue() {
        return 0x0L;
    }

    public static long serialNumberMaxValue() {
        return 0xfffffffffffffffeL;
    }

    public CarEncoder serialNumber(final long value) {
        buffer.putLong(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int modelYearId() {
        return 2;
    }

    public static int modelYearSinceVersion() {
        return 0;
    }

    public static int modelYearEncodingOffset() {
        return 8;
    }

    public static int modelYearEncodingLength() {
        return 2;
    }

    public static String modelYearMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public static int modelYearNullValue() {
        return 65535;
    }

    public static int modelYearMinValue() {
        return 0;
    }

    public static int modelYearMaxValue() {
        return 65534;
    }

    public CarEncoder modelYear(final int value) {
        buffer.putShort(offset + 8, (short) value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int availableId() {
        return 3;
    }

    public static int availableSinceVersion() {
        return 0;
    }

    public static int availableEncodingOffset() {
        return 10;
    }

    public static int availableEncodingLength() {
        return 1;
    }

    public static String availableMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public CarEncoder available(final BooleanType value) {
        buffer.putByte(offset + 10, (byte) value.value());
        return this;
    }

    public static int codeId() {
        return 4;
    }

    public static int codeSinceVersion() {
        return 0;
    }

    public static int codeEncodingOffset() {
        return 11;
    }

    public static int codeEncodingLength() {
        return 1;
    }

    public static String codeMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public CarEncoder code(final Model value) {
        buffer.putByte(offset + 11, value.value());
        return this;
    }

    public static int someNumbersId() {
        return 5;
    }

    public static int someNumbersSinceVersion() {
        return 0;
    }

    public static int someNumbersEncodingOffset() {
        return 12;
    }

    public static int someNumbersEncodingLength() {
        return 16;
    }

    public static String someNumbersMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public static long someNumbersNullValue() {
        return 4294967295L;
    }

    public static long someNumbersMinValue() {
        return 0L;
    }

    public static long someNumbersMaxValue() {
        return 4294967294L;
    }

    public static int someNumbersLength() {
        return 4;
    }


    public CarEncoder someNumbers(final int index, final long value) {
        if (index < 0 || index >= 4) {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 12 + (index * 4);
        buffer.putInt(pos, (int) value, java.nio.ByteOrder.LITTLE_ENDIAN);

        return this;
    }

    public CarEncoder putSomeNumbers(final long value0, final long value1, final long value2, final long value3) {
        buffer.putInt(offset + 12, (int) value0, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(offset + 16, (int) value1, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(offset + 20, (int) value2, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(offset + 24, (int) value3, java.nio.ByteOrder.LITTLE_ENDIAN);

        return this;
    }

    public static int vehicleCodeId() {
        return 6;
    }

    public static int vehicleCodeSinceVersion() {
        return 0;
    }

    public static int vehicleCodeEncodingOffset() {
        return 28;
    }

    public static int vehicleCodeEncodingLength() {
        return 6;
    }

    public static String vehicleCodeMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public static byte vehicleCodeNullValue() {
        return (byte) 0;
    }

    public static byte vehicleCodeMinValue() {
        return (byte) 32;
    }

    public static byte vehicleCodeMaxValue() {
        return (byte) 126;
    }

    public static int vehicleCodeLength() {
        return 6;
    }


    public CarEncoder vehicleCode(final int index, final byte value) {
        if (index < 0 || index >= 6) {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 28 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String vehicleCodeCharacterEncoding() {
        return "ASCII";
    }

    public CarEncoder putVehicleCode(final byte[] src, final int srcOffset) {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length)) {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 28, src, srcOffset, length);

        return this;
    }

    public CarEncoder vehicleCode(final String src) {
        final int length = 6;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length) {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 28, src);

        for (int start = srcLength; start < length; ++start) {
            buffer.putByte(offset + 28 + start, (byte) 0);
        }

        return this;
    }

    public CarEncoder vehicleCode(final CharSequence src) {
        final int length = 6;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length) {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        for (int i = 0; i < srcLength; ++i) {
            final char charValue = src.charAt(i);
            final byte byteValue = charValue > 127 ? (byte) '?' : (byte) charValue;
            buffer.putByte(offset + 28 + i, byteValue);
        }

        for (int i = srcLength; i < length; ++i) {
            buffer.putByte(offset + 28 + i, (byte) 0);
        }

        return this;
    }

    public static int extrasId() {
        return 7;
    }

    public static int extrasSinceVersion() {
        return 0;
    }

    public static int extrasEncodingOffset() {
        return 34;
    }

    public static int extrasEncodingLength() {
        return 1;
    }

    public static String extrasMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    private final OptionalExtrasEncoder extras = new OptionalExtrasEncoder();

    public OptionalExtrasEncoder extras() {
        extras.wrap(buffer, offset + 34);
        return extras;
    }

    public static int discountedModelId() {
        return 8;
    }

    public static int discountedModelSinceVersion() {
        return 0;
    }

    public static int discountedModelEncodingOffset() {
        return 35;
    }

    public static int discountedModelEncodingLength() {
        return 1;
    }

    public static String discountedModelMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "constant";
        }

        return "";
    }

    public static int engineId() {
        return 9;
    }

    public static int engineSinceVersion() {
        return 0;
    }

    public static int engineEncodingOffset() {
        return 35;
    }

    public static int engineEncodingLength() {
        return 10;
    }

    public static String engineMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    private final EngineEncoder engine = new EngineEncoder();

    public EngineEncoder engine() {
        engine.wrap(buffer, offset + 35);
        return engine;
    }

    public static int uuidId() {
        return 100;
    }

    public static int uuidSinceVersion() {
        return 1;
    }

    public static int uuidEncodingOffset() {
        return 45;
    }

    public static int uuidEncodingLength() {
        return 16;
    }

    public static String uuidMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "optional";
        }

        return "";
    }

    public static long uuidNullValue() {
        return -9223372036854775808L;
    }

    public static long uuidMinValue() {
        return -9223372036854775807L;
    }

    public static long uuidMaxValue() {
        return 9223372036854775807L;
    }

    public static int uuidLength() {
        return 2;
    }


    public CarEncoder uuid(final int index, final long value) {
        if (index < 0 || index >= 2) {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 45 + (index * 8);
        buffer.putLong(pos, value, java.nio.ByteOrder.LITTLE_ENDIAN);

        return this;
    }

    public CarEncoder putUuid(final long value0, final long value1) {
        buffer.putLong(offset + 45, value0, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(offset + 53, value1, java.nio.ByteOrder.LITTLE_ENDIAN);

        return this;
    }

    public static int cupHolderCountId() {
        return 101;
    }

    public static int cupHolderCountSinceVersion() {
        return 1;
    }

    public static int cupHolderCountEncodingOffset() {
        return 61;
    }

    public static int cupHolderCountEncodingLength() {
        return 1;
    }

    public static String cupHolderCountMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "optional";
        }

        return "";
    }

    public static short cupHolderCountNullValue() {
        return (short) 255;
    }

    public static short cupHolderCountMinValue() {
        return (short) 0;
    }

    public static short cupHolderCountMaxValue() {
        return (short) 254;
    }

    public CarEncoder cupHolderCount(final short value) {
        buffer.putByte(offset + 61, (byte) value);
        return this;
    }


    private final FuelFiguresEncoder fuelFigures = new FuelFiguresEncoder(this);

    public static long fuelFiguresId() {
        return 10;
    }

    public FuelFiguresEncoder fuelFiguresCount(final int count) {
        fuelFigures.wrap(buffer, count);
        return fuelFigures;
    }

    public static final class FuelFiguresEncoder {
        public static final int HEADER_SIZE = 4;
        private final CarEncoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int initialLimit;

        FuelFiguresEncoder(final CarEncoder parentMessage) {
            this.parentMessage = parentMessage;
        }

        public void wrap(final MutableDirectBuffer buffer, final int count) {
            if (count < 0 || count > 65534) {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            if (buffer != this.buffer) {
                this.buffer = buffer;
            }

            index = 0;
            this.count = count;
            final int limit = parentMessage.limit();
            initialLimit = limit;
            parentMessage.limit(limit + HEADER_SIZE);
            buffer.putShort(limit + 0, (short) 6, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putShort(limit + 2, (short) count, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public FuelFiguresEncoder next() {
            if (index >= count) {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + sbeBlockLength());
            ++index;

            return this;
        }

        public int resetCountToIndex() {
            count = index;
            buffer.putShort(initialLimit + 2, (short) count, java.nio.ByteOrder.LITTLE_ENDIAN);

            return count;
        }

        public static int countMinValue() {
            return 0;
        }

        public static int countMaxValue() {
            return 65534;
        }

        public static int sbeHeaderSize() {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength() {
            return 6;
        }

        public static int speedId() {
            return 11;
        }

        public static int speedSinceVersion() {
            return 0;
        }

        public static int speedEncodingOffset() {
            return 0;
        }

        public static int speedEncodingLength() {
            return 2;
        }

        public static String speedMetaAttribute(final MetaAttribute metaAttribute) {
            if (MetaAttribute.PRESENCE == metaAttribute) {
                return "required";
            }

            return "";
        }

        public static int speedNullValue() {
            return 65535;
        }

        public static int speedMinValue() {
            return 0;
        }

        public static int speedMaxValue() {
            return 65534;
        }

        public FuelFiguresEncoder speed(final int value) {
            buffer.putShort(offset + 0, (short) value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int mpgId() {
            return 12;
        }

        public static int mpgSinceVersion() {
            return 0;
        }

        public static int mpgEncodingOffset() {
            return 2;
        }

        public static int mpgEncodingLength() {
            return 4;
        }

        public static String mpgMetaAttribute(final MetaAttribute metaAttribute) {
            if (MetaAttribute.PRESENCE == metaAttribute) {
                return "required";
            }

            return "";
        }

        public static float mpgNullValue() {
            return Float.NaN;
        }

        public static float mpgMinValue() {
            return 1.401298464324817E-45f;
        }

        public static float mpgMaxValue() {
            return 3.4028234663852886E38f;
        }

        public FuelFiguresEncoder mpg(final float value) {
            buffer.putFloat(offset + 2, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }


        public static int usageDescriptionId() {
            return 200;
        }

        public static String usageDescriptionCharacterEncoding() {
            return "ASCII";
        }

        public static String usageDescriptionMetaAttribute(final MetaAttribute metaAttribute) {
            if (MetaAttribute.PRESENCE == metaAttribute) {
                return "required";
            }

            return "";
        }

        public static int usageDescriptionHeaderLength() {
            return 4;
        }

        public FuelFiguresEncoder putUsageDescription(final DirectBuffer src, final int srcOffset, final int length) {
            if (length > 1073741824) {
                throw new IllegalStateException("length > maxValue for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public FuelFiguresEncoder putUsageDescription(final byte[] src, final int srcOffset, final int length) {
            if (length > 1073741824) {
                throw new IllegalStateException("length > maxValue for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public FuelFiguresEncoder usageDescription(final String value) {
            final int length = null == value ? 0 : value.length();
            if (length > 1073741824) {
                throw new IllegalStateException("length > maxValue for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putStringWithoutLengthAscii(limit + headerLength, value);

            return this;
        }

        public FuelFiguresEncoder usageDescription(final CharSequence value) {
            final int length = null == value ? 0 : value.length();
            if (length > 1073741824) {
                throw new IllegalStateException("length > maxValue for type: " + length);
            }

            final int headerLength = 4;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putStringWithoutLengthAscii(limit + headerLength, value);

            return this;
        }
    }

    private final PerformanceFiguresEncoder performanceFigures = new PerformanceFiguresEncoder(this);

    public static long performanceFiguresId() {
        return 13;
    }

    public PerformanceFiguresEncoder performanceFiguresCount(final int count) {
        performanceFigures.wrap(buffer, count);
        return performanceFigures;
    }

    public static final class PerformanceFiguresEncoder {
        public static final int HEADER_SIZE = 4;
        private final CarEncoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int initialLimit;
        private final AccelerationEncoder acceleration;

        PerformanceFiguresEncoder(final CarEncoder parentMessage) {
            this.parentMessage = parentMessage;
            acceleration = new AccelerationEncoder(parentMessage);
        }

        public void wrap(final MutableDirectBuffer buffer, final int count) {
            if (count < 0 || count > 65534) {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            if (buffer != this.buffer) {
                this.buffer = buffer;
            }

            index = 0;
            this.count = count;
            final int limit = parentMessage.limit();
            initialLimit = limit;
            parentMessage.limit(limit + HEADER_SIZE);
            buffer.putShort(limit + 0, (short) 1, java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.putShort(limit + 2, (short) count, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

        public PerformanceFiguresEncoder next() {
            if (index >= count) {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + sbeBlockLength());
            ++index;

            return this;
        }

        public int resetCountToIndex() {
            count = index;
            buffer.putShort(initialLimit + 2, (short) count, java.nio.ByteOrder.LITTLE_ENDIAN);

            return count;
        }

        public static int countMinValue() {
            return 0;
        }

        public static int countMaxValue() {
            return 65534;
        }

        public static int sbeHeaderSize() {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength() {
            return 1;
        }

        public static int octaneRatingId() {
            return 14;
        }

        public static int octaneRatingSinceVersion() {
            return 0;
        }

        public static int octaneRatingEncodingOffset() {
            return 0;
        }

        public static int octaneRatingEncodingLength() {
            return 1;
        }

        public static String octaneRatingMetaAttribute(final MetaAttribute metaAttribute) {
            if (MetaAttribute.PRESENCE == metaAttribute) {
                return "required";
            }

            return "";
        }

        public static short octaneRatingNullValue() {
            return (short) 255;
        }

        public static short octaneRatingMinValue() {
            return (short) 90;
        }

        public static short octaneRatingMaxValue() {
            return (short) 110;
        }

        public PerformanceFiguresEncoder octaneRating(final short value) {
            buffer.putByte(offset + 0, (byte) value);
            return this;
        }


        public static long accelerationId() {
            return 15;
        }

        public AccelerationEncoder accelerationCount(final int count) {
            acceleration.wrap(buffer, count);
            return acceleration;
        }

        public static final class AccelerationEncoder {
            public static final int HEADER_SIZE = 4;
            private final CarEncoder parentMessage;
            private MutableDirectBuffer buffer;
            private int count;
            private int index;
            private int offset;
            private int initialLimit;

            AccelerationEncoder(final CarEncoder parentMessage) {
                this.parentMessage = parentMessage;
            }

            public void wrap(final MutableDirectBuffer buffer, final int count) {
                if (count < 0 || count > 65534) {
                    throw new IllegalArgumentException("count outside allowed range: count=" + count);
                }

                if (buffer != this.buffer) {
                    this.buffer = buffer;
                }

                index = 0;
                this.count = count;
                final int limit = parentMessage.limit();
                initialLimit = limit;
                parentMessage.limit(limit + HEADER_SIZE);
                buffer.putShort(limit + 0, (short) 6, java.nio.ByteOrder.LITTLE_ENDIAN);
                buffer.putShort(limit + 2, (short) count, java.nio.ByteOrder.LITTLE_ENDIAN);
            }

            public AccelerationEncoder next() {
                if (index >= count) {
                    throw new java.util.NoSuchElementException();
                }

                offset = parentMessage.limit();
                parentMessage.limit(offset + sbeBlockLength());
                ++index;

                return this;
            }

            public int resetCountToIndex() {
                count = index;
                buffer.putShort(initialLimit + 2, (short) count, java.nio.ByteOrder.LITTLE_ENDIAN);

                return count;
            }

            public static int countMinValue() {
                return 0;
            }

            public static int countMaxValue() {
                return 65534;
            }

            public static int sbeHeaderSize() {
                return HEADER_SIZE;
            }

            public static int sbeBlockLength() {
                return 6;
            }

            public static int mphId() {
                return 16;
            }

            public static int mphSinceVersion() {
                return 0;
            }

            public static int mphEncodingOffset() {
                return 0;
            }

            public static int mphEncodingLength() {
                return 2;
            }

            public static String mphMetaAttribute(final MetaAttribute metaAttribute) {
                if (MetaAttribute.PRESENCE == metaAttribute) {
                    return "required";
                }

                return "";
            }

            public static int mphNullValue() {
                return 65535;
            }

            public static int mphMinValue() {
                return 0;
            }

            public static int mphMaxValue() {
                return 65534;
            }

            public AccelerationEncoder mph(final int value) {
                buffer.putShort(offset + 0, (short) value, java.nio.ByteOrder.LITTLE_ENDIAN);
                return this;
            }


            public static int secondsId() {
                return 17;
            }

            public static int secondsSinceVersion() {
                return 0;
            }

            public static int secondsEncodingOffset() {
                return 2;
            }

            public static int secondsEncodingLength() {
                return 4;
            }

            public static String secondsMetaAttribute(final MetaAttribute metaAttribute) {
                if (MetaAttribute.PRESENCE == metaAttribute) {
                    return "required";
                }

                return "";
            }

            public static float secondsNullValue() {
                return Float.NaN;
            }

            public static float secondsMinValue() {
                return 1.401298464324817E-45f;
            }

            public static float secondsMaxValue() {
                return 3.4028234663852886E38f;
            }

            public AccelerationEncoder seconds(final float value) {
                buffer.putFloat(offset + 2, value, java.nio.ByteOrder.LITTLE_ENDIAN);
                return this;
            }

        }
    }

    public static int manufacturerId() {
        return 18;
    }

    public static String manufacturerCharacterEncoding() {
        return "UTF-8";
    }

    public static String manufacturerMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public static int manufacturerHeaderLength() {
        return 4;
    }

    public CarEncoder putManufacturer(final DirectBuffer src, final int srcOffset, final int length) {
        if (length > 1073741824) {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putBytes(limit + headerLength, src, srcOffset, length);

        return this;
    }

    public CarEncoder putManufacturer(final byte[] src, final int srcOffset, final int length) {
        if (length > 1073741824) {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putBytes(limit + headerLength, src, srcOffset, length);

        return this;
    }

    public CarEncoder manufacturer(final String value) {
        final byte[] bytes;
        try {
            bytes = null == value || value.isEmpty() ? org.agrona.collections.ArrayUtil.EMPTY_BYTE_ARRAY : value.getBytes("UTF-8");
        } catch (final java.io.UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

        final int length = bytes.length;
        if (length > 1073741824) {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putBytes(limit + headerLength, bytes, 0, length);

        return this;
    }

    public static int modelId() {
        return 19;
    }

    public static String modelCharacterEncoding() {
        return "UTF-8";
    }

    public static String modelMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public static int modelHeaderLength() {
        return 4;
    }

    public CarEncoder putModel(final DirectBuffer src, final int srcOffset, final int length) {
        if (length > 1073741824) {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putBytes(limit + headerLength, src, srcOffset, length);

        return this;
    }

    public CarEncoder putModel(final byte[] src, final int srcOffset, final int length) {
        if (length > 1073741824) {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putBytes(limit + headerLength, src, srcOffset, length);

        return this;
    }

    public CarEncoder model(final String value) {
        final byte[] bytes;
        try {
            bytes = null == value || value.isEmpty() ? org.agrona.collections.ArrayUtil.EMPTY_BYTE_ARRAY : value.getBytes("UTF-8");
        } catch (final java.io.UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

        final int length = bytes.length;
        if (length > 1073741824) {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putBytes(limit + headerLength, bytes, 0, length);

        return this;
    }

    public static int activationCodeId() {
        return 20;
    }

    public static String activationCodeCharacterEncoding() {
        return "ASCII";
    }

    public static String activationCodeMetaAttribute(final MetaAttribute metaAttribute) {
        if (MetaAttribute.PRESENCE == metaAttribute) {
            return "required";
        }

        return "";
    }

    public static int activationCodeHeaderLength() {
        return 4;
    }

    public CarEncoder putActivationCode(final DirectBuffer src, final int srcOffset, final int length) {
        if (length > 1073741824) {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putBytes(limit + headerLength, src, srcOffset, length);

        return this;
    }

    public CarEncoder putActivationCode(final byte[] src, final int srcOffset, final int length) {
        if (length > 1073741824) {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putBytes(limit + headerLength, src, srcOffset, length);

        return this;
    }

    public CarEncoder activationCode(final String value) {
        final int length = null == value ? 0 : value.length();
        if (length > 1073741824) {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putStringWithoutLengthAscii(limit + headerLength, value);

        return this;
    }

    public CarEncoder activationCode(final CharSequence value) {
        final int length = null == value ? 0 : value.length();
        if (length > 1073741824) {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, java.nio.ByteOrder.LITTLE_ENDIAN);
        buffer.putStringWithoutLengthAscii(limit + headerLength, value);

        return this;
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

        final CarDecoder decoder = new CarDecoder();
        decoder.wrap(buffer, initialOffset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
