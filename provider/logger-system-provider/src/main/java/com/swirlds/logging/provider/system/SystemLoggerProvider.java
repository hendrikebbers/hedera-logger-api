package com.swirlds.logging.provider.system;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class SystemLoggerProvider implements LogProvider {

    private static final AtomicReference<LogEventConsumer> logEventConsumer = new AtomicReference<>();

    private final Configuration configuration;

    public SystemLoggerProvider(Configuration configuration) {
        this.configuration = Objects.requireNonNull(configuration, "configuration must not be null");
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getName() {
        return "Adapter for java.lang.System.Logger";
    }

    @Override
    public void install(LogEventConsumer consumer) {
        Objects.requireNonNull(consumer, "consumer must not be null!");
        if (logEventConsumer.get() != null) {
            throw new IllegalArgumentException("consumer cannot only be set once!");
        }
        logEventConsumer.set(consumer);
    }

    public static LogEventConsumer getLogEventConsumer() {
        return logEventConsumer.get();
    }
}
