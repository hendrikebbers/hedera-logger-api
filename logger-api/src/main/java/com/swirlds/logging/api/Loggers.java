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

package com.swirlds.logging.api;


import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

import com.swirlds.logging.api.internal.DefaultLoggingSystem;
import com.swirlds.logging.api.internal.util.EmergencyLogger;
import edu.umd.cs.findbugs.annotations.NonNull;

public final class Loggers {

    private final static System.Logger EMERGENCY_LOGGER = EmergencyLogger.getInstance();

    @NonNull
    public static Logger getLogger(@NonNull String name) {
        return DefaultLoggingSystem.getInstance().getLogger(name);
    }

    @NonNull
    public static Logger getLogger(@NonNull Class<?> clazz) {
        if (clazz == null) {
            final Class<?> callerClass = StackWalker.getInstance(RETAIN_CLASS_REFERENCE).getCallerClass();
            EMERGENCY_LOGGER.log(
                    System.Logger.Level.ERROR,
                    Loggers.class.getName() + ": clazz is null when called by '" + callerClass
                            + "'. Will use root logger instead.");
            return getLogger("");
        }
        return getLogger(clazz.getName());
    }

    @NonNull
    public static Marker getMarker(@NonNull String name) {
        return DefaultLoggingSystem.getInstance().getMarker(name);
    }
}
