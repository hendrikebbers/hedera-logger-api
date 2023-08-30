package com.swirlds.logging.api.internal.util;

import java.lang.System.Logger.Level;

public class SystemLoggerConverterUtils {

    public static com.swirlds.logging.api.Level convertFromSystemLogger(Level level) {
        if (level == Level.ERROR) {
            return com.swirlds.logging.api.Level.ERROR;
        }
        if (level == Level.WARNING) {
            return com.swirlds.logging.api.Level.WARN;
        }
        if (level == Level.INFO) {
            return com.swirlds.logging.api.Level.INFO;
        }
        if (level == Level.DEBUG) {
            return com.swirlds.logging.api.Level.DEBUG;
        }
        return com.swirlds.logging.api.Level.TRACE;
    }

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
