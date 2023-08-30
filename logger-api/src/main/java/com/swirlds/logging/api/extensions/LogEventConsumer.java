package com.swirlds.logging.api.extensions;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.LogEvent;
import java.util.function.Consumer;

public interface LogEventConsumer extends Consumer<LogEvent> {

    boolean isEnabled(String name, Level level);
}
