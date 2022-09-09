package io.mercury.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.mercury.common.lang.Const;

import javax.annotation.Nullable;

/**
 * @author yellow013
 * <p>
 *         TODO 优化实现
 */
public final class JsonWrapper {

    // 普通JSON序列化
    private static final Gson Gson = TypeRegistrar
            .registerAll(new GsonBuilder())
            .create();

    // JSON序列化, 包含Null值
    private static final Gson GsonHasNulls = TypeRegistrar
            .registerAll(new GsonBuilder())
            .serializeNulls()
            .create();

    // 以较高可视化的格式返回JSON
    private final static Gson GsonPretty = TypeRegistrar
            .registerAll(new GsonBuilder())
            .setPrettyPrinting()
            .create();

    // 以漂亮的格式返回JSON, 包含Null值
    private static final Gson GsonPrettyHasNulls = TypeRegistrar
            .registerAll(new GsonBuilder())
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    /**
     * @param obj Object
     * @return String
     */
    public static String toJson(@Nullable Object obj) {
        return toJson0(Gson, obj);
    }

    /**
     * @param obj Object
     * @return String
     */
    public static String toPrettyJson(@Nullable Object obj) {
        return toJson0(GsonPretty, obj);
    }

    /**
     * @param obj Object
     * @return String
     */
    public static String toJsonHasNulls(@Nullable Object obj) {
        return toJson0(GsonHasNulls, obj);
    }

    /**
     * @param obj Object
     * @return String
     */
    public static String toPrettyJsonHasNulls(@Nullable Object obj) {
        return toJson0(GsonPrettyHasNulls, obj);
    }

    private static String toJson0(final Gson gson, final Object obj) {
        if (obj == null)
            return Const.StringConst.NULL_STRING;
        return gson.toJson(obj);
    }

}
