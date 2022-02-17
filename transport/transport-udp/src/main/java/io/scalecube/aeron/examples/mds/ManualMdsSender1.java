package io.scalecube.aeron.examples.mds;

import static io.aeron.CommonContext.UDP_MEDIA;
import static io.scalecube.aeron.examples.AeronHelper.NUMBER_OF_MESSAGES;
import static io.scalecube.aeron.examples.AeronHelper.STREAM_ID;
import static io.scalecube.aeron.examples.AeronHelper.printPublication;

import io.aeron.Aeron;
import io.aeron.Aeron.Context;
import io.aeron.ChannelUriStringBuilder;
import io.aeron.ExclusivePublication;
import io.aeron.driver.MediaDriver;
import io.scalecube.aeron.examples.AeronHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.agrona.CloseHelper;
import org.agrona.concurrent.SigInt;

public class ManualMdsSender1 {

  public static final String ENDPOINT = "localhost:20121";

  private static final AtomicBoolean running = new AtomicBoolean(true);
  private static MediaDriver mediaDriver;
  private static Aeron aeron;

  /**
   * Main runner.
   *
   * @param args args
   */
  public static void main(String[] args) throws InterruptedException {
    SigInt.register(ManualMdsSender1::close);

    mediaDriver = MediaDriver.launchEmbedded();
    String aeronDirectoryName = mediaDriver.aeronDirectoryName();

    Context context =
        new Context()
            .aeronDirectoryName(aeronDirectoryName)
            .availableImageHandler(AeronHelper::printAvailableImage)
            .unavailableImageHandler(AeronHelper::printUnavailableImage);

    aeron = Aeron.connect(context);
    System.out.println("hello, " + context.aeronDirectoryName());

    String channel = new ChannelUriStringBuilder().media(UDP_MEDIA).endpoint(ENDPOINT).build();

    ExclusivePublication publication = aeron.addExclusivePublication(channel, STREAM_ID);

    printPublication(publication);

    for (long i = 0; i < NUMBER_OF_MESSAGES; i++) {
      AeronHelper.sendMessage(publication, i);
      Thread.sleep(TimeUnit.SECONDS.toMillis(1));
    }

    System.out.println("Done sending");

    close();
  }

  private static void close() {
    running.set(false);
    CloseHelper.close(aeron);
    CloseHelper.close(mediaDriver);
  }
}
