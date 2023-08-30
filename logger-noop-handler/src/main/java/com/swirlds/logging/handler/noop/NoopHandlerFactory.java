package com.swirlds.logging.handler.noop;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;

public class NoopHandlerFactory implements LogHandlerFactory {

    @Override
    public LogHandler create(Configuration configuration) {
        return new NoopHandler(configuration);
    }
}
