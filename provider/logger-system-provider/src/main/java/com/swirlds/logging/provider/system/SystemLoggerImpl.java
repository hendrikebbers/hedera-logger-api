package com.swirlds.logging.provider.system;

import static com.swirlds.logging.provider.system.SystemLoggerConverterUtils.convertFromSystemLogger;

import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import com.swirlds.logging.api.extensions.event.LogEventFactory;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class SystemLoggerImpl implements System.Logger {

    private final String name;

    private final LogEventConsumer logEventConsumer;

    private final LogEventFactory logEventFactory;

    public SystemLoggerImpl(final String name, final LogEventFactory factory, final LogEventConsumer logEventConsumer) {
        this.name = name;
        this.logEventConsumer = logEventConsumer;
        this.logEventFactory = factory;
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
            LogEvent logEvent = logEventFactory.createLogEvent(convertedLevel, name, message, thrown);
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
            LogEvent logEvent = logEventFactory.createLogEvent(convertedLevel, name, message);
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
