package com.swirlds.logging.api.internal.event;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.event.LogEventFactory;
import com.swirlds.logging.api.extensions.event.LogMessage;
import com.swirlds.logging.api.extensions.event.Marker;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Instant;
import java.util.Map;

public class ReuseableLogEventFactory implements LogEventFactory {

    private final ThreadLocal<MutableLogEvent> threadLocal = ThreadLocal.withInitial(() -> new MutableLogEvent());

    @NonNull
    @Override
    public LogEvent createLogEvent(@NonNull Level level, @NonNull String loggerName, @NonNull String threadName,
            @NonNull Instant timestamp, @NonNull LogMessage message, @Nullable Throwable throwable,
            @Nullable Marker marker, @NonNull Map<String, String> context) {
        final MutableLogEvent mutableLogEvent = threadLocal.get();
        mutableLogEvent.update(level, loggerName, threadName, timestamp, message, throwable, marker, context);
        return mutableLogEvent;
    }
}
