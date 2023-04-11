package io.mercury.common.http;

import io.mercury.common.collections.MutableMaps;
import org.eclipse.collections.api.map.MutableMap;

public final class HeaderParams {

    private final MutableMap<HeaderEnum, String> headers = MutableMaps.newUnifiedMap();

    private HeaderParams() {
    }

    


}
