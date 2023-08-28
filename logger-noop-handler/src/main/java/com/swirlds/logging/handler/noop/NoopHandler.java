package com.swirlds.logging.handler.noop;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogHandler;

public class NoopHandler implements LogHandler {

    private final Configuration configuration;

    public NoopHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getName() {
        return "Noop Handler";
    }

    @Override
    public boolean isActive() {
        return configuration.getValue("logging.handler.noop.enabled", Boolean.class, false);
    }

    @Override
    public void onLogEvent(LogEvent event) {
        event.hashCode();
    }

    @Override
    public boolean isEnabled(String name, Level level) {
        return true;
    }
}
