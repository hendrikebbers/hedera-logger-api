package com.swirlds.logging.provider.jul;

import static com.swirlds.logging.provider.jul.JulConversionUtils.convertFromJul;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class JulInternalLogForwarder extends Handler {

    private final LogEventConsumer logEventConsumer;

    public JulInternalLogForwarder(LogEventConsumer logEventConsumer) {
        this.logEventConsumer = Objects.requireNonNull(logEventConsumer, "logEventConsumer must not be null");
    }

    @Override
    public void publish(LogRecord record) {
        Level level = convertFromJul(record.getLevel());
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
                                new LogEvent(translateOrKey(resourceBundle, message), name, level, record.getThrown()));
                    } else {
                        logEventConsumer.accept(new LogEvent(message, name, level, record.getThrown()));
                    }
                } else {
                    final ResourceBundle resourceBundle = record.getResourceBundle();
                    if (resourceBundle != null) {
                        logEventConsumer.accept(
                                new LogEvent(MessageFormat.format(translateOrKey(resourceBundle, message), parameters),
                                        name,
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

    private static String translateOrKey(ResourceBundle bundle, String key) {
        if (bundle == null || key == null) {
            return "";
        }
        try {
            return bundle.getString(key);
        } catch (Exception x) {
            return key;
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
