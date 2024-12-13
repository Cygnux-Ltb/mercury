package io.mercury.serialization.fury;

import org.apache.fury.Fury;

import static org.apache.fury.config.Language.JAVA;

public final class FuryKeeper {

    public static Fury newInstance(Class<?>... classes) {
        var fury = Fury.builder()
                .withLanguage(JAVA)
                .requireClassRegistration(true)
                .build();
        for (var clazz : classes)
            fury.register(clazz);
        return fury;
    }

}
