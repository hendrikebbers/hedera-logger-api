package com.swirlds.logging.provider.system;

import java.lang.System.Logger.Level;

public class SystemLoggerConverterUtils {

    public static com.swirlds.logging.api.Level convertFromSystemLogger(Level level) {
        return switch (level) {
            case ALL, TRACE -> com.swirlds.logging.api.Level.TRACE;
            case DEBUG -> com.swirlds.logging.api.Level.DEBUG;
            case INFO -> com.swirlds.logging.api.Level.INFO;
            case WARNING -> com.swirlds.logging.api.Level.WARN;
            case ERROR, OFF -> com.swirlds.logging.api.Level.ERROR;
        };
    }
}
