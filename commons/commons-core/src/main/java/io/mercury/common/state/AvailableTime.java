package io.mercury.common.state;

import java.time.LocalTime;

public interface AvailableTime {

    boolean isRunningAllTime();

    LocalTime[] getStartTimes();

    LocalTime[] getEndTimes();

}
