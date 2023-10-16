package com.swirlds.logging.provider.jul;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import com.swirlds.logging.api.extensions.event.LogEventFactory;
import com.swirlds.logging.api.extensions.provider.AbstractLogProvider;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogManager;

/**
 * Implementation of {@link LogProvider} for java.util.logging.
 */
public class JulLogProvider extends AbstractLogProvider {

    /**
     * Creates a new instance.
     *
     * @param configuration the configuration
     */
    public JulLogProvider(@NonNull final Configuration configuration) {
        super("jul", configuration);
    }

    @Override
    public void install(@NonNull LogEventFactory logEventFactory, @NonNull final LogEventConsumer logEventConsumer) {
        Objects.requireNonNull(logEventConsumer, "logEventConsumer must not be null");
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        rootLogger.addHandler(new JulInternalLogForwarder(logEventFactory, logEventConsumer));
        rootLogger.setLevel(java.util.logging.Level.ALL);
    }
}
