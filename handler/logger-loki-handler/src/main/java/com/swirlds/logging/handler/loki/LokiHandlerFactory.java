package com.swirlds.logging.handler.loki;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;

public class LokiHandlerFactory implements LogHandlerFactory {
    @Override
    public LogHandler create(Configuration configuration) {
        return new LokiHandler(configuration);
    }
}
