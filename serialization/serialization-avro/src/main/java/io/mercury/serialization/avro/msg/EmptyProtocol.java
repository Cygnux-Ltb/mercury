/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package io.mercury.serialization.avro.msg;

@org.apache.avro.specific.AvroGenerated
public interface EmptyProtocol {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"EmptyProtocol\",\"namespace\":\"io.mercury.serialization.avro.msg\",\"types\":[{\"type\":\"enum\",\"name\":\"ContentType\",\"symbols\":[\"NULL\",\"BOOLEAN\",\"BYTE\",\"CHAR\",\"INT\",\"LONG\",\"DOUBLE\",\"STRING\",\"OBJECT\",\"LIST\",\"MAP\"]},{\"type\":\"record\",\"name\":\"Envelope\",\"fields\":[{\"name\":\"code\",\"type\":\"int\"},{\"name\":\"version\",\"type\":\"int\"},{\"name\":\"contentType\",\"type\":\"ContentType\"}]},{\"type\":\"record\",\"name\":\"AvroBinaryMsg\",\"fields\":[{\"name\":\"envelope\",\"type\":\"Envelope\"},{\"name\":\"sequence\",\"type\":\"long\"},{\"name\":\"epoch\",\"type\":\"long\"},{\"name\":\"content\",\"type\":\"bytes\"}]},{\"type\":\"record\",\"name\":\"AvroTextMsg\",\"fields\":[{\"name\":\"envelope\",\"type\":\"Envelope\"},{\"name\":\"sequence\",\"type\":\"long\"},{\"name\":\"epoch\",\"type\":\"long\"},{\"name\":\"content\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}],\"messages\":{}}");

  @org.apache.avro.specific.AvroGenerated
  public interface Callback extends EmptyProtocol {
    public static final org.apache.avro.Protocol PROTOCOL = io.mercury.serialization.avro.msg.EmptyProtocol.PROTOCOL;
  }
}