package com.swirlds.logging.api.extensions;

import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.internal.LoggerManager;
import com.swirlds.logging.api.internal.configuration.LogConfiguration;
import edu.umd.cs.findbugs.annotations.NonNull;

public class DefaultLoggerSystem {

    private final static DefaultLoggerSystem INSTANCE = new DefaultLoggerSystem();

    private final LoggerManager loggerManager;

    private DefaultLoggerSystem() {
        this.loggerManager = new LoggerManager(new LogConfiguration());
    }

    public static DefaultLoggerSystem getInstance() {
        return INSTANCE;
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
}