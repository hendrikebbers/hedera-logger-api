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
import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;
import com.swirlds.logging.api.extensions.shipper.LogShipper;
import com.swirlds.logging.api.extensions.shipper.LogShipperFactory;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.lang.System.Logger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class LoggerManager implements LogEventConsumer {

    private final static System.Logger LOGGER = EmergencyLogger.getInstance();
    public static final String UNDEFINED = "UNDEFINED";

    private final List<LogHandler> handlers;

    private final Map<String, LoggerImpl> loggers;

    private final List<LogListener> listeners;

    private final AtomicBoolean hasListeners;

    private final LoggingLevelConfig levelConfig;

    public LoggerManager(@NonNull final Configuration configuration) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        LOGGER.log(TRACE, "LoggerManager initialization starts");

        this.levelConfig = new LoggingLevelConfig(configuration);
        this.loggers = new ConcurrentHashMap<>();
        this.handlers = new CopyOnWriteArrayList<>();
        this.listeners = new CopyOnWriteArrayList<>();
        this.hasListeners = new AtomicBoolean(false);

        final ServiceLoader<LogHandlerFactory> handlerServiceLoader = ServiceLoader.load(LogHandlerFactory.class);
        handlerServiceLoader.stream()
                .map(ServiceLoader.Provider::get)
                .map(factory -> factory.apply(configuration))
                .filter(handler -> handler.isActive())
                .forEach(handlers::add);

        final ServiceLoader<LogShipperFactory> adapterServiceLoader = ServiceLoader.load(LogShipperFactory.class);
        final List<LogShipper> providers = adapterServiceLoader.stream()
                .map(ServiceLoader.Provider::get)
                .map(factory -> factory.apply(configuration))
                .filter(adapter -> adapter.isActive())
                .collect(Collectors.toList());
        providers.forEach(adapter -> adapter.install(this));

        final String handlerMessage = "LoggerManager initialized with " + handlers.size()
                + " handlers: " + handlers;
        final String adapterMessage = "LoggerManager installed with " + providers.size()
                + " providers: " + providers;
        LOGGER.log(TRACE, handlerMessage);
        LOGGER.log(TRACE, adapterMessage);
        handlers.forEach(handler -> {
            final LocalDateTime time = LocalDateTime.now();
            final String threadName = Thread.currentThread().getName();
            handler.accept(new LogEvent(handlerMessage, time, threadName, "", Level.DEBUG, null, Map.of(), null));
            handler.accept(new LogEvent(adapterMessage, time, threadName, "", Level.DEBUG, null, Map.of(), null));
        });
        LOGGER.log(TRACE, "LoggerManager initialization ends");
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
                LogEvent enrichedEvent = null;
                for (final LogHandler handler : handlers) {
                    if (handler.isEnabled(event.loggerName(), event.level())) {
                        if (enrichedEvent == null) {
                            final Map<String, String> context = new HashMap<>(event.context());
                            context.putAll(GlobalContext.getContextMap());
                            context.putAll(ThreadLocalContext.getContextMap());
                            enrichedEvent = LogEvent.createCopyWithDifferentContext(event,
                                    Collections.unmodifiableMap(context));
                        }
                        handler.accept(enrichedEvent);
                    }
                }
                if (hasListeners.get()) {
                    if (enrichedEvent == null) {
                        final Map<String, String> context = new HashMap<>(event.context());
                        context.putAll(GlobalContext.getContextMap());
                        context.putAll(ThreadLocalContext.getContextMap());
                        enrichedEvent = LogEvent.createCopyWithDifferentContext(event,
                                Collections.unmodifiableMap(context));
                    }
                    for (final LogListener listener : listeners) {
                        if (enrichedEvent.loggerName().startsWith(listener.getLoggerName())) {
                            listener.accept(enrichedEvent);
                        }
                    }
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
            hasListeners.set(true);
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
            hasListeners.set(!listeners.isEmpty());
        }
    }
}
