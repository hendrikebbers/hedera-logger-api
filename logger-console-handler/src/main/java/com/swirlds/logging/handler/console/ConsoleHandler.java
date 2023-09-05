package com.swirlds.logging.handler.console;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.EmergencyLogger;
import com.swirlds.logging.api.extensions.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import com.swirlds.logging.format.LineBasedFormat;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConsoleHandler implements LogHandler {

    private final static EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    private final Configuration configuration;

    private final Lock writeLock = new ReentrantLock();

    private boolean stopped = false;

    private final LineBasedFormat lineBasedFormat;


    public ConsoleHandler(@NonNull Configuration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
        lineBasedFormat = new LineBasedFormat(new PrintWriter(System.out));
    }

    @Override
    public String getName() {
        return "Console Handler";
    }

    @Override
    public boolean isActive() {
        return configuration.getValue("logging.handler.console.enabled", Boolean.class, false);
    }

    @Override
    public void accept(final LogEvent event) {
        try {
            writeLock.lock();
            if (stopped) {
                EMERGENCY_LOGGER.log(event);
            } else {
                lineBasedFormat.print(event);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void stopAndFinalize() {
        try {
            writeLock.lock();
            stopped = true;
        } finally {
            writeLock.unlock();
        }
    }
}
