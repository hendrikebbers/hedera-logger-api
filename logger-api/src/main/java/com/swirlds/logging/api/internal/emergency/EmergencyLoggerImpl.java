package com.swirlds.logging.api.internal.emergency;

import com.swirlds.logging.api.extensions.EmergencyLogger;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.internal.format.LineBasedFormat;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * A {@link System.Logger} implementation that is used as a LAST RESORT when no other logger is available. It is
 * important that this logger does not depend on any other logger implementation and that it does not throw exceptions.
 * Next to that the logger should try to somehow log the message even in a broken system.
 * <p>
 * The logger is defined as a singleton.
 */
public class EmergencyLoggerImpl implements EmergencyLogger {

    /**
     * A reference to the inner logger that is used to log the messages. This reference can be set at runtime once the
     * real logging system is available. The {@link EmergencyLoggerImpl} even tries to log a message (for example to
     * {@link System#out}) when the inner logger is broken.
     */
    private final static AtomicReference<System.Logger> innerLoggerRef = new AtomicReference<>();

    /**
     * The singleton instance of the {@link EmergencyLoggerImpl}.
     */
    private final static EmergencyLoggerImpl INSTANCE = new EmergencyLoggerImpl();

    /**
     * The name of the logger.
     */
    private final static String name = "EMERGENCY-LOGGER";

    /**
     * The name of the system property that defines the level of the logger.
     */
    private final static String LEVEL_PROPERTY_NAME = "com.swirlds.logging.emergency.level";

    /**
     * The level that is supported by the logger.
     */
    private final Level supportedLevel;

    /**
     * The queue that is used to store the log events. Once the real logging system is available the events can be taken
     * by the logging system and logged.
     */
    private final ArrayBlockingQueue<LogEvent> logEvents;

    /**
     * A thread local that is used to prevent recursion. This can happen when the logger is used in a broken system.
     */
    private final ThreadLocal<Boolean> recursionGuard;

    /**
     * The constructor of the {@link EmergencyLoggerImpl}.
     */
    private EmergencyLoggerImpl() {
        logEvents = new ArrayBlockingQueue<>(1000);
        recursionGuard = new ThreadLocal<>();
        supportedLevel = getSupportedLevelFromSystemProperties();
    }

