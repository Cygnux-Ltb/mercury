package io.mercury.common.file;

import static io.mercury.common.character.Charsets.UTF8;
import static io.mercury.common.character.Separator.LINE_SEPARATOR;
import static io.mercury.common.sys.SysProperties.JAVA_IO_TMPDIR_FILE;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.serialization.BytesSerializer;

/**
 * Use FileChannel
 *
 * @author yellow013
 */
public final class FileChannelWriter {

    private FileChannelWriter() {
    }

    /**
     * @param lines  : written data
     * @param target : written target file
     * @return
     * @throws NullPointerException
     * @throws IOException
     */
    public static File write(Collection<String> lines, @Nonnull final File target)
            throws NullPointerException, IOException {
        return write(lines, UTF8, target, 8192, true);
    }

    /**
     * @param lines  : written data
     * @param target : written target file
     * @param append : append file end
     * @return
     * @throws NullPointerException
     * @throws IOException
     */
    public static File write(Collection<String> lines, @Nonnull File target, boolean append)
            throws NullPointerException, IOException {
        return write(lines, UTF8, target, 8192, append);
    }

    /**
     * @param lines    : Written data
     * @param charset  : Value charset
     * @param target   : Written target file
     * @param capacity : Buffer capacity
     * @param append   : Is append to file end
     * @return
     * @throws NullPointerException
     * @throws IOException
     */
    public static File write(Collection<String> lines, @Nonnull Charset charset, @Nonnull File target,
                             int capacity, boolean append) throws NullPointerException, IOException {
        return write(lines,
                line -> line.endsWith(LINE_SEPARATOR) ? line.getBytes(charset)
                        : new StringBuilder(line.length() + LINE_SEPARATOR.length()).append(line).append(LINE_SEPARATOR)
                        .toString().getBytes(charset),
                target, capacity, append);
    }

    /**
     * @param <T>
     * @param data       : Written data
     * @param serializer : Serialization function
     * @param target     : Written target file
     * @param capacity   : Buffer capacity
     * @param append     : Is append to file end
     * @return
     * @throws NullPointerException
     * @throws IOException
     */
    public static <T> File write(Collection<T> data, @Nonnull BytesSerializer<T> serializer, @Nonnull File target,
                                       int capacity, boolean append) throws NullPointerException, IOException {
        if (target == null)
            throw new NullPointerException("target file must not be null.");
        if (capacity < 128)
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
                    // for (int i = 0; i < data.size(); i++) {
                    for (T t : data) {
                        byte[] bytes = serializer.serialization(t);
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
     * @param buffer
     * @param channel
     * @throws IOException
     */
    private static void flipAndChannelWrite(ByteBuffer buffer, FileChannel channel) throws IOException {
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

        List<String> lines = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < 100; j++) {
                builder.append(j);
                builder.append(',');
            }
            builder.append(LINE_SEPARATOR);
            lines.add(builder.toString());
        }

        try {
            write(lines, UTF8, new File(JAVA_IO_TMPDIR_FILE, "test.csv"), 2048, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long nanoTime1 = System.nanoTime();

        System.out.println((nanoTime1 - nanoTime0) / 1000);

    }

}
