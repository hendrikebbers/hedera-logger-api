package com.swirlds.logging.api.internal.event;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.event.LogMessage;
import com.swirlds.logging.api.extensions.event.Marker;
import com.swirlds.logging.api.extensions.event.SimpleLogMessage;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public class MutableLogEvent implements LogEvent {

    private final static LogMessage INITIAL_MESSAGE = new SimpleLogMessage("");
    private final static Instant INITIAL_INSTANT = Instant.MIN;

    private Level level;

    private String loggerName;

    private String threadName;

    private Instant timestamp;

    private LogMessage message;
    private Throwable throwable;
    private Marker marker;
    private Map<String, String> context;

    public MutableLogEvent() {
        update(Level.TRACE, "", "", INITIAL_INSTANT, INITIAL_MESSAGE, null, null, Map.of());
    }

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
