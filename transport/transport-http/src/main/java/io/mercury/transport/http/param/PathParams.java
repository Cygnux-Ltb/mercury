package io.mercury.transport.http.param;

import io.mercury.common.collections.MutableSets;

import java.util.Set;

public final class PathParams {

    private final Set<PathParam> set = MutableSets.newUnifiedSet();

    private PathParams() {
    }

    public static PathParams newInstance() {
        return new PathParams();
    }

    public PathParams addParam(PathParam param) {
        set.add(param);
        return this;
    }

    public String toUrlParams() {
        StringBuilder sb = new StringBuilder("?");
        set.forEach(p -> sb.append(p).append(","));
        return sb.deleteCharAt(sb.length() - 1).toString();
    }


    public static void main(String[] args) {
        PathParams params = PathParams.newInstance();
        params.addParam(new PathParam("A", 1));
        params.addParam(new PathParam("B", 2));
        params.addParam(new PathParam("C", 3));

        System.out.println(params.toUrlParams());
    }

}
