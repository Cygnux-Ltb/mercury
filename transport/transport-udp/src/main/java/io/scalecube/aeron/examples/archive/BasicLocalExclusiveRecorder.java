package io.scalecube.aeron.examples.archive;

import static io.aeron.CommonContext.IPC_MEDIA;
import static io.aeron.CommonContext.MDC_CONTROL_MODE_DYNAMIC;
import static io.aeron.CommonContext.UDP_MEDIA;
import static io.scalecube.aeron.examples.AeronHelper.NUMBER_OF_MESSAGES;
import static io.scalecube.aeron.examples.AeronHelper.STREAM_ID;
import static io.scalecube.aeron.examples.AeronHelper.awaitRecordingPosCounter;
import static io.scalecube.aeron.examples.AeronHelper.controlResponseChannel;
import static io.scalecube.aeron.examples.AeronHelper.localControlChannel;
import static io.scalecube.aeron.examples.AeronHelper.printArchiveContext;
import static io.scalecube.aeron.examples.AeronHelper.printRecordedPublication;

import io.aeron.Aeron;
import io.aeron.ChannelUriStringBuilder;
import io.aeron.CommonContext;
import io.aeron.Publication;
import io.aeron.archive.Archive;
import io.aeron.archive.ArchiveThreadingMode;
import io.aeron.archive.client.AeronArchive;
import io.aeron.archive.status.RecordingPos;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.status.SystemCounterDescriptor;
import io.scalecube.aeron.examples.AeronHelper;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.agrona.CloseHelper;
import org.agrona.concurrent.SigInt;
import org.agrona.concurrent.status.AtomicCounter;
import org.agrona.concurrent.status.CountersReader;

public class BasicLocalExclusiveRecorder {

  public static final String CHANNEL =
      new ChannelUriStringBuilder()
          .media(IPC_MEDIA)
          .endpoint(String.join("-", "ipc_endpoint", UUID.randomUUID().toString()))
          .build();

  public static final String RECORDING_EVENTS_CHANNEL_ENDPOINT = "localhost:8030";
  public static final String CONTROL_CHANNEL_ENDPOINT = "localhost:8010";
  public static final String REPLICATION_CHANNEL_ENDPOINT = "localhost:0";

  private static MediaDriver mediaDriver;
  private static Aeron aeron;
  private static Archive archive;
  private static AeronArchive aeronArchive;
  private static Publication publication;
  private static Publication publication2;
  private static Publication publication3;
  private static final AtomicBoolean running = new AtomicBoolean(true);

  /**
   * Main runner.
   *
   * @param args args
   */
  public static void main(String[] args) throws InterruptedException {
    SigInt.register(BasicLocalExclusiveRecorder::close);

    Path aeronPath = Paths.get(CommonContext.generateRandomDirName());
    String instanceName = aeronPath.getFileName().toString();
    Path archivePath =
        AeronHelper.archivePath()
            .orElseGet(() -> Paths.get(String.join("-", instanceName, "archive")));

    mediaDriver =
        MediaDriver.launch(
            new MediaDriver.Context()
                .aeronDirectoryName(aeronPath.toString())
                .spiesSimulateConnection(true));

    aeron =
        Aeron.connect(
            new Aeron.Context()
                .aeronDirectoryName(aeronPath.toString())
                .availableImageHandler(AeronHelper::printAvailableImage)
                .unavailableImageHandler(AeronHelper::printUnavailableImage));

    archive =
        Archive.launch(
            new Archive.Context()
                .aeron(aeron)
                .mediaDriverAgentInvoker(mediaDriver.sharedAgentInvoker())
                .errorCounter(
                    new AtomicCounter(
                        mediaDriver.context().countersValuesBuffer(),
                        SystemCounterDescriptor.ERRORS.id()))
                .errorHandler(mediaDriver.context().errorHandler())
                .localControlChannel(localControlChannel(instanceName))
                .controlChannel(controlChannel())
                .recordingEventsChannel(recordingEventsChannel())
                .replicationChannel(replicationChannel())
                .aeronDirectoryName(aeronPath.toString())
                .archiveDirectoryName(archivePath.toString())
                .threadingMode(ArchiveThreadingMode.SHARED));

    printArchiveContext(archive.context());

    CountersReader counters = aeron.countersReader();

    aeronArchive =
        AeronArchive.connect(
            new AeronArchive.Context()
                .aeron(aeron)
                .controlResponseChannel(controlResponseChannel()));

    long controlSessionId = aeronArchive.controlSessionId();
    System.out.printf("### controlSessionId: %s%n", controlSessionId);

    // start recorded publication
    publication = aeronArchive.addRecordedExclusivePublication(CHANNEL, STREAM_ID);
    int recordingPosCounterId = awaitRecordingPosCounter(counters, publication.sessionId());
    long recordingId = RecordingPos.getRecordingId(counters, recordingPosCounterId);

    printRecordedPublication(aeronArchive, publication, recordingPosCounterId, recordingId);

    // start recorded publication
    publication2 = aeronArchive.addRecordedExclusivePublication(CHANNEL, STREAM_ID);
    int recordingPosCounterId2 = awaitRecordingPosCounter(counters, publication2.sessionId());
    long recordingId2 = RecordingPos.getRecordingId(counters, recordingPosCounterId2);

    printRecordedPublication(aeronArchive, publication2, recordingPosCounterId2, recordingId2);

    // start recorded publication
    publication3 = aeronArchive.addRecordedExclusivePublication(CHANNEL, STREAM_ID);
    int recordingPosCounterId3 = awaitRecordingPosCounter(counters, publication3.sessionId());
    long recordingId3 = RecordingPos.getRecordingId(counters, recordingPosCounterId3);

    printRecordedPublication(aeronArchive, publication3, recordingPosCounterId3, recordingId3);

    for (long i = 0; i < NUMBER_OF_MESSAGES; i++) {
      AeronHelper.recordMessage(counters, publication, i);
      AeronHelper.recordMessage(counters, publication2, i);
      AeronHelper.recordMessage(counters, publication3, i);
      Thread.sleep(TimeUnit.SECONDS.toMillis(1));
    }

    System.out.println("Done sending");

    close();
  }

  private static String replicationChannel() {
    return new ChannelUriStringBuilder()
        .media(UDP_MEDIA)
        .endpoint(REPLICATION_CHANNEL_ENDPOINT)
        .build();
  }

  private static String recordingEventsChannel() {
    return new ChannelUriStringBuilder()
        .media(UDP_MEDIA)
        .controlMode(MDC_CONTROL_MODE_DYNAMIC)
        .controlEndpoint(RECORDING_EVENTS_CHANNEL_ENDPOINT)
        .build();
  }

  private static String controlChannel() {
    return new ChannelUriStringBuilder()
        .media(UDP_MEDIA)
        .endpoint(CONTROL_CHANNEL_ENDPOINT)
        .build();
  }

  private static void close() {
    running.set(false);
    CloseHelper.close(publication);
    CloseHelper.close(publication2);
    CloseHelper.close(publication3);
    if (aeronArchive != null) {
      aeronArchive.stopRecording(publication);
      aeronArchive.stopRecording(publication2);
      aeronArchive.stopRecording(publication3);
    }
    CloseHelper.close(aeron);
    CloseHelper.close(aeronArchive);
    CloseHelper.close(archive);
    CloseHelper.close(mediaDriver);
  }
}
