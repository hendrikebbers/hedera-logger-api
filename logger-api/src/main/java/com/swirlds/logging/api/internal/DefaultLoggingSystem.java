package com.swirlds.logging.api.internal;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogListener;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;
import com.swirlds.logging.api.internal.configuration.LogConfiguration;
import com.swirlds.logging.api.internal.util.EmergencyLogger;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DefaultLoggingSystem {

    private final static System.Logger LOGGER = EmergencyLogger.getInstance();

    private static class InstanceHolder {
        private static final DefaultLoggingSystem INSTANCE = new DefaultLoggingSystem();
    }

    private final static AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    private final LoggingSystem internalLoggingSystem;

    private DefaultLoggingSystem() {
        final Configuration configuration = new LogConfiguration();
        this.internalLoggingSystem = new LoggingSystem(configuration);
        installHandlers(configuration);
        installProviders(configuration);

        //TODO:  EmergencyLogger.setInnerLogger();

        EmergencyLogger.getInstance()
                .publishLoggedEvents()
                .stream()
                .map(event -> LogEvent.createCopyWithDifferentName(event, "EMERGENCY-LOGGER-QUEUE"))
                .forEach(internalLoggingSystem::accept);
        INITIALIZED.set(true);
    }

    private void installHandlers(final Configuration configuration) {
        final ServiceLoader<LogHandlerFactory> serviceLoader = ServiceLoader.load(LogHandlerFactory.class);
        final List<LogHandler> handlers = serviceLoader.stream()
                .map(Provider::get)
                .map(factory -> factory.apply(configuration))
                .filter(handler -> handler.isActive())
                .collect(Collectors.toList());
        handlers.forEach(h -> internalLoggingSystem.addHandler(h));
        LOGGER.log(Level.DEBUG, handlers.size() + " logging handlers installed: " + handlers);
    }

    private void installProviders(final Configuration configuration) {
        final ServiceLoader<LogProviderFactory> serviceLoader = ServiceLoader.load(LogProviderFactory.class);
        final List<LogProvider> providers = serviceLoader.stream()
                .map(ServiceLoader.Provider::get)
                .map(factory -> factory.apply(configuration))
                .filter(adapter -> adapter.isActive())
                .collect(Collectors.toList());
        providers.forEach(p -> p.install(internalLoggingSystem));
        LOGGER.log(Level.DEBUG, providers.size() + " logging providers installed: " + providers);
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
