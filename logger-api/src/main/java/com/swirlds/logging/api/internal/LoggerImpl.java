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
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Loggers;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.internal.format.MessageFormatter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LoggerImpl implements Logger {

    private final String name;

    private final Marker marker;

    private Map<String, String> context;

    private final LoggerManager loggerManager;

    protected LoggerImpl(String name, final Marker marker, Map<String, String> context, LoggerManager loggerManager) {
        this.name = name;
        this.marker = marker;
        this.context = Collections.unmodifiableMap(context);
        this.loggerManager = loggerManager;
    }

    protected LoggerImpl(String name, LoggerManager loggerManager) {
        this(name, null, Map.of(), loggerManager);
    }

    public String getName() {
        return name;
    }

    protected Marker getMarker() {
        return marker;
    }

    protected Map<String, String> getContext() {
        return context;
    }

    @Override
    public void log(Level level, String message) {
        logImpl(level, message, null);
    }


    @Override
    public void log(Level level, String message, Throwable throwable) {
        logImpl(level, message, throwable);
    }

    private String formatMessage(String message, Object... args) {
        return MessageFormatter.arrayFormat(message, args);
    }

    @Override
    public void log(Level level, String message, Object... args) {
        logImpl(level, formatMessage(message, args), null);
    }

    @Override
    public void log(Level level, String message, Object arg) {
        logImpl(level, formatMessage(message, arg), null);
    }

    @Override
    public void log(Level level, String message, Object arg1, Object arg2) {
        logImpl(level, formatMessage(message, arg1, arg2), null);
    }

    @Override
    public void log(Level level, String message, Throwable throwable, Object... args) {
        logImpl(level, formatMessage(message, args), throwable);
    }

    @Override
    public void log(Level level, String message, Throwable throwable, Object arg1) {
        logImpl(level, formatMessage(message, arg1), throwable);
    }

    @Override
    public void log(Level level, String message, Throwable throwable, Object arg1, Object arg2) {
        logImpl(level, formatMessage(message, arg1, arg2), throwable);
    }

    protected Logger withMarkerAndContext(final Marker marker, final Map<String, String> context) {
        return new LoggerImpl(getName(), marker, context, loggerManager);
    }

    @Override
    public Logger withMarker(String markerName) {
        return withMarkerAndContext(Loggers.getMarker(markerName), context);
    }

    @Override
    public Logger withContext(String key, String value) {
        Map<String, String> newContext = new HashMap<>(context);
        newContext.put(key, value);
        return withMarkerAndContext(marker, newContext);
    }

    @Override
    public Logger withContext(String key, String... values) {
        Map<String, String> newContext = new HashMap<>(context);
        newContext.put(key, String.join(",", values));
        return withMarkerAndContext(marker, newContext);
    }

    @Override
    public boolean isEnabled(Level level) {
        return loggerManager.isEnabled(getName(), level);
    }

    public void logImpl(Level level, String message, final Throwable throwable) {
        if (isEnabled(level)) {
            String threadName = Thread.currentThread().getName();
            Marker marker = getMarker();
            LogEvent logEvent = new LogEvent(message, LocalDateTime.now(), threadName, getName(), level, marker,
                    getContext(), throwable);
            loggerManager.onLogEvent(logEvent);
        }
    }
}
