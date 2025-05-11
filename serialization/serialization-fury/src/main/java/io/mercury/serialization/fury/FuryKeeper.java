package io.mercury.serialization.fury;

import org.apache.fury.Fury;
import org.apache.fury.config.Language;

public final class FuryKeeper {

    private FuryKeeper() {
    }

    /**
     * @param classes Class<?> array
     * @return Fury
     */
    public static Fury newInstance(Class<?>... classes) {
        return newInstance(Language.JAVA, classes);
    }

    /**
     * @param lang    Language
     * @param classes Class<?> array
     * @return Fury
     */
    public static Fury newInstance(Language lang, Class<?>... classes) {
        var fury = Fury.builder()
                .withLanguage(lang)
                .requireClassRegistration(true)
                .build();
        for (var clazz : classes)
            fury.register(clazz);
        return fury;
    }

}
