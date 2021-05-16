package io.mercury.serialization.avro;

import java.io.File;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import io.mercury.common.sys.SysProperties;
import io.mercury.serialization.avro.msg.AvroTextMsg;

public class ReadFromAvro {

	public static void main(String[] args) throws Exception {

		File file = new File(SysProperties.USER_HOME_FILE, "test");

		final DatumReader<AvroTextMsg> reader = new SpecificDatumReader<AvroTextMsg>();

		final DataFileReader<AvroTextMsg> msgReader = new DataFileReader<AvroTextMsg>(file, reader);

		AvroTextMsg msg = null;

		while (msgReader.hasNext()) {
			// Reuse user object by passing it to next(). This saves us from
			// allocating and garbage collecting many objects for files with
			// many items.
			msg = msgReader.next();
			System.out.println(msg);
		}

		msgReader.close();
	}

}
