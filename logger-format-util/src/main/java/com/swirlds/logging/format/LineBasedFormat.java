package com.swirlds.logging.format;

import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.EmergencyLogger;
import com.swirlds.logging.api.extensions.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.LogEvent;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class LineBasedFormat {

    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    private final DateTimeFormatter formatter;

    private final PrintWriter printWriter;

    private static final int MAX_PRINTED_STACK = 10;

    public LineBasedFormat(PrintWriter printWriter) {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.printWriter = printWriter;
    }

    public void print(final LogEvent event) {
        if (event == null) {
            EMERGENCY_LOGGER.logNPE("event");
        }
        final Instant instant = event.timestamp();
        final String timeStamp;
        if (instant == null) {
            timeStamp = "------UNKNOWN------";
        } else {
            timeStamp = formatter.format(instant);
        }
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

    public String asString(Marker marker) {
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
