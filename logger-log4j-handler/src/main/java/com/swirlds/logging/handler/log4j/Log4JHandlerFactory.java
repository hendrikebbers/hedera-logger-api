package com.swirlds.logging.handler.log4j;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogHandler;
import com.swirlds.logging.api.extensions.LogHandlerFactory;

public class Log4JHandlerFactory implements LogHandlerFactory {
    
    @Override
    public LogHandler create(Configuration configuration) {
        return new Log4JHandler(configuration);
    }
}
