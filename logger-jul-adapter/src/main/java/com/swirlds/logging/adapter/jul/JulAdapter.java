package com.swirlds.logging.adapter.jul;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import java.util.logging.Handler;
import java.util.logging.LogManager;

public class JulAdapter implements LogProvider {

    @Override
    public boolean isActive(Configuration configuration) {
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
        rootLogger.addHandler(new JulHandler(logEventConsumer));
        rootLogger.setLevel(java.util.logging.Level.ALL);
    }
}
