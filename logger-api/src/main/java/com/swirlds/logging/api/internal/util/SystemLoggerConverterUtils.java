package com.swirlds.logging.api.internal.util;

import java.lang.System.Logger.Level;

public class SystemLoggerConverterUtils {

    public static Level convertToSystemLogger(com.swirlds.logging.api.Level level) {
        if (level == com.swirlds.logging.api.Level.ERROR) {
            return Level.ERROR;
        }
        if (level == com.swirlds.logging.api.Level.WARN) {
            return Level.WARNING;
        }
        if (level == com.swirlds.logging.api.Level.INFO) {
            return Level.INFO;
        }
        if (level == com.swirlds.logging.api.Level.DEBUG) {
            return Level.DEBUG;
        }
        return Level.TRACE;
    }
}
