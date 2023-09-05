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

package com.swirlds.base.context.internal;

import com.swirlds.base.context.Context;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The GlobalContext is a {@link Context} implementation that defined as a singleton and is used to store global
 * information.
 *
 * @see ThreadLocalContext
 * @see Context
 */
public final class GlobalContext implements Context {

    private static final GlobalContext INSTANCE = new GlobalContext();

    private final AtomicReference<Map<String, String>> contextMapRef;

    /**
     * private constructor to prevent instantiation.
     */
    private GlobalContext() {
        contextMapRef = new AtomicReference<>(new HashMap<>());
    }

    @Override
    public AutoCloseable add(@NonNull String key, @Nullable String value) {
        Objects.requireNonNull(key, "key must not be null");
        contextMapRef.getAndUpdate(map -> {
            Map<String, String> newMap = new HashMap<>(map);
            newMap.put(key, value);
            return newMap;
        });
        return () -> remove(key);
    }

    @Override
    public void remove(@NonNull String key) {
        Objects.requireNonNull(key, "key must not be null");
        contextMapRef.getAndUpdate(map -> {
            Map<String, String> newMap = new HashMap<>(map);
            newMap.remove(key);
            return newMap;
        });
    }

    /**
     * Clears the context.
     */
    public void clear() {
        contextMapRef.set(new HashMap<>());
    }

    /**
     * Returns the singleton instance of the {@link GlobalContext}.
     *
     * @return the singleton instance of the {@link GlobalContext}
     */
    @NonNull
    public static GlobalContext getInstance() {
        return INSTANCE;
    }

    /**
     * returns the content of the context as an immutable map. This method should only be used by the base apis.
     *
     * @return the content of the context as an immutable map
     */
    @NonNull
    public static Map<String, String> getContextMap() {
        return Collections.unmodifiableMap(INSTANCE.contextMapRef.get());
    }
}
