package com.swirlds.logging.api.extensions.handler;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.LogEventConsumer;

public interface LogHandler extends LogEventConsumer {

    String getName();

    boolean isActive();

    default void stopAndFinalize() {

    }

    @Override
    default boolean isEnabled(String name, Level level) {
        return isActive();
    }
}
