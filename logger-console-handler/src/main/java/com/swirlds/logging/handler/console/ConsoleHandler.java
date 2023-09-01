package com.swirlds.logging.handler.console;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ConsoleHandler implements LogHandler {

    private final DateTimeFormatter formatter;

    private final Configuration configuration;

    public ConsoleHandler(Configuration configuration) {
        this.configuration = configuration;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public String getName() {
        return "Console Handler";
    }

    @Override
    public boolean isActive() {
        return configuration.getValue("logging.handler.console.enabled", Boolean.class, false);
    }

    @Override
    public void accept(LogEvent event) {
        String timeStamp = event.timestamp().format(formatter);
        String threadName = event.threadName();
        StringBuffer sb = new StringBuffer();
        sb.append(timeStamp)
                .append(" ")
                .append(event.level())
                .append(" ")
                .append("[")
                .append(threadName)
                .append("]")
                .append(" ")
                .append(event.loggerName())
                .append(" - ")
                .append(event.message());

        Marker marker = event.marker();
        if (marker != null) {
            sb.append(" - M:").append(marker.getName());
        }

        final Map<String, String> context = event.context();

        if (!context.isEmpty()) {
            sb.append(" - C:").append(context);
        }

        if (event.throwable() != null) {
            sb.append(System.lineSeparator());
            final StringWriter sw = new StringWriter();
            event.throwable().printStackTrace(new PrintWriter(sw, true));
            sb.append(sw);
        }
        System.out.println(sb);
    }
}
