/*
 * Copyright 2014-2021 Real Logic Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.aeron.official.raw;

import org.HdrHistogram.Histogram;
import org.agrona.concurrent.SigInt;
import org.agrona.hints.ThreadHints;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

import static io.aeron.driver.Configuration.MTU_LENGTH_DEFAULT;
import static org.agrona.BitUtil.SIZE_OF_LONG;

/**
 * Benchmark used to calculate latency of underlying system.
 *
 * @see ReceiveWriteUdpPong
 */
public class WriteReceiveUdpPing {
    /**
     * Main method for launching the process.
     *
     * @param args passed to the process.
     * @throws IOException if an error occurs with the channel.
     */
    public static void main(final String[] args) throws IOException {
        int numChannels = 1;
        if (1 == args.length) {
            numChannels = Integer.parseInt(args[0]);
        }

        final Histogram histogram = new Histogram(TimeUnit.SECONDS.toNanos(10), 3);

        final ByteBuffer buffer = ByteBuffer.allocateDirect(MTU_LENGTH_DEFAULT);

        final DatagramChannel[] receiveChannels = new DatagramChannel[numChannels];
        for (int i = 0; i < receiveChannels.length; i++) {
            receiveChannels[i] = DatagramChannel.open();
            Common.init(receiveChannels[i]);
            receiveChannels[i].bind(new InetSocketAddress("localhost", Common.PONG_PORT + i));
        }

        final InetSocketAddress writeAddress = new InetSocketAddress("localhost", Common.PING_PORT);
        final DatagramChannel writeChannel = DatagramChannel.open();
        Common.init(writeChannel, writeAddress);

        final AtomicBoolean running = new AtomicBoolean(true);
        SigInt.register(() -> running.set(false));

        while (running.get()) {
            measureRoundTrip(histogram, buffer, receiveChannels, writeChannel, running);

            histogram.reset();
            System.gc();
            LockSupport.parkNanos(1000_000_000);
        }
    }

    private static void measureRoundTrip(final Histogram histogram, final ByteBuffer buffer,
                                         final DatagramChannel[] receiveChannels, final DatagramChannel writeChannel, final AtomicBoolean running)
            throws IOException {
        for (int sequenceNumber = 0; sequenceNumber < Common.NUM_MESSAGES; sequenceNumber++) {
            final long timestampNs = System.nanoTime();

            buffer.clear();
            buffer.putLong(sequenceNumber);
            buffer.putLong(timestampNs);
            buffer.flip();

            writeChannel.write(buffer);

            buffer.clear();
            boolean available = false;
            while (!available) {
                ThreadHints.onSpinWait();
                if (!running.get()) {
                    return;
                }

                for (int i = receiveChannels.length - 1; i >= 0; i--) {
                    if (null != receiveChannels[i].receive(buffer)) {
                        available = true;
                        break;
                    }
                }
            }

            final long receivedSequenceNumber = buffer.getLong(0);
            if (receivedSequenceNumber != sequenceNumber) {
                throw new IllegalStateException("Data Loss:" + sequenceNumber + " to " + receivedSequenceNumber);
            }

            final long durationNs = System.nanoTime() - buffer.getLong(SIZE_OF_LONG);
            histogram.recordValue(durationNs);
        }

        histogram.outputPercentileDistribution(System.out, 1000.0);
    }
}
