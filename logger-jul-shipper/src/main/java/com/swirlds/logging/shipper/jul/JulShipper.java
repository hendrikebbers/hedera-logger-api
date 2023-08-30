package com.swirlds.logging.shipper.jul;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import com.swirlds.logging.api.extensions.shipper.LogShipper;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogManager;

public class JulShipper implements LogShipper {

    private final Configuration configuration;

    public JulShipper(Configuration configuration) {
        this.configuration = Objects.requireNonNull(configuration, "configuration must not be null");
    }

    @Override
    public boolean isActive() {
        return configuration.getValue("logging.adapter.jul.enabled", Boolean.class, false);
    }

    @Override
    public String getName() {
        return "Adapter for java.util.logging";
    }

    public void install(LogEventConsumer logEventConsumer) {
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        rootLogger.addHandler(new InternalJulLogForwarder(logEventConsumer));
        rootLogger.setLevel(java.util.logging.Level.ALL);
    }
}
