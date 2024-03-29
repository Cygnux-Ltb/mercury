package com.lmax.disruptor.support;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.WaitStrategy;

public class DummyWaitStrategy implements WaitStrategy {

    public int signalAllWhenBlockingCalls = 0;

    @Override
    public long waitFor(long sequence, Sequence cursor,
                        Sequence dependentSequence, SequenceBarrier barrier)
            throws AlertException, InterruptedException, TimeoutException {
        return 0;
    }

    @Override
    public void signalAllWhenBlocking() {
        signalAllWhenBlockingCalls++;
    }
}
