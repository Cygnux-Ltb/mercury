package io.mercury.common.state;

import java.time.LocalTime;

public interface AvailableTime {

    boolean isAvailableAllTime();

    LocalTime[] getStartTimes();

    LocalTime[] getStopTimes();

}
