package io.scalecube.aeron.examples.archive;

import io.aeron.archive.client.AeronArchive;
import org.agrona.collections.MutableReference;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AeronArchiveUtil {

    private AeronArchiveUtil() {
        // Do not instantiate
    }

    /**
     * Finds last {@link RecordingDescriptor} matched by given predicate. See for details {@link
     * AeronArchive#listRecording(long, io.aeron.archive.client.RecordingDescriptorConsumer)}.
     *
     * @param aeronArchive aeronArchive
     * @param predicate    predicate
     * @return result
     */
    public static RecordingDescriptor findLastRecording(
            AeronArchive aeronArchive, Predicate<RecordingDescriptor> predicate) {
        MutableReference<RecordingDescriptor> result = new MutableReference<>();
        aeronArchive.listRecordings(
                0,
                Integer.MAX_VALUE,
                (controlSessionId,
                 correlationId,
                 recordingId,
                 startTimestamp,
                 stopTimestamp,
                 startPosition,
                 stopPosition,
                 initialTermId,
                 segmentFileLength,
                 termBufferLength,
                 mtuLength,
                 sessionId,
                 streamId1,
                 strippedChannel,
                 originalChannel,
                 sourceIdentity) -> {
                    RecordingDescriptor value =
                            new RecordingDescriptor(
                                    controlSessionId,
                                    correlationId,
                                    recordingId,
                                    startTimestamp,
                                    stopTimestamp,
                                    startPosition,
                                    stopPosition,
                                    initialTermId,
                                    segmentFileLength,
                                    termBufferLength,
                                    mtuLength,
                                    sessionId,
                                    streamId1,
                                    strippedChannel,
                                    originalChannel,
                                    sourceIdentity);
                    if (predicate.test(value)) {
                        result.set(value);
                    }
                });

        return result.get();
    }

    /**
     * Finds first {@link RecordingDescriptor} matched by given predicate. See for details {@link
     * AeronArchive#listRecording(long, io.aeron.archive.client.RecordingDescriptorConsumer)}.
     *
     * @param aeronArchive    aeronArchive
     * @param fromRecordingId at which to begin the searching
     * @param predicate       predicate
     * @return result
     */
    public static RecordingDescriptor findFirstRecording(AeronArchive aeronArchive,
                                                         long fromRecordingId,
                                                         Predicate<RecordingDescriptor> predicate) {
        if (fromRecordingId < 0) {
            fromRecordingId = 0;
        }
        MutableReference<RecordingDescriptor> result = new MutableReference<>();
        int step = 10;
        for (long i = fromRecordingId; i < Integer.MAX_VALUE; i = i + step) {
            int count = aeronArchive.listRecordings(
                    i,
                    step,
                    (controlSessionId,
                     correlationId,
                     recordingId,
                     startTimestamp,
                     stopTimestamp,
                     startPosition,
                     stopPosition,
                     initialTermId,
                     segmentFileLength,
                     termBufferLength,
                     mtuLength,
                     sessionId,
                     streamId,
                     strippedChannel,
                     originalChannel,
                     sourceIdentity) -> {
                        RecordingDescriptor value =
                                new RecordingDescriptor(
                                        controlSessionId,
                                        correlationId,
                                        recordingId,
                                        startTimestamp,
                                        stopTimestamp,
                                        startPosition,
                                        stopPosition,
                                        initialTermId,
                                        segmentFileLength,
                                        termBufferLength,
                                        mtuLength,
                                        sessionId,
                                        streamId,
                                        strippedChannel,
                                        originalChannel,
                                        sourceIdentity);
                        if (result.get() == null && predicate.test(value)) {
                            result.set(value);
                        }
                    });
            if (result.get() != null || count < step) {
                break;
            }
        }
        return result.get();
    }

    /**
     * Finds all {@link RecordingDescriptor}\s matched by given predicate. See for details {@link
     * AeronArchive#listRecording(long, io.aeron.archive.client.RecordingDescriptorConsumer)}.
     *
     * @param aeronArchive aeronArchive
     * @param predicate    predicate
     * @return result
     */
    public static List<RecordingDescriptor> findAllRecordings(
            AeronArchive aeronArchive, Predicate<RecordingDescriptor> predicate) {
        MutableReference<List<RecordingDescriptor>> result = new MutableReference<>(new ArrayList<>());
        aeronArchive.listRecordings(
                0,
                Integer.MAX_VALUE,
                (controlSessionId,
                 correlationId,
                 recordingId,
                 startTimestamp,
                 stopTimestamp,
                 startPosition,
                 stopPosition,
                 initialTermId,
                 segmentFileLength,
                 termBufferLength,
                 mtuLength,
                 sessionId,
                 streamId1,
                 strippedChannel,
                 originalChannel,
                 sourceIdentity) -> {
                    RecordingDescriptor value =
                            new RecordingDescriptor(
                                    controlSessionId,
                                    correlationId,
                                    recordingId,
                                    startTimestamp,
                                    stopTimestamp,
                                    startPosition,
                                    stopPosition,
                                    initialTermId,
                                    segmentFileLength,
                                    termBufferLength,
                                    mtuLength,
                                    sessionId,
                                    streamId1,
                                    strippedChannel,
                                    originalChannel,
                                    sourceIdentity);
                    if (predicate.test(value)) {
                        result.get().add(value);
                    }
                });

        return result.get();
    }
}
