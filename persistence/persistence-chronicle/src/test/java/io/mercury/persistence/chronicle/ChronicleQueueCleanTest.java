package io.mercury.persistence.chronicle;

import java.time.ZonedDateTime;

import org.junit.Test;

import io.mercury.common.datetime.TimeZone;
import io.mercury.common.thread.SleepSupport;
import io.mercury.persistence.chronicle.queue.ChronicleStringQueue;
import io.mercury.persistence.chronicle.queue.ChronicleStringQueue.ChronicleStringAppender;
import io.mercury.persistence.chronicle.queue.ChronicleStringQueue.ChronicleStringReader;
import io.mercury.persistence.chronicle.queue.FileCycle;

public class ChronicleQueueCleanTest {

	// @Ignore
	@Test
	public void test0() {

		ChronicleStringQueue persistence = ChronicleStringQueue.newBuilder().folder("test").fileClearCycle(5)
				.fileCycle(FileCycle.MINUTELY).build();

		ChronicleStringAppender appender = persistence.acquireAppender();
		ChronicleStringReader reader = persistence.createReader(System.out::println);

		// boolean moved = reader.moveTo(LocalDateTime.now().minusMinutes(20),
		// TimeZones.SYSTEM_DEFAULT);

		// System.out.println("is moved == " + moved);
		reader.runWithNewThread();
		int i = 0;
		while (true) {
			try {
				appender.append(ZonedDateTime.now(TimeZone.UTC).toString());
				Thread.sleep(2000);
				i++;
				if (i == 20) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		persistence.close();
		SleepSupport.sleep(2000);
		System.out.println(appender.isClosed());
		System.out.println(reader.isClosed());

	}

}
