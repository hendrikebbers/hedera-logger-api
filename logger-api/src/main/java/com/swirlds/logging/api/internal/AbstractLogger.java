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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public abstract class AbstractLogger implements Logger {

    private final String name;

    private final Marker marker;

    private Map<String, String> context;

    private List<LoggerListener> listeners = new CopyOnWriteArrayList<>();

    protected AbstractLogger(String name, final Marker marker, Map<String, String> context) {
        this.name = name;
        this.marker = marker;
        this.context = new ConcurrentHashMap<>(context);
    }

    protected AbstractLogger(String name) {
        this(name, null, Map.of());
    }

    public void addListener(LoggerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(LoggerListener listener) {
        listeners.remove(listener);
    }

    protected void notifyListeners(LogEvent event) {
        listeners.forEach(listener -> listener.onLogEvent(event));
    }

    protected String getName() {
        return name;
    }

    protected Marker getMarker() {
        return marker;
    }

    protected Map<String, String> getContext() {
        return context;
    }

    protected abstract void logImpl(Level level, String message, final Throwable throwable);

    @Override
    public void log(Level level, String message) {
        logImpl(level, message, null);
    }

    @Override
    public void log(Level level, Supplier<String> messageSupplier) {
        logImpl(level, messageSupplier.get(), null);
    }

    @Override
    public void log(Level level, String message, Throwable throwable) {
        logImpl(level, message, throwable);
    }

    @Override
    public void log(Level level, Supplier<String> messageSupplier, Throwable throwable) {
        logImpl(level, messageSupplier.get(), throwable);
    }

    private String formatMessage(String message, Object... args) {
        for (Object arg : args) {
            message = message.replaceFirst("\\{\\}", arg.toString());
        }
        return message;
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

    protected abstract Logger withMarkerAndContext(final Marker marker, final Map<String, String> context);

    @Override
    public Logger withMarker(String markerName) {
        return withMarkerAndContext(Loggers.getMarker(markerName), context);
    }

    @Override
    public Logger withContext(String key, String value) {
        Map<String, String> newContext = new ConcurrentHashMap<>(context);
        newContext.put(key, value);
        return withMarkerAndContext(marker, newContext);
    }

    @Override
    public Logger withContext(String key, String... values) {
        Map<String, String> newContext = new ConcurrentHashMap<>(context);
        newContext.put(key, String.join(",", values));
        return withMarkerAndContext(marker, newContext);
    }

    @Override
    public Logger withContext(String key, Supplier<String> value) {
        return withContext(key, value.get());
    }

    @Override
    public boolean isEnabled(Level level) {
        return LoggerManager.getInstance().isEnabled(getName(), level);
    }
}
