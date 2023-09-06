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
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.EmergencyLogger;
import com.swirlds.logging.api.extensions.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import com.swirlds.logging.api.internal.format.MessageFormatter;
import com.swirlds.logging.api.internal.util.SystemLoggerConverterUtils;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LoggerImpl implements Logger {

    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    private final String name;

    private final Marker marker;

    private final Map<String, String> context;

    private final LogEventConsumer logEventConsumer;

    protected LoggerImpl(String name, final Marker marker, Map<String, String> context,
            LogEventConsumer logEventConsumer) {
        if (name == null) {
            EMERGENCY_LOGGER.logNPE("name");
            this.name = "";
        } else {
            this.name = name;
        }
        this.marker = marker;
        this.context = Collections.unmodifiableMap(context);
        if (logEventConsumer == null) {
            EMERGENCY_LOGGER.logNPE("logEventConsumer");
            this.logEventConsumer = new LogEventConsumer() {
                @Override
                public void accept(LogEvent event) {
                    EMERGENCY_LOGGER.log(SystemLoggerConverterUtils.convertToSystemLogger(event.level()),
                            event.message(),
                            event.throwable());
                }

                @Override
                public boolean isEnabled(String name, Level level) {
                    return EMERGENCY_LOGGER.isLoggable(SystemLoggerConverterUtils.convertToSystemLogger(level));
                }
            };
        } else {
            this.logEventConsumer = logEventConsumer;
        }
    }

    public LoggerImpl(String name, LogEventConsumer logEventConsumer) {
        this(name, null, Map.of(), logEventConsumer);
    }

    public String getName() {
        return name;
    }

    private Marker getMarker() {
        return marker;
    }

    private Map<String, String> getContext() {
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
        return new LoggerImpl(getName(), marker, context, logEventConsumer);
    }

    @Override
    public Logger withMarker(String markerName) {
        if (markerName == null) {
            return this;
        } else {
            return withMarkerAndContext(new Marker(markerName, marker), context);
        }
    }

    @Override
    public Logger withContext(String key, String value) {
        if (key != null) {
            Map<String, String> newContext = new HashMap<>(context);
            newContext.put(key, value);
            return withMarkerAndContext(marker, newContext);
        } else {
            return this;
        }
    }

    @Override
    public Logger withContext(String key, String... values) {
        if (key != null) {
            Map<String, String> newContext = new HashMap<>(context);
            newContext.put(key, String.join(",", values));
            return withMarkerAndContext(marker, newContext);
        } else {
            return this;
        }
    }

    @Override
    public boolean isEnabled(Level level) {
        return logEventConsumer.isEnabled(getName(), level);
    }

    public void logImpl(Level level, String message, final Throwable throwable) {
        if (isEnabled(level)) {
            String threadName = Thread.currentThread().getName();
            Marker marker = getMarker();
            LogEvent logEvent = new LogEvent(message, Instant.now(), threadName, getName(), level,
                    marker,
                    getContext(), throwable);
            logEventConsumer.accept(logEvent);
        }
    }
}
