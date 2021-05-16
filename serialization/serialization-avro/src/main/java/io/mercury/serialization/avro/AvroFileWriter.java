package io.mercury.serialization.avro;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import io.mercury.common.util.Assertor;

@NotThreadSafe
public final class AvroFileWriter<T extends SpecificRecord> implements Closeable {

	// SpecificRecord schema
	private final Schema schema;

	// DatumWriter instance use SpecificDatumWriter
	private final DatumWriter<T> datumWriter;
	// DataFileWriter
	private final DataFileWriter<T> fileWriter;

	/**
	 * 
	 * @param record
	 */
	public AvroFileWriter(T record) {
		this.schema = record.getSchema();
		this.datumWriter = new SpecificDatumWriter<>(schema);
		this.fileWriter = new DataFileWriter<>(datumWriter);
	}

	/**
	 * 
	 * @param saveFile
	 * @param records
	 * @throws IOException
	 */
	public void append(final File saveFile, Collection<T> records) throws IOException {
		append(null, saveFile, records);
	}

	/**
	 * 
	 * @param codecFactory
	 * @param saveFile
	 * @param records
	 * @throws IOException
	 */
	public void append(@Nullable CodecFactory codec, final File saveFile, Collection<T> records) throws IOException {
		Assertor.nonNull(saveFile, "saveFile");
		File dir = saveFile.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// 如果文件存在则追加, 否则创建新文件
		if (saveFile.exists()) {
			fileWriter.appendTo(saveFile);
		} else {
			fileWriter.create(schema, saveFile);
		}
		if (codec != null) {
			fileWriter.setCodec(codec);
		}
		// Serializing
		if (records != null && !records.isEmpty()) {
			Iterator<T> iterator = records.iterator();
			while (iterator.hasNext()) {
				// write record
				fileWriter.append(iterator.next());
			}
			fileWriter.fSync();
		}
	}

	@Override
	public void close() throws IOException {
		fileWriter.close();
	}

}
