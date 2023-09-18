package com.swirlds.logging.api.test;

import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.event.LogEventConsumer;

public class DummyConsumer implements LogEventConsumer {
    @Override
    public void accept(LogEvent event) {

    }
}
