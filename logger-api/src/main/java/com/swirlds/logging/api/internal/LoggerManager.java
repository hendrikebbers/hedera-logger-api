/*
 * Copyright (C) 2023 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.swirlds.logging.api.internal;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.internal.console.ConsoleLogger;
import com.swirlds.logging.api.internal.marker.MarkerImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoggerManager {

    private final static LoggerManager INSTANCE = new LoggerManager();

    private final Map<String, AbstractLogger> loggers;

    public LoggerManager() {
        loggers = new ConcurrentHashMap<>();
    }

    public AbstractLogger getLogger(String name) {
        return loggers.computeIfAbsent(name, n -> new ConsoleLogger(n));
    }

    public static LoggerManager getInstance() {
        return INSTANCE;
    }

    public Marker getMarker(String name) {
        return new MarkerImpl(name);
    }

    public boolean isEnabled(String name, Level level) {
        return true;
    }
}
