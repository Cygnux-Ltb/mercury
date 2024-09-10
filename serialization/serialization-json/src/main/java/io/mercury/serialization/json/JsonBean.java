package io.mercury.serialization.json;


import io.mercury.common.serialization.specific.JsonSerializable;

import javax.annotation.Nonnull;

public abstract class JsonConvertible implements JsonSerializable {

    @Nonnull
    @Override
    public String toJson() {
        return JsonWriter.toJson(this);
    }

}
