package com.swirlds.logging.adapter.jul;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogAdapter;
import java.util.logging.Handler;
import java.util.logging.LogManager;

public class JulAdapter implements LogAdapter {

    @Override
    public boolean isActive(Configuration configuration) {
        return configuration.getValue("logging.adapter.jul.enabled", Boolean.class, false);
    }

    @Override
    public String getName() {
        return "Adapter for java.util.logging";
    }

    public void install() {
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        rootLogger.addHandler(new JulHandler());
        rootLogger.setLevel(java.util.logging.Level.ALL);
    }
}
