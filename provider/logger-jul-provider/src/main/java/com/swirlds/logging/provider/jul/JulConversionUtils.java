package com.swirlds.logging.provider.jul;

import com.swirlds.logging.api.Level;
import java.util.Objects;

public class JulConversionUtils {

    public static Level convertFromJul(java.util.logging.Level level) {
        if (level == null) {
            return Level.ERROR;
        }
        if (Objects.equals(java.util.logging.Level.SEVERE, level)) {
            return Level.ERROR;
        }
        if (Objects.equals(java.util.logging.Level.WARNING, level)) {
            return Level.WARN;
        }
        if (Objects.equals(java.util.logging.Level.INFO, level)) {
            return Level.INFO;
        }
        if (Objects.equals(java.util.logging.Level.CONFIG, level)) {
            return Level.DEBUG;
        }
        if (Objects.equals(java.util.logging.Level.FINE, level)) {
            return Level.DEBUG;
        }
        if (Objects.equals(java.util.logging.Level.FINER, level)) {
            return Level.DEBUG;
        }
        if (Objects.equals(java.util.logging.Level.FINEST, level)) {
            return Level.TRACE;
        }
        if (Objects.equals(java.util.logging.Level.ALL, level)) {
            return Level.TRACE;
        }
        return Level.ERROR;
    }
}
