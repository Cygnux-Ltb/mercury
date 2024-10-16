/*
 * Thanks from Greg Kopff
 *
 * Author repository : https://github.com/gkopff/gson-javatime-serialisers
 *
 * ************************************************************************
 *
 * Copyright 2014-2022 Greg Kopff
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.mercury.serialization.json;

/**
 * The {@code Converters} class contains static methods for registering Java Time converters.
 */
public final class TypeRegistrar {

//    public static <T> GsonBuilder registerAll(GsonBuilder builder) {
//        if (builder == null) {
//            throw new NullPointerException("builder cannot be null");
//        }
//        TypeAdaptors.TypeAdaptorMap.forEach(builder::registerTypeAdapter);
//        return builder;
//    }
//
//    /**
//     * @param builder    GsonBuilder
//     * @param type       Class<T>
//     * @param serializer JsonSerializer<T>
//     * @param <T>        T
//     * @return GsonBuilder
//     */
//    public static <T> GsonBuilder register(GsonBuilder builder, Class<T> type, JsonSerializer<T> serializer) {
//        if (builder == null) {
//            throw new NullPointerException("builder cannot be null");
//        }
//        builder.registerTypeAdapter(type, serializer);
//        return builder;
//    }


}
