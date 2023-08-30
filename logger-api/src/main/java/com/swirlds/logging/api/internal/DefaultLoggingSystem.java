package com.swirlds.logging.api.internal;

import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.LogListener;
import com.swirlds.logging.api.internal.configuration.LogConfiguration;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultLoggingSystem {

    private static class InstanceHolder {
        private static final DefaultLoggingSystem INSTANCE = new DefaultLoggingSystem();
    }

    private final static AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    private final LoggingSystem internalLoggingSystem;

    private DefaultLoggingSystem() {
        this.internalLoggingSystem = new LoggingSystem(new LogConfiguration());
        INITIALIZED.set(true);
    }

    public static DefaultLoggingSystem getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @NonNull
    public Logger getLogger(@NonNull String loggerName) {
        return internalLoggingSystem.getLogger(loggerName);
    }

    public void addListener(LogListener listener) {
        internalLoggingSystem.addListener(listener);
    }

    public void removeListener(LogListener listener) {
        internalLoggingSystem.addListener(listener);
    }

    @NonNull
    public Marker getMarker(@NonNull String name) {
        return internalLoggingSystem.getMarker(name);
    }

    public static boolean isInitialized() {
        return INITIALIZED.get();
    }
}
