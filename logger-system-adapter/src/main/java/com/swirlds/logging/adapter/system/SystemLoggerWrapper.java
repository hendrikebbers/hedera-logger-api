package com.swirlds.logging.adapter.system;

import static com.swirlds.logging.adapter.system.SystemLoggerConverter.convertFromSystemLogger;

import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class SystemLoggerWrapper implements System.Logger {

    private final String name;

    private final LogEventConsumer logEventConsumer;

    public SystemLoggerWrapper(final String name, final LogEventConsumer logEventConsumer) {
        this.name = name;
        this.logEventConsumer = logEventConsumer;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLoggable(Level level) {
        if (level == Level.ALL || level == Level.OFF) {
            return false;
        }
        return logEventConsumer.isEnabled(name, convertFromSystemLogger(level));
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        if (isLoggable(level)) {
            com.swirlds.logging.api.Level convertedLevel = convertFromSystemLogger(level);
            final String message;
            if (bundle != null) {
                message = translate(bundle, msg);
            } else {
                message = msg;
            }
            LogEvent logEvent = new LogEvent(message, name, convertedLevel, thrown);
            logEventConsumer.accept(logEvent);
        }
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        if (isLoggable(level)) {
            com.swirlds.logging.api.Level convertedLevel = convertFromSystemLogger(level);
            final String message;
            if (bundle != null) {
                String translated = translate(bundle, format);
                message = MessageFormat.format(translated, params);
            } else {
                message = MessageFormat.format(format, params);
            }
            LogEvent logEvent = new LogEvent(message, name, convertedLevel);
            logEventConsumer.accept(logEvent);
        }
    }

    private static String translate(ResourceBundle bundle, String key) {
        if (bundle == null || key == null) {
            return key;
        }
        try {
            return bundle.getString(key);
        } catch (Exception x) {
            return key;
        }
    }
}
