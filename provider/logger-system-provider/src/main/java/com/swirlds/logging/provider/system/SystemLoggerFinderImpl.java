package com.swirlds.logging.provider.system;

import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import java.lang.System.Logger;
import java.lang.System.LoggerFinder;

public class SystemLoggerFinderImpl extends LoggerFinder {

    @Override
    public Logger getLogger(String name, Module module) {
        final LogEventConsumer consumer = SystemLoggerProvider.getLogEventConsumer();
        if (consumer == null) {
            return new SystemEmergencyLogger(name);
        }
        return new SystemLoggerImpl(name, consumer);
    }
}
