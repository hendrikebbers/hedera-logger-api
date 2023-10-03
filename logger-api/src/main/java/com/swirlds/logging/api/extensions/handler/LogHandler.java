package com.swirlds.logging.api.extensions.handler;

import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * A log handler that handles log events. A log handler can be used to write log events to a file, send them to a remote
 * server, or do any other kind of processing. A log handler is created by a {@link LogHandlerFactory} that use the Java
 * SPI.
 *
 * @see LogHandlerFactory
 */
public interface LogHandler extends LogEventConsumer {

    /**
     * Returns the name of the log handler.
     *
     * @return the name of the log handler
     */
    @NonNull
    default String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Returns true if the log handler is active, false otherwise. If the log handler is not active, it will not be
     * used. This can be used to disable a log handler without removing it from the configuration. The current logging
     * implementation checks that state at startup and not for every log event.
     *
     * @return true if the log handler is active, false otherwise
     */
    boolean isActive();

    /**
     * Calling that method will stop the log handler and finalize it. This can be used to close files or flush streams.
     */
    default void stopAndFinalize() {

    }

}
