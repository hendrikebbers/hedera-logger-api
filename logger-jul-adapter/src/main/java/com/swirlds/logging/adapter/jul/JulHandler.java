package com.swirlds.logging.adapter.jul;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class JulHandler extends Handler {

    private final LogEventConsumer logEventConsumer;

    public JulHandler(LogEventConsumer logEventConsumer) {
        this.logEventConsumer = Objects.requireNonNull(logEventConsumer, "logEventConsumer must not be null");
    }

    @Override
    public void publish(LogRecord record) {
        Level level = convert(record.getLevel());
        final String name = record.getLoggerName();
        if (logEventConsumer.isEnabled(name, level)) {
            String message = record.getMessage();
            final Object[] parameters = record.getParameters();
            if (message == null) {
                logEventConsumer.accept(new LogEvent("", name, level, record.getThrown()));
            } else {
                if (parameters == null || parameters.length == 0) {
                    final ResourceBundle resourceBundle = record.getResourceBundle();
                    if (resourceBundle != null) {
                        logEventConsumer.accept(
                                new LogEvent(translate(resourceBundle, message), name, level, record.getThrown()));
                    } else {
                        logEventConsumer.accept(new LogEvent(message, name, level, record.getThrown()));
                    }
                } else {
                    final ResourceBundle resourceBundle = record.getResourceBundle();
                    if (resourceBundle != null) {
                        logEventConsumer.accept(
                                new LogEvent(MessageFormat.format(translate(resourceBundle, message), parameters), name,
                                        level,
                                        record.getThrown()));
                    } else {
                        logEventConsumer.accept(
                                new LogEvent(MessageFormat.format(message, parameters), name, level,
                                        record.getThrown()));
                    }
                }
            }
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

    private static Level convert(java.util.logging.Level level) {
        if (level == null) {
            return Level.ERROR;
        }
        if (Objects.equals(java.util.logging.Level.SEVERE, level)) {
            return Level.ERROR;
        }
        if (Objects.equals(java.util.logging.Level.WARNING, level)) {
            return Level.WARN;
        }
        if (Objects.equals(java.util.logging.Level.INFO, level)) {
            return Level.INFO;
        }
        if (Objects.equals(java.util.logging.Level.CONFIG, level)) {
            return Level.DEBUG;
        }
        if (Objects.equals(java.util.logging.Level.FINE, level)) {
            return Level.DEBUG;
        }
        if (Objects.equals(java.util.logging.Level.FINER, level)) {
            return Level.DEBUG;
        }
        if (Objects.equals(java.util.logging.Level.FINEST, level)) {
            return Level.TRACE;
        }
        if (Objects.equals(java.util.logging.Level.ALL, level)) {
            return Level.TRACE;
        }
        return Level.ERROR;
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
