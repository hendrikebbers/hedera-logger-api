package com.swirlds.logging.provider.system;

import java.lang.System.Logger.Level;

public class SystemLoggerConverterUtils {

    public static com.swirlds.logging.api.Level convertFromSystemLogger(Level level) {
        switch (level) {
            case ALL:
                return com.swirlds.logging.api.Level.TRACE;
            case TRACE:
                return com.swirlds.logging.api.Level.TRACE;
            case DEBUG:
                return com.swirlds.logging.api.Level.DEBUG;
            case INFO:
                return com.swirlds.logging.api.Level.INFO;
            case WARNING:
                return com.swirlds.logging.api.Level.WARN;
            case ERROR:
                return com.swirlds.logging.api.Level.ERROR;
            case OFF:
                return com.swirlds.logging.api.Level.ERROR;
        }
        return com.swirlds.logging.api.Level.ERROR;
    }
}
