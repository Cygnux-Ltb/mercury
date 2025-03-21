/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package io.mercury.serialization.avro.msg;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@org.apache.avro.specific.AvroGenerated
public class AvroTextMsg extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -6794268861616152353L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"AvroTextMsg\",\"namespace\":\"io.mercury.serialization.avro.msg\",\"fields\":[{\"name\":\"envelope\",\"type\":{\"type\":\"record\",\"name\":\"Envelope\",\"fields\":[{\"name\":\"code\",\"type\":\"int\"},{\"name\":\"version\",\"type\":\"int\"},{\"name\":\"contentType\",\"type\":{\"type\":\"enum\",\"name\":\"ContentType\",\"symbols\":[\"NULL\",\"BOOLEAN\",\"BYTE\",\"CHAR\",\"INT\",\"LONG\",\"DOUBLE\",\"STRING\",\"OBJECT\",\"LIST\",\"MAP\"]}}]}},{\"name\":\"sequence\",\"type\":\"long\"},{\"name\":\"epoch\",\"type\":\"long\"},{\"name\":\"content\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<AvroTextMsg> ENCODER =
      new BinaryMessageEncoder<AvroTextMsg>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<AvroTextMsg> DECODER =
      new BinaryMessageDecoder<AvroTextMsg>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<AvroTextMsg> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<AvroTextMsg> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<AvroTextMsg> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<AvroTextMsg>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this AvroTextMsg to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a AvroTextMsg from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a AvroTextMsg instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static AvroTextMsg fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private io.mercury.serialization.avro.msg.Envelope envelope;
  private long sequence;
  private long epoch;
  private java.lang.String content;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public AvroTextMsg() {}

  /**
   * All-args constructor.
   * @param envelope The new value for envelope
   * @param sequence The new value for sequence
   * @param epoch The new value for epoch
   * @param content The new value for content
   */
  public AvroTextMsg(io.mercury.serialization.avro.msg.Envelope envelope, java.lang.Long sequence, java.lang.Long epoch, java.lang.String content) {
    this.envelope = envelope;
    this.sequence = sequence;
    this.epoch = epoch;
    this.content = content;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return envelope;
    case 1: return sequence;
    case 2: return epoch;
    case 3: return content;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: envelope = (io.mercury.serialization.avro.msg.Envelope)value$; break;
    case 1: sequence = (java.lang.Long)value$; break;
    case 2: epoch = (java.lang.Long)value$; break;
    case 3: content = value$ != null ? value$.toString() : null; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'envelope' field.
   * @return The value of the 'envelope' field.
   */
  public io.mercury.serialization.avro.msg.Envelope getEnvelope() {
    return envelope;
  }


  /**
   * Sets the value of the 'envelope' field.
   * @param value the value to set.
   */
  public AvroTextMsg setEnvelope(io.mercury.serialization.avro.msg.Envelope value) {
    this.envelope = value;
    return this;
  }

  /**
   * Gets the value of the 'sequence' field.
   * @return The value of the 'sequence' field.
   */
  public long getSequence() {
    return sequence;
  }


  /**
   * Sets the value of the 'sequence' field.
   * @param value the value to set.
   */
  public AvroTextMsg setSequence(long value) {
    this.sequence = value;
    return this;
  }

  /**
   * Gets the value of the 'epoch' field.
   * @return The value of the 'epoch' field.
   */
  public long getEpoch() {
    return epoch;
  }


  /**
   * Sets the value of the 'epoch' field.
   * @param value the value to set.
   */
  public AvroTextMsg setEpoch(long value) {
    this.epoch = value;
    return this;
  }

  /**
   * Gets the value of the 'content' field.
   * @return The value of the 'content' field.
   */
  public java.lang.String getContent() {
    return content;
  }


  /**
   * Sets the value of the 'content' field.
   * @param value the value to set.
   */
  public AvroTextMsg setContent(java.lang.String value) {
    this.content = value;
    return this;
  }

  /**
   * Creates a new AvroTextMsg RecordBuilder.
   * @return A new AvroTextMsg RecordBuilder
   */
  public static io.mercury.serialization.avro.msg.AvroTextMsg.Builder newBuilder() {
    return new io.mercury.serialization.avro.msg.AvroTextMsg.Builder();
  }

  /**
   * Creates a new AvroTextMsg RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new AvroTextMsg RecordBuilder
   */
  public static io.mercury.serialization.avro.msg.AvroTextMsg.Builder newBuilder(io.mercury.serialization.avro.msg.AvroTextMsg.Builder other) {
    if (other == null) {
      return new io.mercury.serialization.avro.msg.AvroTextMsg.Builder();
    } else {
      return new io.mercury.serialization.avro.msg.AvroTextMsg.Builder(other);
    }
  }

  /**
   * Creates a new AvroTextMsg RecordBuilder by copying an existing AvroTextMsg instance.
   * @param other The existing instance to copy.
   * @return A new AvroTextMsg RecordBuilder
   */
  public static io.mercury.serialization.avro.msg.AvroTextMsg.Builder newBuilder(io.mercury.serialization.avro.msg.AvroTextMsg other) {
    if (other == null) {
      return new io.mercury.serialization.avro.msg.AvroTextMsg.Builder();
    } else {
      return new io.mercury.serialization.avro.msg.AvroTextMsg.Builder(other);
    }
  }

  /**
   * RecordBuilder for AvroTextMsg instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<AvroTextMsg>
    implements org.apache.avro.data.RecordBuilder<AvroTextMsg> {

    private io.mercury.serialization.avro.msg.Envelope envelope;
    private io.mercury.serialization.avro.msg.Envelope.Builder envelopeBuilder;
    private long sequence;
    private long epoch;
    private java.lang.String content;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(io.mercury.serialization.avro.msg.AvroTextMsg.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.envelope)) {
        this.envelope = data().deepCopy(fields()[0].schema(), other.envelope);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (other.hasEnvelopeBuilder()) {
        this.envelopeBuilder = io.mercury.serialization.avro.msg.Envelope.newBuilder(other.getEnvelopeBuilder());
      }
      if (isValidValue(fields()[1], other.sequence)) {
        this.sequence = data().deepCopy(fields()[1].schema(), other.sequence);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.epoch)) {
        this.epoch = data().deepCopy(fields()[2].schema(), other.epoch);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.content)) {
        this.content = data().deepCopy(fields()[3].schema(), other.content);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing AvroTextMsg instance
     * @param other The existing instance to copy.
     */
    private Builder(io.mercury.serialization.avro.msg.AvroTextMsg other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.envelope)) {
        this.envelope = data().deepCopy(fields()[0].schema(), other.envelope);
        fieldSetFlags()[0] = true;
      }
      this.envelopeBuilder = null;
      if (isValidValue(fields()[1], other.sequence)) {
        this.sequence = data().deepCopy(fields()[1].schema(), other.sequence);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.epoch)) {
        this.epoch = data().deepCopy(fields()[2].schema(), other.epoch);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.content)) {
        this.content = data().deepCopy(fields()[3].schema(), other.content);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'envelope' field.
      * @return The value.
      */
    public io.mercury.serialization.avro.msg.Envelope getEnvelope() {
      return envelope;
    }


    /**
      * Sets the value of the 'envelope' field.
      * @param value The value of 'envelope'.
      * @return This builder.
      */
    public io.mercury.serialization.avro.msg.AvroTextMsg.Builder setEnvelope(io.mercury.serialization.avro.msg.Envelope value) {
      validate(fields()[0], value);
      this.envelopeBuilder = null;
      this.envelope = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'envelope' field has been set.
      * @return True if the 'envelope' field has been set, false otherwise.
      */
    public boolean hasEnvelope() {
      return fieldSetFlags()[0];
    }

    /**
     * Gets the Builder instance for the 'envelope' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public io.mercury.serialization.avro.msg.Envelope.Builder getEnvelopeBuilder() {
      if (envelopeBuilder == null) {
        if (hasEnvelope()) {
          setEnvelopeBuilder(io.mercury.serialization.avro.msg.Envelope.newBuilder(envelope));
        } else {
          setEnvelopeBuilder(io.mercury.serialization.avro.msg.Envelope.newBuilder());
        }
      }
      return envelopeBuilder;
    }

    /**
     * Sets the Builder instance for the 'envelope' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */

    public io.mercury.serialization.avro.msg.AvroTextMsg.Builder setEnvelopeBuilder(io.mercury.serialization.avro.msg.Envelope.Builder value) {
      clearEnvelope();
      envelopeBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'envelope' field has an active Builder instance
     * @return True if the 'envelope' field has an active Builder instance
     */
    public boolean hasEnvelopeBuilder() {
      return envelopeBuilder != null;
    }

    /**
      * Clears the value of the 'envelope' field.
      * @return This builder.
      */
    public io.mercury.serialization.avro.msg.AvroTextMsg.Builder clearEnvelope() {
      envelope = null;
      envelopeBuilder = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'sequence' field.
      * @return The value.
      */
    public long getSequence() {
      return sequence;
    }


    /**
      * Sets the value of the 'sequence' field.
      * @param value The value of 'sequence'.
      * @return This builder.
      */
    public io.mercury.serialization.avro.msg.AvroTextMsg.Builder setSequence(long value) {
      validate(fields()[1], value);
      this.sequence = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'sequence' field has been set.
      * @return True if the 'sequence' field has been set, false otherwise.
      */
    public boolean hasSequence() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'sequence' field.
      * @return This builder.
      */
    public io.mercury.serialization.avro.msg.AvroTextMsg.Builder clearSequence() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'epoch' field.
      * @return The value.
      */
    public long getEpoch() {
      return epoch;
    }


    /**
      * Sets the value of the 'epoch' field.
      * @param value The value of 'epoch'.
      * @return This builder.
      */
    public io.mercury.serialization.avro.msg.AvroTextMsg.Builder setEpoch(long value) {
      validate(fields()[2], value);
      this.epoch = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'epoch' field has been set.
      * @return True if the 'epoch' field has been set, false otherwise.
      */
    public boolean hasEpoch() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'epoch' field.
      * @return This builder.
      */
    public io.mercury.serialization.avro.msg.AvroTextMsg.Builder clearEpoch() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'content' field.
      * @return The value.
      */
    public java.lang.String getContent() {
      return content;
    }


    /**
      * Sets the value of the 'content' field.
      * @param value The value of 'content'.
      * @return This builder.
      */
    public io.mercury.serialization.avro.msg.AvroTextMsg.Builder setContent(java.lang.String value) {
      validate(fields()[3], value);
      this.content = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'content' field has been set.
      * @return True if the 'content' field has been set, false otherwise.
      */
    public boolean hasContent() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'content' field.
      * @return This builder.
      */
    public io.mercury.serialization.avro.msg.AvroTextMsg.Builder clearContent() {
      content = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    public AvroTextMsg build() {
      try {
        AvroTextMsg record = new AvroTextMsg();
        if (envelopeBuilder != null) {
          try {
            record.envelope = this.envelopeBuilder.build();
          } catch (org.apache.avro.AvroMissingFieldException e) {
            e.addParentField(record.getSchema().getField("envelope"));
            throw e;
          }
        } else {
          record.envelope = fieldSetFlags()[0] ? this.envelope : (io.mercury.serialization.avro.msg.Envelope) defaultValue(fields()[0]);
        }
        record.sequence = fieldSetFlags()[1] ? this.sequence : (java.lang.Long) defaultValue(fields()[1]);
        record.epoch = fieldSetFlags()[2] ? this.epoch : (java.lang.Long) defaultValue(fields()[2]);
        record.content = fieldSetFlags()[3] ? this.content : (java.lang.String) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<AvroTextMsg>
    WRITER$ = (org.apache.avro.io.DatumWriter<AvroTextMsg>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<AvroTextMsg>
    READER$ = (org.apache.avro.io.DatumReader<AvroTextMsg>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    this.envelope.customEncode(out);

    out.writeLong(this.sequence);

    out.writeLong(this.epoch);

    out.writeString(this.content);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      if (this.envelope == null) {
        this.envelope = new io.mercury.serialization.avro.msg.Envelope();
      }
      this.envelope.customDecode(in);

      this.sequence = in.readLong();

      this.epoch = in.readLong();

      this.content = in.readString();

    } else {
      for (int i = 0; i < 4; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          if (this.envelope == null) {
            this.envelope = new io.mercury.serialization.avro.msg.Envelope();
          }
          this.envelope.customDecode(in);
          break;

        case 1:
          this.sequence = in.readLong();
          break;

        case 2:
          this.epoch = in.readLong();
          break;

        case 3:
          this.content = in.readString();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










