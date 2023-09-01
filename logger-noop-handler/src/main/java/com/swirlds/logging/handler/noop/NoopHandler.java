package com.swirlds.logging.handler.noop;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.handler.LogHandler;

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
    public void accept(LogEvent event) {
        event.hashCode();
    }

}
