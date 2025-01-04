package org.zeromq.guide.util;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 
 * A simple getKey value message class
 * 
 * @author Danish Shrestha &lt;dshrestha06@gmail.com&gt;
 *
 */
public class KvSimple {
	
	private final String key;
	private long sequence;
	private final byte[] body;

	public KvSimple(String key, long sequence, byte[] body) {
		this.key = key;
		this.sequence = sequence;
		this.body = body; // clone if needed
	}

	public String getKey() {
		return key;
	}

	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	public byte[] getBody() {
		return body;
	}

	public void send(Socket publisher) {

		publisher.send(key.getBytes(ZMQ.CHARSET), ZMQ.SNDMORE);

		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.asLongBuffer().put(sequence);
		publisher.send(bb.array(), ZMQ.SNDMORE);

		publisher.send(body, 0);
	}

	public static KvSimple recv(Socket updates) {
		byte[] data = updates.recv(0);
		if (data == null || !updates.hasReceiveMore())
			return null;
		String key = new String(data, ZMQ.CHARSET);
		data = updates.recv(0);
		if (data == null || !updates.hasReceiveMore())
			return null;
		long sequence = ByteBuffer.wrap(data).getLong();
		byte[] body = updates.recv(0);
		if (body == null || updates.hasReceiveMore())
			return null;

		return new KvSimple(key, sequence, body);
	}

	@Override
	public String toString() {
		return "kvsimple [getKey=" + key + ", getSequence=" + sequence + ", body=" + Arrays.toString(body) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(body);
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + (int) (sequence ^ (sequence >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KvSimple other = (KvSimple) obj;
		if (!Arrays.equals(body, other.body))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return sequence == other.sequence;
	}

}
