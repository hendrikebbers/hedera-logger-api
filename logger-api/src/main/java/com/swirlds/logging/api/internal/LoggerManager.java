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

import static java.lang.System.Logger.Level.TRACE;

import com.swirlds.base.context.internal.GlobalContext;
import com.swirlds.base.context.internal.ThreadLocalContext;
import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.LogAdapter;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogHandler;
import com.swirlds.logging.api.extensions.LogHandlerFactory;
import com.swirlds.logging.api.extensions.LogListener;
import edu.umd.cs.findbugs.annotations.NonNull;
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

public class LoggerManager {

    private final static System.Logger LOGGER = System.getLogger(LoggerManager.class.getName());

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

        ServiceLoader<LogHandlerFactory> handlerServiceLoader = ServiceLoader.load(LogHandlerFactory.class);
        handlerServiceLoader.stream()
                .map(ServiceLoader.Provider::get)
                .map(factory -> factory.create(configuration))
                .filter(handler -> handler.isActive())
                .forEach(handlers::add);

        ServiceLoader<LogAdapter> adapterServiceLoader = ServiceLoader.load(LogAdapter.class);
        List<LogAdapter> adapters = adapterServiceLoader.stream()
                .map(ServiceLoader.Provider::get)
                .filter(adapter -> adapter.isActive(configuration))
                .collect(Collectors.toList());

        adapters.forEach(adapter -> adapter.install());

        final String handlerMessage = "LoggerManager initialized with " + handlers.size()
                + " handlers: " + handlers;
        final String adapterMessage = "LoggerManager installed with " + adapters.size()
                + " adapters: " + adapters;
        LOGGER.log(TRACE, handlerMessage);
        LOGGER.log(TRACE, adapterMessage);
        handlers.forEach(handler -> {
            final LocalDateTime time = LocalDateTime.now();
            final String threadName = Thread.currentThread().getName();
            handler.onLogEvent(new LogEvent(handlerMessage, time, threadName, "", Level.DEBUG, null, Map.of(), null));
            handler.onLogEvent(new LogEvent(adapterMessage, time, threadName, "", Level.DEBUG, null, Map.of(), null));
        });
        LOGGER.log(TRACE, "LoggerManager initialization ends");
    }

    @NonNull
    public LoggerImpl getLogger(@NonNull String name) {
        Objects.requireNonNull(name, "name must not be null");
        return loggers.computeIfAbsent(name, n -> new LoggerImpl(n, this));
    }

    @NonNull
    public Marker getMarker(@NonNull String name) {
        return new MarkerImpl(name);
    }

    public boolean isEnabled(@NonNull String name, @NonNull Level level) {
        if (handlers.isEmpty()) {
            return levelConfig.isEnabled(name, level);
        } else {
            boolean match = handlers.stream()
                    .anyMatch(handler -> handler.isEnabled(name, level));
            if (handlers.isEmpty() || match) {
                return levelConfig.isEnabled(name, level);
            }
        }
        return false;
    }

    public void onLogEvent(@NonNull LogEvent event) {
        Objects.requireNonNull(event, "event must not be null");
        LogEvent enrichedEvent = null;
        for (LogHandler handler : handlers) {
            if (handler.isEnabled(event.loggerName(), event.level())) {
                if (enrichedEvent == null) {
                    Map<String, String> context = new HashMap<>(event.context());
                    context.putAll(GlobalContext.getContextMap());
                    context.putAll(ThreadLocalContext.getContextMap());
                    enrichedEvent = LogEvent.createCopyWithDifferentContext(event,
                            Collections.unmodifiableMap(context));
                }
                handler.onLogEvent(enrichedEvent);
            }
        }
        if (hasListeners.get()) {
            if (enrichedEvent == null) {
                Map<String, String> context = new HashMap<>(event.context());
                context.putAll(GlobalContext.getContextMap());
                context.putAll(ThreadLocalContext.getContextMap());
                enrichedEvent = LogEvent.createCopyWithDifferentContext(event, Collections.unmodifiableMap(context));
            }
            for (LogListener listener : listeners) {
                if (enrichedEvent.loggerName().startsWith(listener.getLoggerName())) {
                    listener.onLogEvent(enrichedEvent);
                }
            }
        }
    }

    public void addListener(@NonNull LogListener listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        listeners.add(listener);
        hasListeners.set(true);
    }

    public void removeListener(@NonNull LogListener listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        listeners.remove(listener);
        hasListeners.set(!listeners.isEmpty());
    }

}
