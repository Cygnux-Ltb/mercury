package io.mercury.common.http.param;

import io.mercury.common.collections.MutableSets;
import io.mercury.common.lang.Throws;
import io.mercury.common.util.ArrayUtil;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

import static io.mercury.common.util.ArrayUtil.splitArray;

public final class PathParams {

    private final Set<PathParam> set = MutableSets.newUnifiedSet();

    private PathParams() {
    }

    public static PathParams with() {
        return new PathParams();
    }

    public static PathParams with(PathParam... params) {
        return new PathParams().addParams(params);
    }

    public static PathParams with(Object... params) {
        return new PathParams().addParams(params);
    }

    public PathParams addParams(Object... params) {
        if (ArrayUtil.isNullOrEmpty(params))
            Throws.illegalArgument("params");
        splitArray(2, params)
                .forEach(objs ->
                        addParams(objs[0], objs.length < 2 ? null : objs[1]));
        return this;
    }

    public PathParams addParams(Object name, Object value) {
        return addParams(new PathParam(name, value == null ? "" : value));
    }

    public PathParams addParams(PathParam... params) {
        Collections.addAll(set, params);
        return this;
    }

    public String toUriParams() {
        StringBuilder sb = new StringBuilder("?");
        set.forEach(param -> sb.append(param).append("&"));
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public URI toFullUri(@Nonnull String uri) {
        return URI.create(uri + this.toUriParams());
    }

}
