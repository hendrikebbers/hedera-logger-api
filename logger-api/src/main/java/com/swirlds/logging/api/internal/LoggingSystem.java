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

import com.swirlds.base.context.internal.GlobalContext;
import com.swirlds.base.context.internal.ThreadLocalContext;
import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.EmergencyLogger;
import com.swirlds.logging.api.extensions.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import com.swirlds.logging.api.internal.level.LoggingLevelConfig;
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

    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    private final List<LogHandler> handlers;

    private final Map<String, LoggerImpl> loggers;

    private final List<Consumer<LogEvent>> listeners;

    private final LoggingLevelConfig levelConfig;

    public LoggingSystem(@NonNull final Configuration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        this.levelConfig = new LoggingLevelConfig(configuration);
        this.loggers = new ConcurrentHashMap<>();
        this.handlers = new CopyOnWriteArrayList<>();
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void addHandler(@NonNull final LogHandler handler) {
        if (handler == null) {
            EMERGENCY_LOGGER.logNPE("handler");
        } else {
            handlers.add(handler);
        }
    }

    @NonNull
    public LoggerImpl getLogger(@NonNull final String name) {
        if (name == null) {
            EMERGENCY_LOGGER.logNPE("name");
            return loggers.computeIfAbsent("", n -> new LoggerImpl(n, this));
        }
        return loggers.computeIfAbsent(name.trim(), n -> new LoggerImpl(n, this));
    }

    public boolean isEnabled(@NonNull final String name, @NonNull final Level level) {
        if (name == null) {
            EMERGENCY_LOGGER.logNPE("name");
            return isEnabled("", level);
        }
        if (level == null) {
            EMERGENCY_LOGGER.logNPE("level");
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
            EMERGENCY_LOGGER.logNPE("event");
        } else {
            if (isEnabled(event.loggerName(), event.level())) {
                try {
                    final List<Consumer<LogEvent>> eventConsumers = new ArrayList<>();
                    if (handlers.isEmpty()) {
                        if (isEnabled(event.loggerName(), event.level())) {
                            eventConsumers.add(e -> EMERGENCY_LOGGER.log(event));
                        }
                    } else {
                        handlers.stream()
                                .filter(handler -> handler.isEnabled(event.loggerName(), event.level()))
                                .forEach(eventConsumers::add);
                    }
                    eventConsumers.addAll(listeners);
                    if (!eventConsumers.isEmpty()) {
                        final Map<String, String> context = new HashMap<>(event.context());
                        context.putAll(GlobalContext.getContextMap());
                        context.putAll(ThreadLocalContext.getContextMap());
                        final LogEvent enrichedEvent = LogEvent.createCopyWithDifferentContext(event,
                                Collections.unmodifiableMap(context));
                        eventConsumers.forEach(consumer -> {
                            try {
                                consumer.accept(enrichedEvent);
                            } catch (final Throwable throwable) {
                                EMERGENCY_LOGGER.log(ERROR, "Exception in handling log event by consumer", throwable);
                            }
                        });
                    }
                } catch (final Throwable throwable) {
                    EMERGENCY_LOGGER.log(ERROR, "Exception in handling log event", throwable);
                }
            }
        }
    }

    public void addListener(@NonNull final Consumer<LogEvent> listener) {
        if (listener == null) {
            EMERGENCY_LOGGER.logNPE("listener");
        } else {
            listeners.add(listener);
            EMERGENCY_LOGGER.log(Logger.Level.DEBUG,
                    "Logging Listener added! This should only be done in unit tests since it can slow down the system.");
        }
    }

    public void removeListener(@NonNull final Consumer<LogEvent> listener) {
        if (listener == null) {
            EMERGENCY_LOGGER.logNPE("listener");
        } else {
            listeners.remove(listener);
        }
    }

    public void stopAndFinalize() {
        handlers.forEach(LogHandler::stopAndFinalize);
    }
}
