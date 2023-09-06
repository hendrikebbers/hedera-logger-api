package com.swirlds.logging.provider.system;

import com.swirlds.logging.api.extensions.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;

public class SystemEmergencyLogger implements System.Logger {
    
    private final String name;


    public SystemEmergencyLogger(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLoggable(Level level) {
        Objects.requireNonNull(level, "level must not be null");
        final LogEventConsumer logEventConsumer = SystemLoggerProvider.getLogEventConsumer();
        if (logEventConsumer != null) {
            return logEventConsumer.isEnabled(name, SystemLoggerConverterUtils.convertFromSystemLogger(level));
        }
        return EmergencyLoggerProvider.getEmergencyLogger().isLoggable(level);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        if (isLoggable(Objects.requireNonNull(level, "level must not be null"))) {
            final String message;
            if (bundle != null && bundle.containsKey(msg)) {
                message = bundle.getString(msg);
            } else {
                message = msg;
            }
            final LogEventConsumer logEventConsumer = SystemLoggerProvider.getLogEventConsumer();
            if (logEventConsumer != null) {
                logEventConsumer.accept(
                        new LogEvent(message, name, SystemLoggerConverterUtils.convertFromSystemLogger(level), thrown));
            } else {
                EmergencyLoggerProvider.getEmergencyLogger().log(level, bundle, msg, thrown);
            }
        }
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        if (isLoggable(level)) {
            if (bundle != null && bundle.containsKey(format)) {
                final String translated = bundle.getString(format);
                final String message = MessageFormat.format(translated, params);
                log(level, EmptyResourceBundle.getInstance(), message, (Throwable) null);
            } else {
                final String message = MessageFormat.format(format, params);
                log(level, EmptyResourceBundle.getInstance(), message, (Throwable) null);
            }
        }
    }
}
