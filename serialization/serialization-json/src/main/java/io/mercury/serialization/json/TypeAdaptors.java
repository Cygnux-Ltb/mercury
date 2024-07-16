package io.mercury.serialization.json;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import io.mercury.common.datetime.pattern.impl.DatePattern;
import io.mercury.common.datetime.pattern.impl.DateTimePattern;
import io.mercury.common.datetime.pattern.impl.TimePattern;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public final class TypeAdaptors {

    public static final JsonSerializer<Duration> DurationSerializer =
            (src, typeOfSrc, context) ->
                    (src == null) ? null
                            : new JsonPrimitive(src.toString());

    public static final JsonSerializer<java.time.Instant> InstantSerializer =
            (src, typeOfSrc, context) ->
                    (src == null) ? null
                            : new JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(src));

    public static final JsonSerializer<ZoneId> ZoneIdSerializer =
            (src, typeOfSrc, context) ->
                    (src == null) ? null
                            : new JsonPrimitive(src.getId());

    public static final JsonSerializer<LocalDate> LocalDateSerializer =
            (src, typeOfSrc, context) ->
                    (src == null) ? null
                            : new JsonPrimitive(DatePattern.YY_MM_DD.getFormatter().format(src));

    public static final JsonSerializer<LocalTime> LocalTimeSerializer =
            (src, typeOfSrc, context) ->
                    (src == null) ? null
                            : new JsonPrimitive(TimePattern.HH_MM_SS_SSS.getFormatter().format(src));

    public static final JsonSerializer<LocalDateTime> LocalDateTimeSerializer =
            (src, typeOfSrc, context) ->
                    (src == null) ? null
                            : new JsonPrimitive(DateTimePattern.YY_MM_DD_HH_MM_SS_SSS.getFormatter().format(src));

    public static final JsonSerializer<ZonedDateTime> ZonedDateTimeSerializer =
            (src, typeOfSrc, context) ->
                    (src == null) ? null
                            : new JsonPrimitive(DateTimeFormatter.ISO_DATE_TIME.format(src));


    public static final Map<Class<?>, JsonSerializer<?>> TypeAdaptorMap = new HashMap<>();

    static {
        TypeAdaptorMap.put(Duration.class, DurationSerializer);
        TypeAdaptorMap.put(Instant.class, InstantSerializer);
        TypeAdaptorMap.put(ZoneId.class, ZoneIdSerializer);
        TypeAdaptorMap.put(LocalDate.class, LocalDateSerializer);
        TypeAdaptorMap.put(LocalTime.class, LocalTimeSerializer);
        TypeAdaptorMap.put(LocalDateTime.class, LocalDateTimeSerializer);
        TypeAdaptorMap.put(ZonedDateTime.class, ZonedDateTimeSerializer);
    }


}