    /**
     * Returns the level based on a possible system property.
     *
     * @return the level based on a possible system property
     */
    private static Level getSupportedLevelFromSystemProperties() {
        final String property = System.getProperty(LEVEL_PROPERTY_NAME);
        if (property == null) {
            return Level.DEBUG; //DEFAULT LEVEL
        } else if (Objects.equals(property.toUpperCase(), "TRACE")) {
            return Level.TRACE;
        } else if (Objects.equals(property.toUpperCase(), "DEBUG")) {
            return Level.DEBUG;
        } else if (Objects.equals(property.toUpperCase(), "INFO")) {
            return Level.INFO;
        } else if (Objects.equals(property.toUpperCase(), "WARN")) {
            return Level.WARNING;
        } else if (Objects.equals(property.toUpperCase(), "ERROR")) {
            return Level.ERROR;
        } else {
            return Level.TRACE;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * A method that is used to call any given {@link Supplier} in a guarded way. This includes exception handling and a
     * recursion check.
     *
     * @param supplier the supplier that should be called
     * @param <T>      the type of the result
     * @return the result of the supplier
     */
    private <T> T callGuarded(@NonNull Supplier<T> supplier, @NonNull T fallbackValue) {
        return callGuarded(null, fallbackValue, supplier);
    }

    /**
     * A method that is used to call any given {@link Runnable} in a guarded way. This includes exception handling and a
     * recursion check. In case of a problem the method tries to at least log the given fallback log event.
     *
     * @param fallbackLogEvent the fallback log event that should be logged when the logger is broken.
     * @param task             the task that should be called
     */
    private void callGuarded(@NonNull final LogEvent fallbackLogEvent, @NonNull final Runnable task) {
        callGuarded(fallbackLogEvent, null, () -> {
            task.run();
            return null;
        });
    }

    /**
     * A method that is used to call any given {@link Supplier} in a guarded way. This includes exception handling and a
     * recursion check. In case of a problem the method tries to at least log the given fallback log event.
     *
     * @param fallbackLogEvent the fallback log event that should be logged when the logger is broken.
     * @param supplier         the supplier that should be called
     * @param <T>              the type of the result
     * @return the result of the supplier
     */
    private <T> T callGuarded(@NonNull final LogEvent fallbackLogEvent, @NonNull T fallbackValue,
            @NonNull final Supplier<T> supplier) {
        final Boolean guard = recursionGuard.get();
        if (guard != null && guard) {
            final LogEvent logEvent = new LogEvent("Recursion in Emergency logger", name,
                    com.swirlds.logging.api.Level.ERROR,
                    new IllegalStateException("Recursion in Emergency logger"));
            handle(logEvent);
            if (fallbackLogEvent != null) {
                handle(fallbackLogEvent);
            }
            return fallbackValue;
        } else {
            recursionGuard.set(true);
            try {
                return supplier.get();
            } catch (final Throwable t) {
                final LogEvent logEvent = new LogEvent("Error in Emergency logger", name,
                        com.swirlds.logging.api.Level.ERROR,
                        t);
                handle(logEvent);
                if (fallbackLogEvent != null) {
                    handle(fallbackLogEvent);
                }
                return fallbackValue;
            } finally {
                recursionGuard.set(false);
            }
        }
    }

    @Override
    public boolean isLoggable(Level level) {
        return callGuarded(() -> {
            if (level == null) {
                return true;
            }
            final System.Logger innerLogger = innerLoggerRef.get();
            if (innerLogger != null) {
                return innerLogger.isLoggable(level);
            }
            return supportedLevel.getSeverity() <= level.getSeverity();
        }, true);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        LogEvent fallbackLogEvent = new LogEvent(msg, name, convertFromSystemLogger(level));
        callGuarded(fallbackLogEvent, () -> {
            final System.Logger innerLogger = innerLoggerRef.get();
            if (innerLogger != null) {
                innerLogger.log(level, bundle, msg, thrown);
            } else {
                final LogEvent logEvent = new LogEvent(msg, name, convertFromSystemLogger(level), thrown);
                handle(logEvent);
            }
        });
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        final LogEvent fallbackLogEvent;
        if (params == null || params.length == 0) {
            fallbackLogEvent = new LogEvent(format, name, convertFromSystemLogger(level));
        } else {
            fallbackLogEvent = new LogEvent(format + " -> " + params, name, convertFromSystemLogger(level));
        }
        callGuarded(fallbackLogEvent, () -> {
            final System.Logger innerLogger = innerLoggerRef.get();
            if (innerLogger != null) {
                innerLogger.log(level, bundle, format, params);
            } else {
                handle(fallbackLogEvent); //TODO: Create message instead of simple printing args
            }
        });
    }


    @Override
    public void log(LogEvent event) {
        if (event == null) {
            logNPE("event");
        }
        callGuarded(event, () -> {
            final System.Logger innerLogger = innerLoggerRef.get();
            if (innerLogger != null) {
                innerLogger.log(convertToSystemLogger(event.level()), null, event.message(), event.throwable());
            } else {
                handle(event);
            }
        });
    }

    /**
     * Handles the given log event by trying to print the message to the console and adding it to the queue. This is the
     * default logging behavior if no inner logger is set (see {@link #setInnerLogger(Logger)}).
     *
     * @param logEvent the log event that should be handled
     */
    private void handle(@NonNull final LogEvent logEvent) {
        if (logEvent != null) {
            final PrintStream printStream = Optional.ofNullable(System.err)
                    .orElse(System.out);
            if (printStream != null) {
                new LineBasedFormat(new PrintWriter(printStream, true)).print(logEvent);
            }
            if (logEvents.remainingCapacity() == 0) {
                logEvents.remove();
            }
            logEvents.add(logEvent);
        }
    }

    /**
     * Converts the given {@link Level} to a {@link com.swirlds.logging.api.Level}.
     *
     * @param level the level that should be converted
     * @return the converted level
     */
    private static com.swirlds.logging.api.Level convertFromSystemLogger(@NonNull Level level) {
        if (level == null) {
            return com.swirlds.logging.api.Level.ERROR;
        }
        return switch (level) {
            case TRACE -> com.swirlds.logging.api.Level.TRACE;
            case DEBUG -> com.swirlds.logging.api.Level.DEBUG;
            case INFO -> com.swirlds.logging.api.Level.INFO;
            case WARNING -> com.swirlds.logging.api.Level.WARN;
            default -> com.swirlds.logging.api.Level.ERROR;
        };
    }

    private static Level convertToSystemLogger(@NonNull com.swirlds.logging.api.Level level) {
        if (level == null) {
            return Level.ERROR;
        }
        return switch (level) {
            case TRACE -> Level.TRACE;
            case DEBUG -> Level.DEBUG;
            case INFO -> Level.INFO;
            case WARN -> Level.WARNING;
            default -> Level.ERROR;
        };
    }


    /**
     * Returns the singleton instance of the emergency logger.
     *
     * @return the singleton instance of the emergency logger
     */
    public static EmergencyLoggerImpl getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the list of logged events and clears the list.
     *
     * @return the list of logged events
     */
    public List<LogEvent> publishLoggedEvents() {
        List<LogEvent> result = List.copyOf(logEvents);
        logEvents.clear();
        return result;
    }

    /**
     * Sets the inner logger that should be used to log the messages.
     *
     * @param logger the inner logger that should be used to log the messages.
     */
    public static void setInnerLogger(System.Logger logger) {
        innerLoggerRef.set(logger);
    }

    @Override
    public void logNPE(@NonNull String nameOfNullParam) {
        log(Level.ERROR, "Null parameter: " + nameOfNullParam,
                new NullPointerException("Null parameter: " + nameOfNullParam));
    }
}
