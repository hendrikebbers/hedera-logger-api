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

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.TRACE;

import com.swirlds.base.context.internal.GlobalContext;
import com.swirlds.base.context.internal.ThreadLocalContext;
import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import com.swirlds.logging.api.extensions.LogListener;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import com.swirlds.logging.api.internal.util.EmergencyLogger;
import com.swirlds.logging.api.internal.util.LoggingLevelConfig;
import com.swirlds.logging.api.internal.util.MarkerImpl;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class LoggingSystem implements LogEventConsumer {

    private final static System.Logger LOGGER = EmergencyLogger.getInstance();

    public static final String UNDEFINED = "UNDEFINED";

    private final List<LogHandler> handlers;

    private final Map<String, LoggerImpl> loggers;

    private final List<LogListener> listeners;

    private final LoggingLevelConfig levelConfig;

    public LoggingSystem(@NonNull final Configuration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        LOGGER.log(TRACE, "Logging system initialization");

        this.levelConfig = new LoggingLevelConfig(configuration);
        this.loggers = new ConcurrentHashMap<>();
        this.handlers = new CopyOnWriteArrayList<>();
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void addHandler(@NonNull final LogHandler handler) {
        if (handler == null) {
            final Class<?> callerClass = StackWalker.getInstance().getCallerClass();
            LOGGER.log(ERROR, "null handler added in '" + callerClass + "'");
        } else {
            handlers.add(handler);
        }
    }

    @NonNull
    public LoggerImpl getLogger(@NonNull final String name) {
        if (name == null) {
            final Class<?> callerClass = StackWalker.getInstance().getCallerClass();
            LOGGER.log(ERROR, "Logger without name created in '" + callerClass + "'");
            return loggers.computeIfAbsent(UNDEFINED, n -> new LoggerImpl(n, this));
        }
        return loggers.computeIfAbsent(name, n -> new LoggerImpl(n, this));
    }

    @NonNull
    public Marker getMarker(@NonNull final String name) {
        if (name == null) {
            final Class<?> callerClass = StackWalker.getInstance().getCallerClass();
            LOGGER.log(ERROR, "Marker without name created in '" + callerClass + "'");
            return new MarkerImpl(UNDEFINED);
        }
        return new MarkerImpl(name);
    }

    public boolean isEnabled(@NonNull final String name, @NonNull final Level level) {
        if (name == null) {
            final Class<?> callerClass = StackWalker.getInstance().getCallerClass();
            LOGGER.log(ERROR, "level check without name called in '" + callerClass + "'");
            return true;
        }
        if (level == null) {
            final Class<?> callerClass = StackWalker.getInstance().getCallerClass();
            LOGGER.log(ERROR, "level check without name called in '" + callerClass + "'");
            return true;
        }
        if (handlers.isEmpty()) {
            return levelConfig.isEnabled(name, level);
        } else {
            final boolean match = handlers.stream()
                    .anyMatch(handler -> handler.isEnabled(name, level));
            if (handlers.isEmpty() || match) {
                return levelConfig.isEnabled(name, level);
            }
        }
        return false;
    }

    @Override
    public void accept(@NonNull final LogEvent event) {
        if (event == null) {
            final Class<?> callerClass = StackWalker.getInstance().getCallerClass();
            LOGGER.log(ERROR, "event is null in '" + callerClass + "'");
        } else {
            try {
                final List<Consumer<LogEvent>> eventConsumers = new ArrayList<>();
                handlers.stream()
                        .filter(handler -> handler.isEnabled(event.loggerName(), event.level()))
                        .forEach(handler -> eventConsumers.add(handler));
                listeners.stream()
                        .filter(listener -> event.loggerName().startsWith(listener.getLoggerName()))
                        .forEach(listener -> eventConsumers.add(listener));
                if (!eventConsumers.isEmpty()) {
                    final Map<String, String> context = new HashMap<>(event.context());
                    context.putAll(GlobalContext.getContextMap());
                    context.putAll(ThreadLocalContext.getContextMap());
                    final LogEvent enrichedEvent = LogEvent.createCopyWithDifferentContext(event,
                            Collections.unmodifiableMap(context));
                    eventConsumers.forEach(consumer -> consumer.accept(enrichedEvent));
                }
            } catch (final Throwable throwable) {
                LOGGER.log(ERROR, "Exception in handling log event", throwable);
            }
        }
    }

    public void addListener(@NonNull final LogListener listener) {
        if (listener == null) {
            final Class<?> callerClass = StackWalker.getInstance().getCallerClass();
            LOGGER.log(ERROR, "listener is null in '" + callerClass + "'");
        } else {
            listeners.add(listener);
            LOGGER.log(Logger.Level.DEBUG,
                    "Logging Listener added! This should only be done in unit tests since it can slow down the system.");
        }
    }

    public void removeListener(@NonNull final LogListener listener) {
        if (listener == null) {
            final Class<?> callerClass = StackWalker.getInstance().getCallerClass();
            LOGGER.log(ERROR, "listener is null in '" + callerClass + "'");
        } else {
            listeners.remove(listener);
        }
    }
}
