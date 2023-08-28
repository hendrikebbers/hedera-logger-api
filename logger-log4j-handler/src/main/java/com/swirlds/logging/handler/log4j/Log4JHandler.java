package com.swirlds.logging.handler.log4j;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JHandler implements LogHandler {

    @Override
    public String getName() {
        return "Log4J Handler";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void onLogEvent(LogEvent event) {
        Logger log4jLogger = LogManager.getLogger(event.loggerName());

        log4jLogger.log(toLog4JLevel(event.level()), event.message(), event.throwable());
    }

    private org.apache.logging.log4j.Level toLog4JLevel(Level level) {
        if (level == Level.ERROR) {
            return org.apache.logging.log4j.Level.ERROR;
        }
        if (level == Level.WARN) {
            return org.apache.logging.log4j.Level.WARN;
        }
        if (level == Level.INFO) {
            return org.apache.logging.log4j.Level.INFO;
        }
        if (level == Level.DEBUG) {
            return org.apache.logging.log4j.Level.DEBUG;
        }
        if (level == Level.TRACE) {
            return org.apache.logging.log4j.Level.TRACE;
        }
        return org.apache.logging.log4j.Level.ALL;
    }

    @Override
    public boolean isEnabled(String name, Level level) {
        return true;
    }
}
