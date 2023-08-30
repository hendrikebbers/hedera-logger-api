package com.swirlds.logging.api.internal;

import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.LogListener;
import com.swirlds.logging.api.internal.configuration.LogConfiguration;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultLoggerSystem {

    private static class InstanceHolder {
        private static final DefaultLoggerSystem INSTANCE = new DefaultLoggerSystem();
    }

    private final static AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    private final LoggerManager loggerManager;

    private DefaultLoggerSystem() {
        this.loggerManager = new LoggerManager(new LogConfiguration());
        INITIALIZED.set(true);
    }

    public static DefaultLoggerSystem getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public LoggerManager getLoggerManager() {
        return loggerManager;
    }

    @NonNull
    public Logger getLogger(@NonNull String loggerName) {
        return loggerManager.getLogger(loggerName);
    }

    public void addListener(LogListener listener) {
        loggerManager.addListener(listener);
    }

    public void removeListener(LogListener listener) {
        loggerManager.addListener(listener);
    }

    @NonNull
    public Marker getMarker(@NonNull String name) {
        return loggerManager.getMarker(name);
    }

    public static boolean isInitialized() {
        return INITIALIZED.get();
    }
}
