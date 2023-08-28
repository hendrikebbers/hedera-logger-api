/*
 * Copyright (C) 2023 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.swirlds.base.context;

import com.swirlds.base.context.internal.GlobalContext;
import com.swirlds.base.context.internal.ThreadLocalContext;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

public interface Context {

    void put(@NonNull String key, @Nullable String value);

    void put(@NonNull String key, @Nullable String... values);

    void remove(@NonNull String key);

    void clear();

    default void put(@NonNull String key, int value) {
        put(key, Integer.toString(value));
    }

    default void put(@NonNull String key, int... values) {
        String[] stringValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            stringValues[i] = Integer.toString(values[i]);
        }
        put(key, stringValues);
    }

    default void put(@NonNull String key, long value) {
        put(key, Long.toString(value));
    }

    default void put(@NonNull String key, long... values) {
        String[] stringValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            stringValues[i] = Long.toString(values[i]);
        }
        put(key, stringValues);
    }

    default void put(@NonNull String key, float value) {
        put(key, Float.toString(value));
    }

    default void put(@NonNull String key, float... values) {
        String[] stringValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            stringValues[i] = Float.toString(values[i]);
        }
        put(key, stringValues);
    }

    default void put(@NonNull String key, double value) {
        put(key, Double.toString(value));
    }

    default void put(@NonNull String key, double... values) {
        String[] stringValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            stringValues[i] = Double.toString(values[i]);
        }
        put(key, stringValues);
    }

    default void put(@NonNull String key, boolean value) {
        put(key, Boolean.toString(value));
    }

    default void put(@NonNull String key, boolean... values) {
        String[] stringValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            stringValues[i] = Boolean.toString(values[i]);
        }
        put(key, stringValues);
    }

    @NonNull
    static Context getGlobalContext() {
        return GlobalContext.getInstance();
    }

    @NonNull
    static Context getThreadLocalContext() {
        return ThreadLocalContext.getInstance();
    }
}