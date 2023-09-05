package com.swirlds.logging.api.extensions.handler;

import com.swirlds.logging.api.extensions.LogEventConsumer;

public interface LogHandler extends LogEventConsumer {

    default String getName() {
        return getClass().getSimpleName();
    }

    boolean isActive();

    default void stopAndFinalize() {

    }

}
