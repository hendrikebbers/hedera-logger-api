package com.swirlds.logging.api.extensions.event;

import com.swirlds.logging.api.Level;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.function.Consumer;

/**
 * A consumer that consumes log events.
 */
public interface LogEventConsumer extends Consumer<LogEvent> {

    /**
     * Checks if the consumer is enabled for the given name and level.
     *
     * @param name  the name
     * @param level the level
     * @return true if the consumer is enabled, false otherwise
     */
    default boolean isEnabled(@NonNull String name, @NonNull Level level) {
        return true;
    }
}
