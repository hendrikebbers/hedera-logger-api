package com.swirlds.logging.api.extensions.handler;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.emergency.EmergencyLogger;
import com.swirlds.logging.api.extensions.emergency.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.event.LogEvent;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An abstract log handler that synchronizes the handling of log events. This handler is used as a base class for all
 * log handlers that need to synchronize the handling of log events like simple handlers that write events to the
 * console or to a file.
 */
public abstract class AbstractSyncedHandler extends AbstractLogHandler {

    /**
     * The emergency logger that is used if the log handler is stopped.
     */
    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    /**
     * The write lock that is used to synchronize the handling of log events.
     */
    private final Lock writeLock = new ReentrantLock();

    /**
     * True if the log handler is stopped, false otherwise.
     */
    private volatile boolean stopped = false;

    /**
     * Creates a new log handler.
     *
     * @param configKey     the configuration key
     * @param configuration the configuration
     */
    public AbstractSyncedHandler(@NonNull final String configKey, @NonNull final Configuration configuration) {
        super(configKey, configuration);
    }

    @Override
    public final void accept(@NonNull LogEvent event) {
        try {
            writeLock.lock();
            if (stopped) {
                //TODO: is the emergency logger really the best idea in that case? If multiple handlers are stopped,
                // the emergency logger will be called multiple times.
                EMERGENCY_LOGGER.log(event);
            } else {
                handleEvent(event);
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Handles the log event synchronously.
     *
     * @param event the log event
     */
    protected abstract void handleEvent(@NonNull LogEvent event);

    @Override
    public final void stopAndFinalize() {
        try {
            writeLock.lock();
            stopped = true;
            handleStopAndFinalize();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Implementations can override this method to handle the stop and finalize of the handler. The method will be
     * called synchronously to the handling of log events.
     */
    protected void handleStopAndFinalize() {}
}
