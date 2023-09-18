package com.swirlds.logging.provider.log4j;

import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Log4JLogProvider implements LogProvider {

    private static final AtomicReference<LogEventConsumer> logEventConsumer = new AtomicReference<>();

    public static LogEventConsumer getLogEventConsumer() {
        return logEventConsumer.get();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getName() {
        return "Log4J Provider";
    }

    @Override
    public void install(LogEventConsumer consumer) {
        Objects.requireNonNull(consumer, "consumer must not be null!");
        if (logEventConsumer.get() != null) {
            throw new IllegalArgumentException("consumer cannot only be set once!");
        }
        logEventConsumer.set(consumer);
    }
}
