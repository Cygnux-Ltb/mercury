/* Generated SBE (Simple Binary Encoding) message codec. */
package baseline;

import org.agrona.MutableDirectBuffer;


/**
 * Common Message
 */
@SuppressWarnings("all")
public final class CommonEncoder {
    public static final int BLOCK_LENGTH = 0;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final CommonEncoder parentMessage = this;
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

    public CommonEncoder wrap(final MutableDirectBuffer buffer, final int offset) {
        if (buffer != this.buffer) {
            this.buffer = buffer;
        }
        this.initialOffset = offset;
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public CommonEncoder wrapAndApplyHeader(final MutableDirectBuffer buffer, final int offset,
                                            final MessageHeaderEncoder headerEncoder) {
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

        final CommonDecoder decoder = new CommonDecoder();
        decoder.wrap(buffer, initialOffset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
