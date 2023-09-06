package com.swirlds.logging.api.test;

import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogEventConsumer;

public class DummyConsumer implements LogEventConsumer {
    @Override
    public void accept(LogEvent event) {

    }
}
