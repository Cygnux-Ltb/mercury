package io.mercury.common.state;

public interface Lockable {

    boolean isLocked();

    boolean tryLock();

    void unlock();

}
