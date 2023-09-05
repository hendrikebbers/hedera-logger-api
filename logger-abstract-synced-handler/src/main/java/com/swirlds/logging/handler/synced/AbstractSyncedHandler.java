package com.swirlds.logging.handler.synced;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.EmergencyLogger;
import com.swirlds.logging.api.extensions.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractSyncedHandler implements LogHandler {

    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    private final String configKey;

    private final Configuration configuration;

    private final Lock writeLock = new ReentrantLock();

    private volatile boolean stopped = false;

    public AbstractSyncedHandler(String configKey, Configuration configuration) {
        this.configKey = configKey;
        this.configuration = configuration;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isActive() {
        return configuration.getValue("logging.handler." + configKey + ".enabled", Boolean.class, false);
    }

    @Override
    public final void accept(LogEvent event) {
        try {
            writeLock.lock();
            if (stopped) {
                EMERGENCY_LOGGER.log(event);
            } else {
                handleSynced(event);
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected abstract void handleSynced(LogEvent event);

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

    protected void handleStopAndFinalize() {}
}
