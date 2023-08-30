package com.swirlds.logging.adapter.system;

import com.swirlds.logging.api.extensions.LogEventConsumer;
import java.lang.System.Logger;
import java.lang.System.LoggerFinder;

public class SystemLoggerFinderImpl extends LoggerFinder {

    @Override
    public Logger getLogger(String name, Module module) {
        final LogEventConsumer consumer = SystemLoggerProvider.getLogEventConsumer();
        if (consumer == null) {
            return new SystemEmergencyLogger(name);
        }
        return new SystemLoggerWrapper(name, consumer);
    }
}
