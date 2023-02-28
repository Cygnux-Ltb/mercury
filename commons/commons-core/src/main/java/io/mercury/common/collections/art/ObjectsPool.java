/*
 * Copyright 2019-2020 Maksim Zheravin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mercury.common.collections.art;

import io.mercury.common.log.Log4j2LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ObjectsPool {

    private static final Logger log = Log4j2LoggerFactory.getLogger(ObjectsPool.class);

    public static final int ORDER = 0;

    public static final int DIRECT_ORDER = 1;
    public static final int DIRECT_BUCKET = 2;
    public static final int ART_NODE_4 = 8;
    public static final int ART_NODE_16 = 9;
    public static final int ART_NODE_48 = 10;
    public static final int ART_NODE_256 = 11;

    public static final int SYMBOL_POSITION_RECORD = 12;

    private final ArrayStack[] pools;

    public static ObjectsPool createDefaultPool() {

        // initialize object pools
        final HashMap<Integer, Integer> objectsPoolConfig = new HashMap<>();
        objectsPoolConfig.put(ObjectsPool.DIRECT_ORDER, 512);
        objectsPoolConfig.put(ObjectsPool.DIRECT_BUCKET, 256);
        objectsPoolConfig.put(ObjectsPool.ART_NODE_4, 256);
        objectsPoolConfig.put(ObjectsPool.ART_NODE_16, 128);
        objectsPoolConfig.put(ObjectsPool.ART_NODE_48, 64);
        objectsPoolConfig.put(ObjectsPool.ART_NODE_256, 32);

        return new ObjectsPool(objectsPoolConfig);
    }

    public ObjectsPool(final Map<Integer, Integer> sizesConfig) {
        int maxStack = sizesConfig.keySet().stream().max(Integer::compareTo).orElse(0);
        this.pools = new ArrayStack[maxStack + 1];
        sizesConfig.forEach((type, size) -> this.pools[type] = new ArrayStack(size));
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final int type, final Supplier<T> supplier) {
        final T obj = (T) pools[type].pop(); // pollFirst is cheaper for empty pool
        if (obj == null) {
            log.debug("MISS {}", type);
            return supplier.get();
        } else {
            log.debug("HIT {} (count={})", type, pools[type].count);
            return obj;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final int type, final Function<ObjectsPool, T> constructor) {
        final T obj = (T) pools[type].pop(); // pollFirst is cheaper for empty pool
        if (obj == null) {
            log.debug("MISS {}", type);
            return constructor.apply(this);
        } else {
            log.debug("HIT {} (count={})", type, pools[type].count);
            return obj;
        }
    }

    public void put(final int type, Object object) {
        log.debug("RETURN {} (count={})", type, pools[type].count);
        pools[type].add(object);
    }

    private final static class ArrayStack {
        private int count;
        private final Object[] objs;

        ArrayStack(int size) {
            this.objs = new Object[size];
            this.count = 0;
        }

        void add(Object obj) {
            if (count != objs.length) {
                objs[count] = obj;
                count++;
            }
        }

        Object pop() {
            if (count != 0) {
                count--;
                Object obj = objs[count];
                objs[count] = null;
                return obj;
            }
            return null;
        }
    }

}
