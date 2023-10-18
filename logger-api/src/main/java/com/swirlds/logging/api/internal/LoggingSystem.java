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

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.emergency.EmergencyLogger;
import com.swirlds.logging.api.extensions.emergency.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import com.swirlds.logging.api.extensions.event.LogEventFactory;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import com.swirlds.logging.api.internal.event.SimpleLogEventFactory;
import com.swirlds.logging.api.internal.level.LoggingLevelConfig;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * The implementation of the logging system.
 */
public class LoggingSystem implements LogEventConsumer {

    /**
     * The emergency logger that is used to log errors that occur during the logging process.
     */
    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    /**
     * The name of the root logger.
     */
    public static final String ROOT_LOGGER_NAME = "";

    /**
     * The handlers of the logging system.
     */
    private final List<LogHandler> handlers;

    /**
     * The already created loggers of the logging system.
     */
    private final Map<String, LoggerImpl> loggers;

    /**
     * The level configuration of the logging system that checks if a specific logger is enabled for a specific level.
     */
    private final LoggingLevelConfig levelConfig;

    /**
     * The factory that is used to create log events.
     */
    private final LogEventFactory logEventFactory = new SimpleLogEventFactory();

    /**
     * Creates a new logging system.
     *
     * @param configuration the configuration of the logging system
     */
    public LoggingSystem(@NonNull final Configuration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        this.levelConfig = new LoggingLevelConfig(configuration);
        this.loggers = new ConcurrentHashMap<>();
        this.handlers = new CopyOnWriteArrayList<>();
    }

    /**
     * Adds a new handler to the logging system.
     *
     * @param handler the handler to add
     */
    public void addHandler(@NonNull final LogHandler handler) {
        if (handler == null) {
            EMERGENCY_LOGGER.logNPE("handler");
        } else {
            handlers.add(handler);
        }
    }

    /**
     * Removes a handler from the logging system.
     *
     * @param handler the handler to remove
     */
    public void removeHandler(@NonNull final LogHandler handler) {
        if (handler == null) {
            EMERGENCY_LOGGER.logNPE("handler");
        } else {
            handlers.remove(handler);
        }
    }

    /**
     * Returns the logger with the given name.
     *
     * @param name the name of the logger
     * @return the logger with the given name
     */
    @NonNull
    public LoggerImpl getLogger(@NonNull final String name) {
        if (name == null) {
            EMERGENCY_LOGGER.logNPE("name");
            return loggers.computeIfAbsent(ROOT_LOGGER_NAME, n -> new LoggerImpl(n, logEventFactory, this));
        }
        return loggers.computeIfAbsent(name.trim(), n -> new LoggerImpl(n, logEventFactory, this));
    }

    /**
     * Checks if the logger with the given name is enabled for the given level.
     *
     * @param name  the name of the logger
     * @param level the level to check
     * @return true, if the logger with the given name is enabled for the given level, otherwise false
     */
    public boolean isEnabled(@NonNull final String name, @NonNull final Level level) {
        if (name == null) {
            EMERGENCY_LOGGER.logNPE("name");
            return isEnabled(ROOT_LOGGER_NAME, level);
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
                    handlers.stream()
                            .filter(handler -> handler.isEnabled(event.loggerName(), event.level()))
                            .forEach(eventConsumers::add);

                    if (eventConsumers.isEmpty()) {
                        if (isEnabled(event.loggerName(), event.level())) {
                            eventConsumers.add(e -> EMERGENCY_LOGGER.log(event));
                        }
                    }
                    if (!eventConsumers.isEmpty()) {
                        eventConsumers.forEach(consumer -> {
                            try {
                                consumer.accept(event);
                            } catch (final Throwable throwable) {
                                EMERGENCY_LOGGER.log(Level.ERROR, "Exception in handling log event by consumer",
                                        throwable);
                            }
                        });
                    }
                } catch (final Throwable throwable) {
                    EMERGENCY_LOGGER.log(Level.ERROR, "Exception in handling log event", throwable);
                }
            }
        }
    }

    /**
     * Stops and finalizes the logging system.
     */
    public void stopAndFinalize() {
        handlers.forEach(LogHandler::stopAndFinalize);
    }

    /**
     * Returns the log event factory of the logging system.
     *
     * @return the log event factory of the logging system
     */
    public LogEventFactory getLogEventFactory() {
        return logEventFactory;
    }
}
