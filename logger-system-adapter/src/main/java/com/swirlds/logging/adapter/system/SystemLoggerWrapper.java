package com.swirlds.logging.adapter.system;

import com.swirlds.logging.api.Logger;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SystemLoggerWrapper implements System.Logger {

    private final Logger innerLogger;

    public SystemLoggerWrapper(final Logger innerLogger) {
        this.innerLogger = innerLogger;
    }

    @Override
    public String getName() {
        return innerLogger.getName();
    }

    @Override
    public boolean isLoggable(Level level) {
        switch (level) {
            case ALL:
                return true;
            case TRACE:
                return innerLogger.isEnabled(com.swirlds.logging.api.Level.TRACE);
            case DEBUG:
                return innerLogger.isEnabled(com.swirlds.logging.api.Level.DEBUG);
            case INFO:
                return innerLogger.isEnabled(com.swirlds.logging.api.Level.INFO);
            case WARNING:
                return innerLogger.isEnabled(com.swirlds.logging.api.Level.WARN);
            case ERROR:
                return innerLogger.isEnabled(com.swirlds.logging.api.Level.ERROR);
            case OFF:
                return false;
        }
        return false;
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        if (isLoggable(level)) {
            com.swirlds.logging.api.Level convertedLevel = convert(level);
            final String message;
            if (bundle != null) {
                message = getString(bundle, msg);
            } else {
                message = msg;
            }
            innerLogger.logImpl(convertedLevel, message, null);
        }
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        if (isLoggable(level)) {
            com.swirlds.logging.api.Level convertedLevel = convert(level);
            final String message;
            if (bundle != null) {
                String translated = getString(bundle, format);
                message = MessageFormat.format(translated, params);
            } else {
                message = MessageFormat.format(format, params);
                ;
            }
            innerLogger.logImpl(convertedLevel, message, null);
        }
    }

    private static com.swirlds.logging.api.Level convert(Level level) {
        switch (level) {
            case ALL:
                return com.swirlds.logging.api.Level.TRACE;
            case TRACE:
                return com.swirlds.logging.api.Level.TRACE;
            case DEBUG:
                return com.swirlds.logging.api.Level.DEBUG;
            case INFO:
                return com.swirlds.logging.api.Level.INFO;
            case WARNING:
                return com.swirlds.logging.api.Level.WARN;
            case ERROR:
                return com.swirlds.logging.api.Level.ERROR;
            case OFF:
                return com.swirlds.logging.api.Level.ERROR;
        }
        return com.swirlds.logging.api.Level.ERROR;
    }

    private static String getString(ResourceBundle bundle, String key) {
        if (bundle == null || key == null) {
            return key;
        }
        try {
            return bundle.getString(key);
        } catch (MissingResourceException x) {
            // Emulate what java.util.logging Formatters do
            // We don't want unchecked exception to propagate up to
            // the caller's code.
            return key;
        }
    }
}
