package com.swirlds.logging.api.internal.emergency;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.emergency.EmergencyLogger;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.internal.format.LineBasedFormat;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * A {@link EmergencyLogger} implementation that is used as a LAST RESORT when no other logger is available. It is
 * important that this logger does not depend on any other logger implementation and that it does not throw exceptions.
 * Next to that the logger should try to somehow log the message even in a broken system.
 * <p>
 * The logger is defined as a singleton.
 */
public class EmergencyLoggerImpl implements EmergencyLogger {

    /**
     * The name of the emergency logger.
     */
    private final static String EMERGENCY_LOGGER_NAME = "EMERGENCY-LOGGER";

    /**
     * The message that is used when the message is undefined.
     */
    private final static String UNDEFINED_MESSAGE = "UNDEFINED-MESSAGE";

    /**
     * The size of the queue that is used to store the log events.
     */
    private final static int LOG_EVENT_QUEUE_SIZE = 1000;

    /**
     * The name of the system property that defines the level of the logger.
     */
    private final static String LEVEL_PROPERTY_NAME = "com.swirlds.logging.emergency.level";

    /**
     * The singleton instance of the logger.
     */
    private final static EmergencyLoggerImpl INSTANCE = new EmergencyLoggerImpl();

    /**
     * The level that is supported by the logger.
     */
    private final AtomicReference<Level> supportedLevel = new AtomicReference<>();

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
     * Creates the singleton instance of the logger.
     */
    private EmergencyLoggerImpl() {
        this.logEvents = new ArrayBlockingQueue<>(LOG_EVENT_QUEUE_SIZE);
        recursionGuard = new ThreadLocal<>();
    }

    /**
     * Returns the level based on a possible system property.
     *
     * @return the level based on a possible system property
     */
    @NonNull
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
            return Level.WARN;
        } else if (Objects.equals(property.toUpperCase(), "ERROR")) {
            return Level.ERROR;
        } else {
            return Level.TRACE;
        }
    }

    @Override
    public void logNPE(@NonNull String nameOfNullParam) {
        log(Level.ERROR, "Null parameter: " + nameOfNullParam,
                new NullPointerException("Null parameter: " + nameOfNullParam));
    }

    @Override
    public void log(@NonNull Level level, @NonNull String message, @Nullable Throwable thrown) {
        if (level == null && message == null) {
            log(new LogEvent(Level.ERROR, EMERGENCY_LOGGER_NAME, UNDEFINED_MESSAGE, thrown));
        } else if (level == null) {
            log(new LogEvent(Level.ERROR, EMERGENCY_LOGGER_NAME, message, thrown));
        } else if (message == null) {
            log(new LogEvent(level, EMERGENCY_LOGGER_NAME, UNDEFINED_MESSAGE, thrown));
        } else {
            log(new LogEvent(level, EMERGENCY_LOGGER_NAME, message, thrown));
        }
    }

    @Override
    public void log(@NonNull LogEvent event) {
        if (event == null) {
            logNPE("event");
            return;
        }
        if (isLoggable(event.level())) {
            callGuarded(event, () -> handle(event));
        }
    }

    /**
     * Checks if the given level is loggable by the logger.
     *
     * @param level the level to check
     * @return true if the level is loggable, false otherwise
     */
    private boolean isLoggable(@NonNull Level level) {
        if (level == null) {
            logNPE("level");
            return true;
        }
        if (supportedLevel.get() == null) {
            supportedLevel.set(getSupportedLevelFromSystemProperties());
        }
        final Level internalSupportedLevel = supportedLevel.get();
        if (internalSupportedLevel == null) {
            return true;
        }
        return internalSupportedLevel.enabledLoggingOfLevel(level);
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
     * @param fallbackValue    the fallback value that should be returned when the logger is broken.
     * @param supplier         the supplier that should be called
     * @param <T>              the type of the result
     * @return the result of the supplier
     */
    @Nullable
    private <T> T callGuarded(@Nullable final LogEvent fallbackLogEvent, @Nullable T fallbackValue,
            @NonNull final Supplier<T> supplier) {
        final Boolean guard = recursionGuard.get();
        if (guard != null && guard) {
            final LogEvent logEvent = new LogEvent(Level.ERROR, EMERGENCY_LOGGER_NAME, "Recursion in Emergency logger",
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
                final LogEvent logEvent = new LogEvent(Level.ERROR, EMERGENCY_LOGGER_NAME, "Error in Emergency logger",
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

    /**
     * Handles the given log event by trying to print the message to the console and adding it to the queue.
     *
     * @param logEvent the log event that should be handled
     */
    private void handle(@NonNull final LogEvent logEvent) {
        if (logEvent == null) {
            logNPE("logEvent");
            return;
        }
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

    /**
     * Returns the list of logged events and clears the list.
     *
     * @return the list of logged events
     */
    @NonNull
    public List<LogEvent> publishLoggedEvents() {
        List<LogEvent> result = List.copyOf(logEvents);
        logEvents.clear();
        return result;
    }

    /**
     * Returns the instance of the logger.
     *
     * @return the instance of the logger
     */
    @NonNull
    public static EmergencyLoggerImpl getInstance() {
        return INSTANCE;
    }
}
