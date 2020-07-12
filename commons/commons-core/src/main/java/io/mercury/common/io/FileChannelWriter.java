package io.mercury.common.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.character.Separator;
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
	 * @throws IOException
	 */
	public static final File write(List<String> data, @Nonnull final File target) throws IOException {
		return write(data, target, 8192);
	}

	/**
	 * 
	 * @param data     : Written data
	 * @param target   : Written target file
	 * @param capacity : Buffer capacity
	 * @return
	 * @throws IOException
	 */
	public static final File write(List<String> data, @Nonnull final File target, final int capacity)
			throws NullPointerException, IOException {
		if (target == null)
			throw new NullPointerException("target file must not be null.");
		File parentFile = target.getParentFile();
		if (!parentFile.exists())
			parentFile.mkdirs();
		if (!target.exists())
			target.createNewFile();
		if (CollectionUtils.isNotEmpty(data)) {
			try (RandomAccessFile rafile = new RandomAccessFile(target, "rw");
					FileChannel channel = rafile.getChannel()) {
				// Allocate [capacity] direct buffer
				ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);
				for (int i = 0; i < data.size(); i++) {
					byte[] bytes = data.get(i).getBytes();
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
		return target;
	}

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

		List<String> lines = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			StringBuilder builder = new StringBuilder();
			for (int j = 0; j < 1000; j++) {
				builder.append(j);
				builder.append(',');
			}
			builder.append(Separator.LINE_SEPARATOR);
			lines.add(builder.toString());
		}

		try {
			write(lines, new File(SysProperties.JAVA_IO_TMPDIR_FILE, "test.csv"), 2048);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
