package io.mercury.serialization.avro;

import java.io.File;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import junit.framework.TestCase;

/**
 * 
 */
public class AvroTest extends TestCase {

	public static void main(String[] args) {
		// Serializing

		// Now let's serialize our Users to disk.

		// Serialize user1, user2 and user3 to disk
		DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
		DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
		dataFileWriter.create(user1.getSchema(), new File("users.avro"));
		dataFileWriter.append(user1);
		dataFileWriter.append(user2);
		dataFileWriter.append(user3);
		dataFileWriter.close();

		// We create a DatumWriter, which converts Java objects into an in-memory
		// serialized format. The SpecificDatumWriter class is used with generated
		// classes and extracts the schema from the specified generated type.

		// Next we create a DataFileWriter, which writes the serialized records, as well
		// as the schema, to the file specified in the dataFileWriter.create call. We
		// write our users to the file via calls to the dataFileWriter.append method.
		// When we are done writing, we close the data file.

		// Deserializing

		// Finally, let's deserialize the data file we just created.

		// Deserialize Users from disk
		DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
		DataFileReader<User> dataFileReader = new DataFileReader<User>(file, userDatumReader);
		User user = null;
		while (dataFileReader.hasNext()) {
			// Reuse user object by passing it to next(). This saves us from
			// allocating and garbage collecting many objects for files with
			// many items.
			user = dataFileReader.next(user);
			System.out.println(user);
		}

		// Deserializing is very similar to serializing. We create a
		// SpecificDatumReader, analogous to the SpecificDatumWriter we used in
		// serialization, which converts in-memory serialized items into instances of
		// our generated class, in this case User. We pass the DatumReader and the
		// previously created File to a DataFileReader, analogous to the DataFileWriter,
		// which reads both the schema used by the writer as well as the data from the
		// file on disk. The data will be read using the writer's schema included in the
		// file and the schema provided by the reader, in this case the User class. The
		// writer's schema is needed to know the order in which fields were written,
		// while the reader's schema is needed to know what fields are expected and how
		// to fill in default values for fields added since the file was written. If
		// there are differences between the two schemas, they are resolved according to
		// the Schema Resolution specification.

		// Next we use the DataFileReader to iterate through the serialized Users and
		// print the deserialized object to stdout. Note how we perform the iteration:
		// we create a single User object which we store the current deserialized user
		// in, and pass this record object to every call of dataFileReader.next. This is
		// a performance optimization that allows the DataFileReader to reuse the same
		// User object rather than allocating a new User for every iteration, which can
		// be very expensive in terms of object allocation and garbage collection if we
		// deserialize a large data file. While this technique is the standard way to
		// iterate through a data file, it's also possible to use for (User user :
		// dataFileReader) if performance is not a concern.

	}
}
