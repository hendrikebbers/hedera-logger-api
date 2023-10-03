package com.swirlds.logging.provider.system;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import com.swirlds.logging.api.extensions.provider.AbstractLogProvider;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class SystemLoggerProvider extends AbstractLogProvider {

    private static final AtomicReference<LogEventConsumer> logEventConsumer = new AtomicReference<>();

    public SystemLoggerProvider(Configuration configuration) {
        super("system", configuration);
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
