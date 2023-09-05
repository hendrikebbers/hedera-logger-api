package com.swirlds.logging.api.extensions.handler;

import com.swirlds.config.api.Configuration;

public interface LogHandlerFactory {

    LogHandler create(Configuration configuration);
}
