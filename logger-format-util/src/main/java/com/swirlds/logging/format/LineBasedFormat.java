package com.swirlds.logging.format;

import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.EmergencyLogger;
import com.swirlds.logging.api.extensions.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.LogEvent;
import java.io.PrintWriter;
import java.lang.System.Logger.Level;
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
        final String timeStamp = asString(event.timestamp());
        String threadName = event.threadName();
        printWriter.print(timeStamp);
        printWriter.print(' ');
        printWriter.print(event.level());
        printWriter.print(' ');
        printWriter.print('[');
        printWriter.print(threadName);
        printWriter.print(']');
        printWriter.print(' ');
        printWriter.print(event.loggerName());
        printWriter.print(" - ");
        printWriter.print(event.message());

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

    private String asString(Instant instant) {
        if (instant == null) {
            return "UNDEFINED";
        } else {
            try {
                return formatter.format(instant);
            } catch (final Throwable e) {
                EMERGENCY_LOGGER.log(Level.ERROR, "Failed to format instant", e);
                return "UNDEFINED";
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
