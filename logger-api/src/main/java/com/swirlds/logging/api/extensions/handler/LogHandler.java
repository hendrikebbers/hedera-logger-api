package com.swirlds.logging.api.extensions.handler;

import com.swirlds.logging.api.extensions.LogEventConsumer;

public interface LogHandler extends LogEventConsumer {

    String getName();

    boolean isActive();
}
