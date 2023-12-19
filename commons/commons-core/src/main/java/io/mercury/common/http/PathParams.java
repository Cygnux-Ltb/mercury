package io.mercury.common.http;

import io.mercury.common.collections.MutableSets;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

public final class PathParams {

    public record PathParam(
            Object name,
            Object value) {

        @Override
        public String toString() {
            return STR."\{name}=\{value == null ? "" : value}";
        }

    }

    private final Set<PathParam> set = MutableSets.newUnifiedSet();

    private PathParams() {
    }

    public static PathParams empty() {
        return new PathParams();
    }

    public static PathParams with(PathParam... params) {
        return new PathParams().addParams(params);
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
