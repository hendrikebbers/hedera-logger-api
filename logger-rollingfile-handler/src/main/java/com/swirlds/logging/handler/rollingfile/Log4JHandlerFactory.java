package com.swirlds.logging.handler.rollingfile;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;

public class Log4JHandlerFactory implements LogHandlerFactory {

    @Override
    public LogHandler create(Configuration configuration) {
        return new Log4JHandler(configuration);
    }
}
