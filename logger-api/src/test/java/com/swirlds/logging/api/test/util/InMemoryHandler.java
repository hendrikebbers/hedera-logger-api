package com.swirlds.logging.api.test.util;

import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryHandler implements LogHandler {

    private final List<LogEvent> events = new CopyOnWriteArrayList<>();

    @Override
    public String getName() {
        return InMemoryHandler.class.getSimpleName();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void accept(LogEvent event) {
        events.add(event);
    }

    public List<LogEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }
}
