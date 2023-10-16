package com.swirlds.logging.api.internal.event;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import com.swirlds.logging.api.extensions.event.LogMessage;
import com.swirlds.logging.api.extensions.event.Marker;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Instant;
import java.util.Map;

/**
 * A log event that is passed to the {@link LogEventConsumer} for processing.
 *
 * @param level      The log level
 * @param loggerName The name of the logger
 * @param threadName The name of the thread
 * @param timestamp  The timestamp of the log event
 * @param message    The log message (this is not a String since the message can be parameterized. See
 *                   {@link LogMessage} for more details).
 * @param throwable  The throwable
 * @param marker     The marker
 * @param context    The context
 */
public record DefaultLogEvent(@NonNull Level level, @NonNull String loggerName, @NonNull String threadName,
                              @NonNull Instant timestamp, @NonNull LogMessage message,
                              @Nullable Throwable throwable, @Nullable Marker marker,
                              @NonNull Map<String, String> context
) implements LogEvent {

}