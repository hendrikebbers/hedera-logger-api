package com.swirlds.logging.handler.rollingfile;

import com.swirlds.logging.api.extensions.LogEvent;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

public class Log4JWrappedLogEvent extends org.apache.logging.log4j.core.AbstractLogEvent {

    private final LogEvent event;

    public Log4JWrappedLogEvent(LogEvent event) {
        this.event = event;
    }

    @Override
    public Map<String, String> getContextMap() {
        return event.context();
    }

    @Override
    public ReadOnlyStringMap getContextData() {
        return null;
    }

    @Override
    public ContextStack getContextStack() {
        return null;
    }

    @Override
    public String getLoggerFqcn() {
        return null;
    }

    @Override
    public org.apache.logging.log4j.Level getLevel() {
        return Log4jConverter.convertToLog4J(event.level());
    }

    @Override
    public String getLoggerName() {
        return event.loggerName();
    }

    @Override
    public org.apache.logging.log4j.Marker getMarker() {
        return Log4jConverter.convertToLog4J(event.marker());
    }

    @Override
    public Message getMessage() {
        return new SimpleMessage(event.message().getMessage());
    }

    @Override
    public long getTimeMillis() {
        return TimeUnit.SECONDS.toMillis(event.timestamp().toEpochMilli());
    }

    @Override
    public Instant getInstant() {
        final MutableInstant instant = new MutableInstant();
        instant.initFromEpochMilli(event.timestamp().toEpochMilli(), 0);
        return instant;
    }

    @Override
    public StackTraceElement getSource() {
        return null;
    }

    @Override
    public String getThreadName() {
        return event.threadName();
    }

    @Override
    public Throwable getThrown() {
        return event.throwable();
    }

    @Override
    public ThrowableProxy getThrownProxy() {
        return null;
    }

    @Override
    public long getNanoTime() {
        return 0;
    }
}
