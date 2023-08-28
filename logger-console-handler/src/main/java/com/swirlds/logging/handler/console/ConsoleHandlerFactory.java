package com.swirlds.logging.handler.console;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogHandler;
import com.swirlds.logging.api.extensions.LogHandlerFactory;

public class ConsoleHandlerFactory implements LogHandlerFactory {

    @Override
    public LogHandler create(Configuration configuration) {
        return new ConsoleHandler(configuration);
    }
}