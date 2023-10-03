package com.swirlds.logging.api.extensions.handler;

import com.swirlds.config.api.Configuration;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * A factory that creates {@link LogHandler}s. The factory is used by the Java SPI to create log handlers.
 *
 * @see LogHandler
 * @see java.util.ServiceLoader
 */
public interface LogHandlerFactory {

    /**
     * Creates a new log handler.
     *
     * @param configuration the configuration
     * @return the log handler
     */
    @NonNull
    LogHandler create(@NonNull Configuration configuration);
}
