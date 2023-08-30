package com.swirlds.logging.api.extensions;

import com.swirlds.logging.api.Level;
import java.util.function.Consumer;

public interface LogEventConsumer extends Consumer<LogEvent> {

    default boolean isEnabled(String name, Level level) {
        return true;
    }
}
