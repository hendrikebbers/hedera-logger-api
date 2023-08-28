package com.swirlds.logging.adapter.jul;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.extensions.DefaultLoggerSystem;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class JulHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        Level level = convert(record.getLevel());
        final String loggerName = record.getLoggerName();
        Logger logger = DefaultLoggerSystem.getInstance().getLogger(loggerName);
        if (logger.isEnabled(level)) {
            String message = record.getMessage();
            final Object[] parameters = record.getParameters();
            if (message == null) {
                logger.logImpl(level, "", record.getThrown());
            } else {
                if (parameters == null || parameters.length == 0) {
                    logger.logImpl(level, message, record.getThrown());
                } else {
                    logger.logImpl(level, MessageFormat.format(message, parameters), record.getThrown());
                }
            }
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
