package com.swirlds.logging.handler.log4j;

import com.swirlds.logging.api.extensions.LogEvent;
import org.apache.logging.log4j.core.LogEventListener;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;

public class Log4jForwarder extends LogEventListener {

    LoggerContext loggerContext;

    public Log4jForwarder() {
        this.loggerContext = LoggerContext.getContext(false);
    }

    public void log(LogEvent event) {
        final Logger logger = loggerContext.getLogger(event.loggerName());
        if (loggerContext.isStarted()) {
            logger.setLevel(Log4jConverter.convertToLog4J(event.level()));
            log(Log4jConverter.convertToLog4J(event));
        }
    }
}
