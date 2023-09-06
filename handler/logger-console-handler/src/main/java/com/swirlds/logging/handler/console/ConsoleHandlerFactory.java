package com.swirlds.logging.handler.console;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import com.swirlds.logging.api.extensions.handler.LogHandlerFactory;
import edu.umd.cs.findbugs.annotations.NonNull;

public class ConsoleHandlerFactory implements LogHandlerFactory {

    @Override
    public LogHandler create(@NonNull Configuration configuration) {
        return new ConsoleHandler(configuration);
    }
}
