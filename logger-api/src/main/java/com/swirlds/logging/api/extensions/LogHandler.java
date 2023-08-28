package com.swirlds.logging.api.extensions;

import com.swirlds.logging.api.Level;

public interface LogHandler {

    String getName();

    boolean isActive();

    void onLogEvent(LogEvent event);

    boolean isEnabled(String name, Level level);
}
