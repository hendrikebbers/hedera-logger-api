package com.swirlds.logging.api.internal.util;

import com.swirlds.logging.api.extensions.LogEvent;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;

public class EmergencyLogger implements System.Logger {

    private final static System.Logger innerLogger = System.getLogger(EmergencyLogger.class.getName());

    private final static EmergencyLogger INSTANCE = new EmergencyLogger();

    private final static String name = "EMERGENCY-LOGGER";

    private final static String LEVEL_PROPERTY_NAME = "com.swirlds.logging.emergency.level";

    private final Level supportedLevel;

    private final ArrayBlockingQueue<LogEvent> logEvents;

    private final ThreadLocal<Boolean> recursionGuard;

    private EmergencyLogger() {
        logEvents = new ArrayBlockingQueue<>(1000);
        recursionGuard = new ThreadLocal<>();
        final String property = System.getProperty(LEVEL_PROPERTY_NAME);
        if (property == null) {
            supportedLevel = Level.DEBUG;
        } else if (Objects.equals(property.toUpperCase(), "TRACE")) {
            supportedLevel = Level.TRACE;
        } else if (Objects.equals(property.toUpperCase(), "DEBUG")) {
            supportedLevel = Level.DEBUG;
        } else if (Objects.equals(property.toUpperCase(), "INFO")) {
            supportedLevel = Level.INFO;
        } else if (Objects.equals(property.toUpperCase(), "WARN")) {
            supportedLevel = Level.WARNING;
        } else if (Objects.equals(property.toUpperCase(), "ERROR")) {
            supportedLevel = Level.ERROR;
        } else {
            supportedLevel = Level.TRACE;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLoggable(Level level) {
        final Boolean guard = recursionGuard.get();
        if (guard != null && guard) {
            return true;
        } else {
            recursionGuard.set(true);
            try {
                if (level == null) {
                    return true;
                }
                if (innerLogger != null) {
                    return innerLogger.isLoggable(level);
                }
                return supportedLevel.getSeverity() <= level.getSeverity();
            } finally {
                recursionGuard.set(false);
            }
        }
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        final Boolean guard = recursionGuard.get();
        if (guard != null && guard) {
            LogEvent logEvent = new LogEvent(msg, name, convertFromSystemLogger(level), thrown);
            handle(logEvent);
        } else {
            recursionGuard.set(true);
            try {
                if (innerLogger != null) {
                    innerLogger.log(level, bundle, msg, thrown);
                } else {
                    LogEvent logEvent = new LogEvent(msg, name, convertFromSystemLogger(level),
                            thrown);
                    handle(logEvent);
                }
            } finally {
                recursionGuard.set(false);
            }
        }
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        final Boolean guard = recursionGuard.get();
        if (guard != null && guard) {
            LogEvent logEvent = new LogEvent(format + " -> " + params, name, convertFromSystemLogger(level));
            handle(logEvent);
        } else {
            recursionGuard.set(true);
            try {
                if (innerLogger != null) {
                    innerLogger.log(level, bundle, format, params);
                } else {
                    LogEvent logEvent = new LogEvent(format + " -> " + params, name, convertFromSystemLogger(level));
                    handle(logEvent);
                }
            } finally {
                recursionGuard.set(false);
            }
        }
    }

    private void handle(final LogEvent logEvent) {
        if (logEvent != null) {
            final PrintStream printStream = Optional.ofNullable(System.err)
                    .orElse(System.out);
            if (printStream != null) {
                printStream.println(name + ": " + logEvent.message());
                Optional.ofNullable(logEvent.throwable())
                        .ifPresent(Throwable::printStackTrace);
            }
            logEvents.add(logEvent);
        }
    }

    public static com.swirlds.logging.api.Level convertFromSystemLogger(Level level) {
        if (level == Level.TRACE) {
            return com.swirlds.logging.api.Level.TRACE;
        }
        if (level == Level.DEBUG) {
            return com.swirlds.logging.api.Level.TRACE;
        }
        if (level == Level.INFO) {
            return com.swirlds.logging.api.Level.TRACE;
        }
        if (level == Level.WARNING) {
            return com.swirlds.logging.api.Level.TRACE;
        }
        return com.swirlds.logging.api.Level.ERROR;
    }

    public static EmergencyLogger getInstance() {
        return INSTANCE;
    }

    public List<LogEvent> getLoggedEvents() {
        List<LogEvent> result = List.copyOf(logEvents);
        logEvents.clear();
        return result;
    }

}
