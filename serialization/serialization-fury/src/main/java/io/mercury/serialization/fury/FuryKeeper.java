package io.mercury.serialization.fury;

import org.apache.fury.Fury;
import org.apache.fury.ThreadLocalFury;
import org.apache.fury.ThreadSafeFury;

import static org.apache.fury.config.Language.JAVA;

public final class FuryKeeper {

    static final ThreadSafeFury FURY_MSG_USED = new ThreadLocalFury(classLoader -> {
        var fury = Fury.builder()
                .withLanguage(JAVA)
                .withClassLoader(classLoader)
                .build();
        fury.register(FuryMsg.class);
        return fury;
    });

}
