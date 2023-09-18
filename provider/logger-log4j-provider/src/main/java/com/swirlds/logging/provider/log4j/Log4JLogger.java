package com.swirlds.logging.provider.log4j;

import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import com.swirlds.logging.api.extensions.emergency.EmergencyLoggerProvider;
import java.time.Instant;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;

public class Log4JLogger extends AbstractLogger {

    public Log4JLogger(final String name) {
        super(name);
    }

    public Log4JLogger(final String name, final MessageFactory messageFactory) {
        super(name, messageFactory);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4, Object p5) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4, Object p5, Object p6) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4, Object p5, Object p6, Object p7) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4, Object p5, Object p6, Object p7, Object p8) {
        return isEnabled(name, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
            Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return isEnabled(name, level);
    }


    private boolean isEnabled(String name, Level level) {
        final LogEventConsumer logEventConsumer = Log4JLogProvider.getLogEventConsumer();
        if (logEventConsumer == null) {
            return true;
        }
        return logEventConsumer.isEnabled(name, Log4jConverter.convertFromLog4J(level));
    }


    @Override
    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
        final LogEvent logEvent = new LogEvent(Log4jConverter.convertFromLog4J(level), name,
                Thread.currentThread().getName(), Instant.now(), message.getFormattedMessage(),
                t, Log4jConverter.convertFromLog4J(marker),
                Map.of()
        );
        final LogEventConsumer logEventConsumer = Log4JLogProvider.getLogEventConsumer();
        if (logEventConsumer == null) {
            EmergencyLoggerProvider.getEmergencyLogger()
                    .log(Log4jConverter.convertFromLog4J(level), logEvent.message(), logEvent.throwable());
        } else {
            logEventConsumer.accept(logEvent);
        }
    }

    @Override
    public Level getLevel() {
        final LogEventConsumer logEventConsumer = Log4JLogProvider.getLogEventConsumer();
        if (logEventConsumer == null) {
            return Level.ERROR;
        }
        if (logEventConsumer.isEnabled(name, com.swirlds.logging.api.Level.TRACE)) {
            return Level.TRACE;
        }
        if (logEventConsumer.isEnabled(name, com.swirlds.logging.api.Level.DEBUG)) {
            return Level.DEBUG;
        }
        if (logEventConsumer.isEnabled(name, com.swirlds.logging.api.Level.INFO)) {
            return Level.INFO;
        }
        if (logEventConsumer.isEnabled(name, com.swirlds.logging.api.Level.WARN)) {
            return Level.WARN;
        }
        return Level.ERROR;
    }
}