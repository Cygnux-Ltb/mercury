package io.mercury.common.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.character.Charsets;
import io.mercury.common.character.Separator;
import io.mercury.common.serialization.specific.ByteArraySerializer;
import io.mercury.common.sys.SysProperties;

/**
 * Use FileChannel
 * 
 * @author yellow013
 */
public final class FileChannelWriter {

	private FileChannelWriter() {
	}

	/**
	 * 
	 * @param data   : Written data
	 * @param target : Written target file
	 * @return
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public static final File write(List<String> data, @Nonnull final File target)
			throws NullPointerException, IOException {
		return write(data, Charsets.UTF8, target, 8192, true);
	}

	/**
	 * 
	 * @param data   : Written data
	 * @param target : Written target file
	 * @param append : Is append file end
	 * @return
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public static final File write(List<String> data, @Nonnull File target, boolean append)
			throws NullPointerException, IOException {
		return write(data, Charsets.UTF8, target, 8192, append);
	}

	/**
	 * 
	 * @param data     : Written data
	 * @param charset  : Value charset
	 * @param target   : Written target file
	 * @param capacity : Buffer capacity
	 * @param append   : Is append file end
	 * @return
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public static final File write(List<String> data, @Nonnull Charset charset, @Nonnull File target, int capacity,
			boolean append) throws NullPointerException, IOException {
		return write(data, str -> str.getBytes(charset), target, capacity, append);
	}

	/**
	 * 
	 * @param <T>
	 * @param data          : Written data
	 * @param serialization : Serialization function
	 * @param target        : Written target file
	 * @param capacity      : Buffer capacity
	 * @param append        : Is append file end
	 * @return
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public static final <T> File write(List<T> data, @Nonnull ByteArraySerializer<T> serializer, @Nonnull File target,
			int capacity, boolean append) throws NullPointerException, IOException {
		if (target == null)
			throw new NullPointerException("target file must not be null.");
		if (capacity <= 0)
			capacity = 4096;
		File parentFile = target.getParentFile();
		if (!parentFile.exists())
			parentFile.mkdirs();
		if (!target.exists())
			target.createNewFile();
		if (CollectionUtils.isNotEmpty(data)) {
			try (RandomAccessFile rafile = new RandomAccessFile(target, "rw")) {
				if (append) {
					// Seek to end
					rafile.seek(rafile.length());
				}
				try (FileChannel channel = rafile.getChannel()) {
					// Allocate [capacity] direct buffer
					ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);
					for (int i = 0; i < data.size(); i++) {
						byte[] bytes = serializer.serialization(data.get(i));
						if (bytes == null || bytes.length == 0) {
							continue;
						}
						if (bytes.length < capacity) {
							// [bytes#length] < [capacity], Only need to write once
							flipAndChannelWrite(buffer.put(bytes), channel);
						} else {
							// Count writes
							int count = bytes.length / capacity;
							int offset = 0;
							for (int r = 0; r < count; r++) {
								// Write from the last [offset], Write length is buffer [capacity]
								flipAndChannelWrite(buffer.put(bytes, offset = r * capacity, capacity), channel);
							}
							// Remaining data
							int remaining = bytes.length % capacity;
							if (remaining > 0) {
								// Write from the last [offset], Write length is remaining
								flipAndChannelWrite(buffer.put(bytes, offset += capacity, remaining), channel);
							}
						}
					}
					channel.force(true);
				}
			}
		}
		return target;
	}

	/**
	 * 
	 * @param buffer
	 * @param channel
	 * @throws IOException
	 */
	private static final void flipAndChannelWrite(ByteBuffer buffer, FileChannel channel) throws IOException {
		// Flip buffer to output
		buffer.flip();
		// Loop write
		while (buffer.hasRemaining()) {
			channel.write(buffer);
		}
		// Clear buffer to input
		buffer.clear();
	}

	public static void main(String[] args) {

		long nanoTime0 = System.nanoTime();

		List<String> lines = new ArrayList<String>();
		for (int i = 0; i < 1000000; i++) {
			StringBuilder builder = new StringBuilder();
			for (int j = 0; j < 100; j++) {
				builder.append(j);
				builder.append(',');
			}
			builder.append(Separator.LINE_SEPARATOR);
			lines.add(builder.toString());
		}

		try {
			write(lines, Charsets.UTF8, new File(SysProperties.JAVA_IO_TMPDIR_FILE, "test.csv"), 2048, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		long nanoTime1 = System.nanoTime();

		System.out.println((nanoTime1 - nanoTime0) / 1000);

	}

}
