package com.swirlds.logging.provider.jul;

import static com.swirlds.logging.provider.jul.JulUtils.convertFromJul;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.emergency.EmergencyLogger;
import com.swirlds.logging.api.extensions.emergency.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.event.LogEventConsumer;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Forwards java.util.logging events to a {@link LogEventConsumer}. The class implements the {@link Handler} interface
 * of java.util.logging and converts all {@link LogRecord} objects to {@link LogEvent} objects.
 */
public final class JulInternalLogForwarder extends Handler {

    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    private final LogEventConsumer logEventConsumer;

    /**
     * Creates a new instance.
     *
     * @param logEventConsumer
     */
    public JulInternalLogForwarder(@NonNull final LogEventConsumer logEventConsumer) {
        this.logEventConsumer = Objects.requireNonNull(logEventConsumer, "logEventConsumer must not be null");
    }

    @Override
    public void publish(@NonNull final LogRecord record) {
        if (record == null) {
            EMERGENCY_LOGGER.logNPE("record");
            return;
        }
        final Level level = convertFromJul(record.getLevel());
        final String name = record.getLoggerName();
        if (logEventConsumer.isEnabled(name, level)) {
            final String message = JulUtils.extractMessage(record);
            logEventConsumer.accept(new LogEvent(level, name, message, record.getThrown()));
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
