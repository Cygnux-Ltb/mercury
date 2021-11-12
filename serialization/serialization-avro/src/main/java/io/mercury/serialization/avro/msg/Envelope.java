/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package io.mercury.serialization.avro.msg;

import java.util.Optional;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@org.apache.avro.specific.AvroGenerated
public class Envelope extends org.apache.avro.specific.SpecificRecordBase
		implements org.apache.avro.specific.SpecificRecord {
	
	private static final long serialVersionUID = -437770354948209888L;

	public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse(
			"{\"type\":\"record\",\"name\":\"Envelope\",\"namespace\":\"io.mercury.serialization.avro.msg\",\"fields\":[{\"name\":\"code\",\"type\":\"int\"},{\"name\":\"version\",\"type\":\"int\"},{\"name\":\"contentType\",\"type\":{\"type\":\"enum\",\"name\":\"ContentType\",\"symbols\":[\"INT\",\"LONG\",\"DOUBLE\",\"STRING\",\"OBJECT\",\"LIST\",\"MAP\"]}}]}");

	public static org.apache.avro.Schema getClassSchema() {
		return SCHEMA$;
	}

	private static final SpecificData MODEL$ = new SpecificData();

	private static final BinaryMessageEncoder<Envelope> ENCODER = new BinaryMessageEncoder<Envelope>(MODEL$, SCHEMA$);

	private static final BinaryMessageDecoder<Envelope> DECODER = new BinaryMessageDecoder<Envelope>(MODEL$, SCHEMA$);

	/**
	 * Return the BinaryMessageEncoder instance used by this class.
	 * 
	 * @return the message encoder used by this class
	 */
	public static BinaryMessageEncoder<Envelope> getEncoder() {
		return ENCODER;
	}

	/**
	 * Return the BinaryMessageDecoder instance used by this class.
	 * 
	 * @return the message decoder used by this class
	 */
	public static BinaryMessageDecoder<Envelope> getDecoder() {
		return DECODER;
	}

	/**
	 * Create a new BinaryMessageDecoder instance for this class that uses the
	 * specified {@link SchemaStore}.
	 * 
	 * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
	 * @return a BinaryMessageDecoder instance for this class backed by the given
	 *         SchemaStore
	 */
	public static BinaryMessageDecoder<Envelope> createDecoder(SchemaStore resolver) {
		return new BinaryMessageDecoder<Envelope>(MODEL$, SCHEMA$, resolver);
	}

	/**
	 * Serializes this Envelope to a ByteBuffer.
	 * 
	 * @return a buffer holding the serialized data for this instance
	 * @throws java.io.IOException if this instance could not be serialized
	 */
	public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
		return ENCODER.encode(this);
	}

	/**
	 * Deserializes a Envelope from a ByteBuffer.
	 * 
	 * @param b a byte buffer holding serialized data for an instance of this class
	 * @return a Envelope instance decoded from the given buffer
	 * @throws java.io.IOException if the given bytes could not be deserialized into
	 *                             an instance of this class
	 */
	public static Envelope fromByteBuffer(java.nio.ByteBuffer b) throws java.io.IOException {
		return DECODER.decode(b);
	}

	private int code;
	private int version;
	private io.mercury.serialization.avro.msg.ContentType contentType;

	/**
	 * Default constructor. Note that this does not initialize fields to their
	 * default values from the schema. If that is desired then one should use
	 * <code>newBuilder()</code>.
	 */
	public Envelope() {
	}

	/**
	 * All-args constructor.
	 * 
	 * @param code        The new value for code
	 * @param version     The new value for version
	 * @param contentType The new value for contentType
	 */
	public Envelope(java.lang.Integer code, java.lang.Integer version,
			io.mercury.serialization.avro.msg.ContentType contentType) {
		this.code = code;
		this.version = version;
		this.contentType = contentType;
	}

	public org.apache.avro.specific.SpecificData getSpecificData() {
		return MODEL$;
	}

	public org.apache.avro.Schema getSchema() {
		return SCHEMA$;
	}

	// Used by DatumWriter. Applications should not call.
	public java.lang.Object get(int field$) {
		switch (field$) {
		case 0:
			return code;
		case 1:
			return version;
		case 2:
			return contentType;
		default:
			throw new IndexOutOfBoundsException("Invalid index: " + field$);
		}
	}

	// Used by DatumReader. Applications should not call.
	public void put(int field$, java.lang.Object value$) {
		switch (field$) {
		case 0:
			code = (java.lang.Integer) value$;
			break;
		case 1:
			version = (java.lang.Integer) value$;
			break;
		case 2:
			contentType = (io.mercury.serialization.avro.msg.ContentType) value$;
			break;
		default:
			throw new IndexOutOfBoundsException("Invalid index: " + field$);
		}
	}

	/**
	 * Gets the value of the 'code' field.
	 * 
	 * @return The value of the 'code' field.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Gets the value of the 'code' field as an Optional&lt;java.lang.Integer&gt;.
	 * 
	 * @return The value wrapped in an Optional&lt;java.lang.Integer&gt;.
	 */
	public Optional<java.lang.Integer> getOptionalCode() {
		return Optional.<java.lang.Integer>ofNullable(code);
	}

	/**
	 * Sets the value of the 'code' field.
	 * 
	 * @param value the value to set.
	 */
	public void setCode(int value) {
		this.code = value;
	}

	/**
	 * Gets the value of the 'version' field.
	 * 
	 * @return The value of the 'version' field.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Gets the value of the 'version' field as an
	 * Optional&lt;java.lang.Integer&gt;.
	 * 
	 * @return The value wrapped in an Optional&lt;java.lang.Integer&gt;.
	 */
	public Optional<java.lang.Integer> getOptionalVersion() {
		return Optional.<java.lang.Integer>ofNullable(version);
	}

	/**
	 * Sets the value of the 'version' field.
	 * 
	 * @param value the value to set.
	 */
	public void setVersion(int value) {
		this.version = value;
	}

	/**
	 * Gets the value of the 'contentType' field.
	 * 
	 * @return The value of the 'contentType' field.
	 */
	public io.mercury.serialization.avro.msg.ContentType getContentType() {
		return contentType;
	}

	/**
	 * Gets the value of the 'contentType' field as an
	 * Optional&lt;io.mercury.serialization.avro.msg.ContentType&gt;.
	 * 
	 * @return The value wrapped in an
	 *         Optional&lt;io.mercury.serialization.avro.msg.ContentType&gt;.
	 */
	public Optional<io.mercury.serialization.avro.msg.ContentType> getOptionalContentType() {
		return Optional.<io.mercury.serialization.avro.msg.ContentType>ofNullable(contentType);
	}

	/**
	 * Sets the value of the 'contentType' field.
	 * 
	 * @param value the value to set.
	 */
	public void setContentType(io.mercury.serialization.avro.msg.ContentType value) {
		this.contentType = value;
	}

	/**
	 * Creates a new Envelope RecordBuilder.
	 * 
	 * @return A new Envelope RecordBuilder
	 */
	public static io.mercury.serialization.avro.msg.Envelope.Builder newBuilder() {
		return new io.mercury.serialization.avro.msg.Envelope.Builder();
	}

	/**
	 * Creates a new Envelope RecordBuilder by copying an existing Builder.
	 * 
	 * @param other The existing builder to copy.
	 * @return A new Envelope RecordBuilder
	 */
	public static io.mercury.serialization.avro.msg.Envelope.Builder newBuilder(
			io.mercury.serialization.avro.msg.Envelope.Builder other) {
		if (other == null) {
			return new io.mercury.serialization.avro.msg.Envelope.Builder();
		} else {
			return new io.mercury.serialization.avro.msg.Envelope.Builder(other);
		}
	}

	/**
	 * Creates a new Envelope RecordBuilder by copying an existing Envelope
	 * instance.
	 * 
	 * @param other The existing instance to copy.
	 * @return A new Envelope RecordBuilder
	 */
	public static io.mercury.serialization.avro.msg.Envelope.Builder newBuilder(
			io.mercury.serialization.avro.msg.Envelope other) {
		if (other == null) {
			return new io.mercury.serialization.avro.msg.Envelope.Builder();
		} else {
			return new io.mercury.serialization.avro.msg.Envelope.Builder(other);
		}
	}

	/**
	 * RecordBuilder for Envelope instances.
	 */
	@org.apache.avro.specific.AvroGenerated
	public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Envelope>
			implements org.apache.avro.data.RecordBuilder<Envelope> {

		private int code;
		private int version;
		private io.mercury.serialization.avro.msg.ContentType contentType;

		/** Creates a new Builder */
		private Builder() {
			super(SCHEMA$, MODEL$);
		}

		/**
		 * Creates a Builder by copying an existing Builder.
		 * 
		 * @param other The existing Builder to copy.
		 */
		private Builder(io.mercury.serialization.avro.msg.Envelope.Builder other) {
			super(other);
			if (isValidValue(fields()[0], other.code)) {
				this.code = data().deepCopy(fields()[0].schema(), other.code);
				fieldSetFlags()[0] = other.fieldSetFlags()[0];
			}
			if (isValidValue(fields()[1], other.version)) {
				this.version = data().deepCopy(fields()[1].schema(), other.version);
				fieldSetFlags()[1] = other.fieldSetFlags()[1];
			}
			if (isValidValue(fields()[2], other.contentType)) {
				this.contentType = data().deepCopy(fields()[2].schema(), other.contentType);
				fieldSetFlags()[2] = other.fieldSetFlags()[2];
			}
		}

		/**
		 * Creates a Builder by copying an existing Envelope instance
		 * 
		 * @param other The existing instance to copy.
		 */
		private Builder(io.mercury.serialization.avro.msg.Envelope other) {
			super(SCHEMA$, MODEL$);
			if (isValidValue(fields()[0], other.code)) {
				this.code = data().deepCopy(fields()[0].schema(), other.code);
				fieldSetFlags()[0] = true;
			}
			if (isValidValue(fields()[1], other.version)) {
				this.version = data().deepCopy(fields()[1].schema(), other.version);
				fieldSetFlags()[1] = true;
			}
			if (isValidValue(fields()[2], other.contentType)) {
				this.contentType = data().deepCopy(fields()[2].schema(), other.contentType);
				fieldSetFlags()[2] = true;
			}
		}

		/**
		 * Gets the value of the 'code' field.
		 * 
		 * @return The value.
		 */
		public int getCode() {
			return code;
		}

		/**
		 * Gets the value of the 'code' field as an Optional&lt;java.lang.Integer&gt;.
		 * 
		 * @return The value wrapped in an Optional&lt;java.lang.Integer&gt;.
		 */
		public Optional<java.lang.Integer> getOptionalCode() {
			return Optional.<java.lang.Integer>ofNullable(code);
		}

		/**
		 * Sets the value of the 'code' field.
		 * 
		 * @param value The value of 'code'.
		 * @return This builder.
		 */
		public io.mercury.serialization.avro.msg.Envelope.Builder setCode(int value) {
			validate(fields()[0], value);
			this.code = value;
			fieldSetFlags()[0] = true;
			return this;
		}

		/**
		 * Checks whether the 'code' field has been set.
		 * 
		 * @return True if the 'code' field has been set, false otherwise.
		 */
		public boolean hasCode() {
			return fieldSetFlags()[0];
		}

		/**
		 * Clears the value of the 'code' field.
		 * 
		 * @return This builder.
		 */
		public io.mercury.serialization.avro.msg.Envelope.Builder clearCode() {
			fieldSetFlags()[0] = false;
			return this;
		}

		/**
		 * Gets the value of the 'version' field.
		 * 
		 * @return The value.
		 */
		public int getVersion() {
			return version;
		}

		/**
		 * Gets the value of the 'version' field as an
		 * Optional&lt;java.lang.Integer&gt;.
		 * 
		 * @return The value wrapped in an Optional&lt;java.lang.Integer&gt;.
		 */
		public Optional<java.lang.Integer> getOptionalVersion() {
			return Optional.<java.lang.Integer>ofNullable(version);
		}

		/**
		 * Sets the value of the 'version' field.
		 * 
		 * @param value The value of 'version'.
		 * @return This builder.
		 */
		public io.mercury.serialization.avro.msg.Envelope.Builder setVersion(int value) {
			validate(fields()[1], value);
			this.version = value;
			fieldSetFlags()[1] = true;
			return this;
		}

		/**
		 * Checks whether the 'version' field has been set.
		 * 
		 * @return True if the 'version' field has been set, false otherwise.
		 */
		public boolean hasVersion() {
			return fieldSetFlags()[1];
		}

		/**
		 * Clears the value of the 'version' field.
		 * 
		 * @return This builder.
		 */
		public io.mercury.serialization.avro.msg.Envelope.Builder clearVersion() {
			fieldSetFlags()[1] = false;
			return this;
		}

		/**
		 * Gets the value of the 'contentType' field.
		 * 
		 * @return The value.
		 */
		public io.mercury.serialization.avro.msg.ContentType getContentType() {
			return contentType;
		}

		/**
		 * Gets the value of the 'contentType' field as an
		 * Optional&lt;io.mercury.serialization.avro.msg.ContentType&gt;.
		 * 
		 * @return The value wrapped in an
		 *         Optional&lt;io.mercury.serialization.avro.msg.ContentType&gt;.
		 */
		public Optional<io.mercury.serialization.avro.msg.ContentType> getOptionalContentType() {
			return Optional.<io.mercury.serialization.avro.msg.ContentType>ofNullable(contentType);
		}

		/**
		 * Sets the value of the 'contentType' field.
		 * 
		 * @param value The value of 'contentType'.
		 * @return This builder.
		 */
		public io.mercury.serialization.avro.msg.Envelope.Builder setContentType(
				io.mercury.serialization.avro.msg.ContentType value) {
			validate(fields()[2], value);
			this.contentType = value;
			fieldSetFlags()[2] = true;
			return this;
		}

		/**
		 * Checks whether the 'contentType' field has been set.
		 * 
		 * @return True if the 'contentType' field has been set, false otherwise.
		 */
		public boolean hasContentType() {
			return fieldSetFlags()[2];
		}

		/**
		 * Clears the value of the 'contentType' field.
		 * 
		 * @return This builder.
		 */
		public io.mercury.serialization.avro.msg.Envelope.Builder clearContentType() {
			contentType = null;
			fieldSetFlags()[2] = false;
			return this;
		}

		@Override
		public Envelope build() {
			try {
				Envelope record = new Envelope();
				record.code = fieldSetFlags()[0] ? this.code : (java.lang.Integer) defaultValue(fields()[0]);
				record.version = fieldSetFlags()[1] ? this.version : (java.lang.Integer) defaultValue(fields()[1]);
				record.contentType = fieldSetFlags()[2] ? this.contentType
						: (io.mercury.serialization.avro.msg.ContentType) defaultValue(fields()[2]);
				return record;
			} catch (org.apache.avro.AvroMissingFieldException e) {
				throw e;
			} catch (java.lang.Exception e) {
				throw new org.apache.avro.AvroRuntimeException(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static final org.apache.avro.io.DatumWriter<Envelope> WRITER$ = (org.apache.avro.io.DatumWriter<Envelope>) MODEL$
			.createDatumWriter(SCHEMA$);

	@Override
	public void writeExternal(java.io.ObjectOutput out) throws java.io.IOException {
		WRITER$.write(this, SpecificData.getEncoder(out));
	}

	@SuppressWarnings("unchecked")
	private static final org.apache.avro.io.DatumReader<Envelope> READER$ = (org.apache.avro.io.DatumReader<Envelope>) MODEL$
			.createDatumReader(SCHEMA$);

	@Override
	public void readExternal(java.io.ObjectInput in) throws java.io.IOException {
		READER$.read(this, SpecificData.getDecoder(in));
	}

	@Override
	protected boolean hasCustomCoders() {
		return true;
	}

	@Override
	public void customEncode(org.apache.avro.io.Encoder out) throws java.io.IOException {
		out.writeInt(this.code);

		out.writeInt(this.version);

		out.writeEnum(this.contentType.ordinal());

	}

	@Override
	public void customDecode(org.apache.avro.io.ResolvingDecoder in) throws java.io.IOException {
		org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
		if (fieldOrder == null) {
			this.code = in.readInt();

			this.version = in.readInt();

			this.contentType = io.mercury.serialization.avro.msg.ContentType.values()[in.readEnum()];

		} else {
			for (int i = 0; i < 3; i++) {
				switch (fieldOrder[i].pos()) {
				case 0:
					this.code = in.readInt();
					break;

				case 1:
					this.version = in.readInt();
					break;

				case 2:
					this.contentType = io.mercury.serialization.avro.msg.ContentType.values()[in.readEnum()];
					break;

				default:
					throw new java.io.IOException("Corrupt ResolvingDecoder.");
				}
			}
		}
	}
}
