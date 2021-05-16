package io.mercury.serialization.avro;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import io.mercury.common.file.FileUtil;
import io.mercury.common.util.Assertor;

@NotThreadSafe
public final class AvroMsgFileWriter<T extends SpecificRecord> implements Closeable {

	private final Schema schema;

	private final DatumWriter<T> datumWriter;
	private final DataFileWriter<T> fileWriter;

	public AvroMsgFileWriter(T record) {
		this.schema = record.getSchema();
		this.datumWriter = new SpecificDatumWriter<>(schema);
		this.fileWriter = new DataFileWriter<>(datumWriter);
	}

	public void append(final File saveFile, Collection<T> records) throws IOException {
		Assertor.nonNull(saveFile, "saveFile");
		
		// Serializing

		// Now let's serialize our Users to disk.

		// 把两条记录写入到文件中 (序列化)
		// DatumWriter是一个接口,需要用它的实现类创建对象
		// 如果创建了实例化对象, 则使用SpecificDatumWriter来创建对象
		// 如果没有创建实例化对象, 则使用GenericDatumWriter来创建对象

		// 创建序列化文件, 文件会创建到项目的根目录下
		fileWriter.create(schema, saveFile);
		fileWriter.setCodec(CodecFactory.snappyCodec());

		if (records != null && !records.isEmpty()) {
			Iterator<T> iterator = records.iterator();
			while (iterator.hasNext())
				// write record
				fileWriter.append(iterator.next());
		}
		fileWriter.flush();
	}

	@Override
	public void close() throws IOException {
		fileWriter.close();
	}

}
