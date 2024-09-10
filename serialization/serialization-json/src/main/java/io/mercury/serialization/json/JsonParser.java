package io.mercury.serialization.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.mercury.common.collections.ImmutableLists.newImmutableList;
import static io.mercury.common.collections.ImmutableMaps.newImmutableMap;
import static io.mercury.common.collections.MutableLists.newFastList;
import static io.mercury.common.collections.MutableMaps.newUnifiedMap;
import static io.mercury.common.util.StringSupport.isNullOrEmpty;

public final class JsonParser {

    private JsonParser() {
    }

    // TODO 添加反序列化属性
    private static final ObjectMapper Mapper = new ObjectMapper();

    // TODO 添加配置信息
    private static final TypeFactory TYPE_FACTORY = Mapper.getTypeFactory();

    /**
     * @param json String
     * @return JsonElement
     */
    public static JSONObject parseJson(String json) throws JsonParseException {
        try {
            return JSON.parseObject(json, Feature.UseBigDecimalForDoubles);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    /**
     * @param json String
     * @return boolean
     */
    public static boolean isJsonArray(String json) throws JsonParseException {
        try {
            return JSON.isValidArray(json);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    /**
     * @param json String
     * @return boolean
     */
    public static boolean isJsonObject(String json) throws JsonParseException {
        try {
            return JSON.isValidObject(json);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    /**
     * @param json String
     * @return T
     * @throws JsonParseException e
     */
    @Nullable
    public static <T> T toObject(String json) throws JsonParseException {
        try {
            return JSON.parseObject(json, new com.alibaba.fastjson2.TypeReference<>() {
            });
        } catch (Exception e) {
            throw new JsonParseException(json, e);
        }
    }

    /**
     * @param json String
     * @param type Class<T>
     * @return T
     * @throws JsonParseException e
     */
    @Nullable
    public static <T> T toObject(String json, Class<T> type) throws JsonParseException {
        try {
            return JSON.parseObject(json, type);
        } catch (Exception e) {
            throw new JsonParseException(json, e);
        }
    }

    //**************************** LIST CONVERT ****************************//

    /**
     * @param json String
     * @return List<T>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <T> List<T> toList(String json) throws JsonParseException {
        try {
            if (isNullOrEmpty(json))
                return new ArrayList<>();
            return Mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<>() {
            });
        } catch (Exception e) {
            throw new JsonParseException(json, e);
        }
    }

    /**
     * @param json String
     * @return MutableList<T>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <T> MutableList<T> toMutableList(String json) throws JsonParseException {
        return newFastList(
                // List convert to MutableList
                toList(json));
    }

    /**
     * @param json String
     * @return ImmutableList<T>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <T> ImmutableList<T> toImmutableList(String json) throws JsonParseException {
        return newImmutableList(
                // List convert to MutableList
                toList(json));
    }

    /**
     * @param json String
     * @param type Class<T>
     * @return List<T>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <T> List<T> toList(String json, Class<T> type) throws JsonParseException {
        try {
            if (json == null || json.isEmpty() || type == null)
                return new ArrayList<>();
            return JSON.parseArray(json, type);
        } catch (Exception e) {
            throw new JsonParseException(json, e);
        }
    }

    /**
     * @param json String
     * @return MutableList<T>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <T> MutableList<T> toMutableList(String json, Class<T> type) throws JsonParseException {
        return newFastList(
                // List convert to MutableList
                toList(json, type));
    }

    /**
     * @param json String
     * @return ImmutableList<T>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <T> ImmutableList<T> toImmutableList(String json, Class<T> type) throws JsonParseException {
        return newImmutableList(
                // List convert to MutableList
                toList(json, type));
    }


    //**************************** MAP CONVERT ****************************//

    /**
     * @param json String
     * @return Map<K, V>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <K, V> Map<K, V> toMap(String json) throws JsonParseException {
        try {
            if (json == null || json.isEmpty())
                return new HashMap<>();
            return JSON.parseObject(json, new com.alibaba.fastjson2.TypeReference<>() {
            });
        } catch (Exception e) {
            throw new JsonParseException(json, e);
        }
    }

    /**
     * @param json String
     * @return MutableMap<K, V>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <K, V> MutableMap<K, V> toMutableMap(String json) throws JsonParseException {
        return newUnifiedMap(
                // Map convert to MutableMap
                toMap(json));
    }

    /**
     * @param json String @Nonnull
     * @return ImmutableMap<K, V>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <K, V> ImmutableMap<K, V> toImmutableMap(String json) throws JsonParseException {
        return newImmutableMap(
                // Map convert to ImmutableMap
                toMap(json));
    }

    /**
     * @param json      String
     * @param keyType   Class<K>
     * @param valueType Class<V>
     * @return Map<K, V>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyType, Class<V> valueType)
            throws JsonParseException {
        try {
            if (json == null || json.isEmpty())
                return new HashMap<>();
            if (keyType == null || valueType == null)
                return toMap(json);
            return Mapper.readValue(json, TYPE_FACTORY.constructMapLikeType(Map.class, keyType, valueType));
        } catch (Exception e) {
            throw new JsonParseException(json, e);
        }
    }


    /**
     * @param json      String
     * @param keyType   Class<K>
     * @param valueType Class<V>
     * @return MutableMap<K, V>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <K, V> MutableMap<K, V> toMutableMap(String json, Class<K> keyType, Class<V> valueType)
            throws JsonParseException {
        return newUnifiedMap(
                // Map convert to MutableMap
                toMap(json, keyType, valueType));
    }

    /**
     * @param json      String
     * @param keyType   Class<K>
     * @param valueType Class<V>
     * @return ImmutableMap<K, V>
     * @throws JsonParseException e
     */
    @Nonnull
    public static <K, V> ImmutableMap<K, V> toImmutableMap(String json, Class<K> keyType, Class<V> valueType)
            throws JsonParseException {
        return newImmutableMap(
                // Map convert to ImmutableMap
                toMap(json, keyType, valueType));
    }


    public static void main(String[] args) {
        Map<String, String> map0 = new HashMap<>();
        map0.put("A", "1");
        map0.put("B", "2");
        map0.put("C", "11");
        map0.put("D", null);
        map0.put("E", null);
        String json = JsonWriter.toJsonHasNulls(map0);
        System.out.println(json);
        Map<Object, Object> map1 = JsonParser.toMap(json);
        System.out.println(map1);
        map1.forEach((key, value) -> {
            System.out.println(key.getClass());
            System.out.println(value.getClass());
        });
    }

}
