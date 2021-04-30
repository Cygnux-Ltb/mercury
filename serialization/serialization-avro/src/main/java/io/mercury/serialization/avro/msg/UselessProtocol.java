/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package io.mercury.serialization.avro.msg;

@org.apache.avro.specific.AvroGenerated
public interface UselessProtocol {
	public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse(
			"{\"protocol\":\"UselessProtocol\",\"namespace\":\"io.mercury.serialization.avro.msg\",\"types\":[{\"type\":\"enum\",\"name\":\"ContentType\",\"symbols\":[\"OBJECT\",\"LIST\",\"STRING\",\"INT\",\"LONG\",\"DOUBLE\"]},{\"type\":\"record\",\"name\":\"Envelope\",\"fields\":[{\"name\":\"code\",\"type\":\"int\"},{\"name\":\"version\",\"type\":\"int\"},{\"name\":\"contentType\",\"type\":\"ContentType\"}]},{\"type\":\"record\",\"name\":\"AvroBinaryMsg\",\"fields\":[{\"name\":\"sequence\",\"type\":\"long\"},{\"name\":\"epoch\",\"type\":\"long\"},{\"name\":\"envelope\",\"type\":\"Envelope\"},{\"name\":\"content\",\"type\":\"bytes\"}]},{\"type\":\"record\",\"name\":\"AvroTextMsg\",\"fields\":[{\"name\":\"sequence\",\"type\":\"long\"},{\"name\":\"epoch\",\"type\":\"long\"},{\"name\":\"envelope\",\"type\":\"Envelope\"},{\"name\":\"content\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}],\"messages\":{}}");

	@org.apache.avro.specific.AvroGenerated
	public interface Callback extends UselessProtocol {
		public static final org.apache.avro.Protocol PROTOCOL = io.mercury.serialization.avro.msg.UselessProtocol.PROTOCOL;
	}
	
}