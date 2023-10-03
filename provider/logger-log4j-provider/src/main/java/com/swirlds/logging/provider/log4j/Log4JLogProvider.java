package com.swirlds.logging.provider.log4j;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import com.swirlds.logging.api.extensions.provider.AbstractLogProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Log4JLogProvider extends AbstractLogProvider {

    private static final AtomicReference<LogEventConsumer> logEventConsumer = new AtomicReference<>();

    /**
     * Creates a new log provider.
     *
     * @param configuration the configuration
     */
    public Log4JLogProvider(@NonNull final Configuration configuration) {
        super("log4j", configuration);
    }

    public static LogEventConsumer getLogEventConsumer() {
        return logEventConsumer.get();
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
