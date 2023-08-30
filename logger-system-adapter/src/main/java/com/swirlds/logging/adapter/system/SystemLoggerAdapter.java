package com.swirlds.logging.adapter.system;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class SystemLoggerAdapter implements LogProvider {

    private static AtomicReference<LogEventConsumer> logEventConsumer = new AtomicReference<>();

    @Override
    public boolean isActive(Configuration configuration) {
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
