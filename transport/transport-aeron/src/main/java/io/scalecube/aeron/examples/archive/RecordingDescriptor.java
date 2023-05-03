package io.scalecube.aeron.examples.archive;

/**
 * Constructor.
 *
 * @param controlSessionId  of the originating session requesting to list recordings.
 * @param correlationId     of the associated request to list recordings.
 * @param recordingId       of this recording descriptor.
 * @param startTimestamp    for the recording.
 * @param stopTimestamp     for the recording.
 * @param startPosition     for the recording against the recorded publication.
 * @param stopPosition      reached for the recording.
 * @param initialTermId     for the recorded publication.
 * @param segmentFileLength for the recording which is a multiple of termBufferLength.
 * @param termBufferLength  for the recorded publication.
 * @param mtuLength         for the recorded publication.
 * @param sessionId         for the recorded publication.
 * @param streamId          for the recorded publication.
 * @param strippedChannel   for the recorded publication.
 * @param originalChannel   for the recorded publication.
 * @param sourceIdentity    for the recorded publication.
 */
public record RecordingDescriptor(
        long controlSessionId,
        long correlationId,
        long recordingId,
        long startTimestamp,
        long stopTimestamp,
        long startPosition,
        long stopPosition,
        int initialTermId,
        int segmentFileLength,
        int termBufferLength,
        int mtuLength,
        int sessionId,
        int streamId,
        String strippedChannel,
        String originalChannel,
        String sourceIdentity) {

    @Override
    public String toString() {
        return "RecordingDescriptor{"
                + "controlSessionId="
                + controlSessionId
                + ", correlationId="
                + correlationId
                + ", recordingId="
                + recordingId
                + ", startTimestamp="
                + startTimestamp
                + ", stopTimestamp="
                + stopTimestamp
                + ", startPosition="
                + startPosition
                + ", stopPosition="
                + stopPosition
                + ", initialTermId="
                + initialTermId
                + ", segmentFileLength="
                + segmentFileLength
                + ", termBufferLength="
                + termBufferLength
                + ", mtuLength="
                + mtuLength
                + ", sessionId="
                + sessionId
                + ", streamId="
                + streamId
                + ", strippedChannel='"
                + strippedChannel
                + '\''
                + ", originalChannel='"
                + originalChannel
                + '\''
                + ", sourceIdentity='"
                + sourceIdentity
                + '\''
                + '}';
    }

}
