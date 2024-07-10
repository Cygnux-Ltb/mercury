/*
 * Copyright 2019-2023 Adaptive Financial Consulting Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aeroncookbook.agrona.agents;

import org.agrona.DirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.ShutdownSignalBarrier;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiveAgent implements Agent
{
    private final ShutdownSignalBarrier barrier;
    private final OneToOneRingBuffer ringBuffer;
    private final int sendCount;
    private final Logger logger = LoggerFactory.getLogger(ReceiveAgent.class);

    public ReceiveAgent(final OneToOneRingBuffer ringBuffer, final ShutdownSignalBarrier barrier, final int sendCount)
    {
        this.ringBuffer = ringBuffer;
        this.barrier = barrier;
        this.sendCount = sendCount;
    }

    @Override
    public int doWork() throws Exception
    {
        ringBuffer.read(this::handler);
        return 0;
    }

    private void handler(final int messageType, final DirectBuffer buffer, final int offset, final int length)
    {
        final int lastValue = buffer.getInt(offset);

        if (lastValue == sendCount)
        {
            logger.info("received: {}", lastValue);
            barrier.signal();
        }
    }

    @Override
    public String roleName()
    {
        return "receiver";
    }
}
