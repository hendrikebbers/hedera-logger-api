package com.swirlds.logging.api.internal.event;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.event.LogMessage;
import com.swirlds.logging.api.extensions.event.Marker;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * A mutable log event. The event can be used to not create new log event instances for every log statement.
 */
public class MutableLogEvent implements LogEvent {

    /**
     * The initial message that is used when the message is undefined.
     */
    private final static LogMessage INITIAL_MESSAGE = new SimpleLogMessage("");

    /**
     * The initial timestamp that is used when the timestamp is undefined.
     */
    private final static Instant INITIAL_INSTANT = Instant.MIN;

    /**
     * The log level.
     */
    private Level level;

    /**
     * The name of the logger.
     */
    private String loggerName;

    /**
     * The name of the thread.
     */
    private String threadName;

    /**
     * The timestamp of the log event.
     */
    private Instant timestamp;

    /**
     * The log message.
     */
    private LogMessage message;

    /**
     * The throwable.
     */
    private Throwable throwable;

    /**
     * The marker.
     */
    private Marker marker;

    /**
     * The context.
     */
    private Map<String, String> context;

    /**
     * Creates a new mutable log event that is filled with dummy values.
     */
    public MutableLogEvent() {
        update(Level.TRACE, "", "", INITIAL_INSTANT, INITIAL_MESSAGE, null, null, Map.of());
    }

    /**
     * Updates the log event with the given values.
     *
     * @param level      The log level
     * @param loggerName The name of the logger
     * @param threadName The name of the thread
     * @param timestamp  The timestamp of the log event
     * @param message    The log message (this is not a String since the message can be parameterized)
     * @param throwable  The throwable
     * @param marker     The marker
     * @param context    The context
     */
    public void update(Level level, String loggerName, String threadName, Instant timestamp, LogMessage message,
            Throwable throwable, Marker marker, Map<String, String> context) {
        this.level = level;
        this.loggerName = loggerName;
        this.threadName = threadName;
        this.timestamp = timestamp;
        this.message = message;
        this.throwable = throwable;
        this.marker = marker;
        this.context = Collections.unmodifiableMap(context);
    }

    @Override
    public Level level() {
        return level;
    }

    @Override
    public String loggerName() {
        return loggerName;
    }

    @Override
    public String threadName() {
        return threadName;
    }

    @Override
    public Instant timestamp() {
        return timestamp;
    }

    @Override
    public LogMessage message() {
        return message;
    }

    @Override
    public Throwable throwable() {
        return throwable;
    }

    @Override
    public Marker marker() {
        return marker;
    }

    @Override
    public Map<String, String> context() {
        return context;
    }
}
