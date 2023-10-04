package com.swirlds.logging.api.internal.format;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.emergency.EmergencyLogger;
import com.swirlds.logging.api.extensions.emergency.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.event.LogMessage;
import com.swirlds.logging.api.internal.Marker;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class LineBasedFormat {

    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    private final DateTimeFormatter formatter;

    private final PrintWriter printWriter;

    public LineBasedFormat(PrintWriter printWriter) {
        this.formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault());
        this.printWriter = printWriter;
    }

    public void print(final LogEvent event) {
        if (event == null) {
            EMERGENCY_LOGGER.logNPE("event");
        }
        printWriter.print(asString(event.timestamp()));
        printWriter.print(' ');
        printWriter.print(asString(event.level()));
        printWriter.print(' ');
        printWriter.print('[');
        printWriter.print(asString(event.threadName(), "THREAD"));
        printWriter.print(']');
        printWriter.print(' ');
        printWriter.print(asString(event.loggerName(), "LOGGER"));
        printWriter.print(" - ");
        printWriter.print(asString(event.message()));

        Marker marker = event.marker();
        if (marker != null) {
            printWriter.print(" - M:");
            printWriter.print(asString(marker));
        }

        final Map<String, String> context = event.context();
        if (context != null && !context.isEmpty()) {
            printWriter.print(" - C:");
            printWriter.print(context);
        }
        printWriter.println();

        Throwable throwable = event.throwable();
        if (throwable != null) {
            throwable.printStackTrace(printWriter);
        }
    }

    private String asString(String str, String suffix) {
        if (str == null) {
            return "UNDEFINED-" + suffix;
        } else {
            return str;
        }
    }

    private String asString(Level level) {
        if (level == null) {
            return "UNDEFINED";
        } else {
            return level.name();
        }
    }

    private String asString(LogMessage message) {
        if (message == null) {
            return "UNDEFINED-MESSAGE";
        } else {
            try {
                return message.getMessage();
            } catch (final Throwable e) {
                EMERGENCY_LOGGER.log(Level.ERROR, "Failed to format message", e);
                return "BROKEN-MESSAGE";
            }
        }
    }

    private String asString(Instant instant) {
        if (instant == null) {
            return "UNDEFINED-TIMESTAMP       ";
        } else {
            try {
                return formatter.format(instant);
            } catch (final Throwable e) {
                EMERGENCY_LOGGER.log(Level.ERROR, "Failed to format instant", e);
                return "BROKEN-TIMESTAMP          ";
            }
        }
    }

    private String asString(Marker marker) {
        if (marker == null) {
            return "null";
        } else {
            final Marker parent = marker.parent();
            if (parent == null) {
                return "Marker{name='" + marker.name() + "'}";
            } else {
                return "Marker{name='" + marker.name() + "', parent='" + asString(parent) + "'}";
            }
        }
    }
}
