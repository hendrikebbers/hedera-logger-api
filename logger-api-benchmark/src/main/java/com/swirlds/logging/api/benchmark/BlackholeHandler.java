package com.swirlds.logging.api.benchmark;

import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import org.openjdk.jmh.infra.Blackhole;

public class BlackholeHandler implements LogHandler {

    private final Blackhole blackhole;

    public BlackholeHandler(Blackhole blackhole) {
        this.blackhole = blackhole;
    }

    @Override
    public String getName() {
        return BlackholeHandler.class.getSimpleName();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void accept(LogEvent event) {
        blackhole.consume(event);
    }
}
