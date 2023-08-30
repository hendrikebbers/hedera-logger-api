package com.swirlds.logging.handler.log4j;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Marker;
import java.util.Objects;

public class Log4jConverter {

    public static Level convertFromLog4J(org.apache.logging.log4j.Level level) {
        Objects.requireNonNull(level, "level must not be null");
        if (Objects.equals(org.apache.logging.log4j.Level.ERROR, level)) {
            return Level.ERROR;
        }
        if (Objects.equals(org.apache.logging.log4j.Level.WARN, level)) {
            return Level.WARN;
        }
        if (Objects.equals(org.apache.logging.log4j.Level.INFO, level)) {
            return Level.INFO;
        }
        if (Objects.equals(org.apache.logging.log4j.Level.DEBUG, level)) {
            return Level.DEBUG;
        }
        if (Objects.equals(org.apache.logging.log4j.Level.TRACE, level)) {
            return Level.TRACE;
        }
        return Level.ERROR;
    }

    public static org.apache.logging.log4j.Level convertToLog4J(Level level) {
        Objects.requireNonNull(level, "level must not be null");
        if (Objects.equals(Level.ERROR, level)) {
            return org.apache.logging.log4j.Level.ERROR;
        }
        if (Objects.equals(Level.WARN, level)) {
            return org.apache.logging.log4j.Level.WARN;
        }
        if (Objects.equals(Level.INFO, level)) {
            return org.apache.logging.log4j.Level.INFO;
        }
        if (Objects.equals(Level.DEBUG, level)) {
            return org.apache.logging.log4j.Level.DEBUG;
        }
        if (Objects.equals(Level.TRACE, level)) {
            return org.apache.logging.log4j.Level.TRACE;
        }
        return org.apache.logging.log4j.Level.ERROR;
    }

    public static org.apache.logging.log4j.Marker convertToLog4J(Marker marker) {
        return null;
    }

    public static Marker convertFromLog4J(org.apache.logging.log4j.Marker marker) {
        return null;
    }

}
