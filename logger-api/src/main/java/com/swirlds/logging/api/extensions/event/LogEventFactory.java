package com.swirlds.logging.api.extensions.event;

import com.swirlds.logging.api.Level;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public interface LogEventFactory {

    @NonNull
    LogEvent createLogEvent(@NonNull Level level, @NonNull String loggerName, @NonNull String threadName,
            @NonNull Instant timestamp, @NonNull LogMessage message,
            @Nullable Throwable throwable, @Nullable Marker marker,
            @NonNull Map<String, String> context);

    @NonNull
    default LogEvent createLogEvent(@NonNull Level level, @NonNull String loggerName, @NonNull String threadName,
            @NonNull Instant timestamp, @NonNull String message,
            @Nullable Throwable throwable, @Nullable Marker marker,
            @NonNull Map<String, String> context) {
        return createLogEvent(level, loggerName, threadName, timestamp, new SimpleLogMessage(message), throwable,
                marker, context);
    }

    @NonNull
    default LogEvent createLogEvent(@NonNull Level level, @NonNull String loggerName, @NonNull String message) {
        return createLogEvent(level, loggerName, message, null);
    }

    @NonNull
    default LogEvent createLogEvent(@NonNull Level level, @NonNull String loggerName, @NonNull String message,
            @Nullable Throwable throwable) {
        return createLogEvent(level, loggerName, Thread.currentThread().getName(), Instant.now(),
                new SimpleLogMessage(message), throwable, null,
                Map.of()
        );
    }


    /**
     * Creates a new {@link LogEvent} that has all parameters of the given logEvent but a different loggerName.
     *
     * @param logEvent   the logEvent that should be copied (excluding the loggerName)
     * @param loggerName the new logger name
     * @return the new copy of the event
     */
    @NonNull
    default LogEvent createLogEvent(@NonNull LogEvent logEvent, @NonNull String loggerName) {
        Objects.requireNonNull(logEvent);
        return createLogEvent(logEvent.level(), loggerName, logEvent.threadName(),
                logEvent.timestamp(), logEvent.message(),
                logEvent.throwable(), logEvent.marker(), logEvent.context());
    }

}
