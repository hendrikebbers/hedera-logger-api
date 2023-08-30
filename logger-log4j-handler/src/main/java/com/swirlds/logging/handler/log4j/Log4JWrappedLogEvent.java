package com.swirlds.logging.handler.log4j;

import com.swirlds.logging.api.extensions.LogEvent;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.time.Instant;
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
        return new SimpleMessage(event.message());
    }

    @Override
    public long getTimeMillis() {
        return TimeUnit.SECONDS.toMillis(event.timestamp().toEpochSecond(ZoneOffset.UTC));
    }

    @Override
    public Instant getInstant() {
        return new Instant() {

            @Override
            public void formatTo(StringBuilder buffer) {
                buffer.append("MutableInstant[epochSecond=").append(getEpochSecond()).append(", nano=")
                        .append(getNanoOfSecond()).append("]");
            }

            @Override
            public long getEpochSecond() {
                return event.timestamp().toEpochSecond(ZoneOffset.UTC);
            }

            @Override
            public int getNanoOfSecond() {
                return event.timestamp().get(ChronoField.NANO_OF_SECOND);
            }

            @Override
            public long getEpochMillisecond() {
                return 0;
            }

            @Override
            public int getNanoOfMillisecond() {
                return 0;
            }
        };
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
