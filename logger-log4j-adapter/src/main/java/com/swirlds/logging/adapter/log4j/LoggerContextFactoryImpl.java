package com.swirlds.logging.adapter.log4j;

import java.net.URI;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

public class LoggerContextFactoryImpl implements LoggerContextFactory {

    private final LoggerContext context = new LoggerContextImpl();

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext) {
        return context;
    }

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext,
            URI configLocation, String name) {
        return context;
    }

    @Override
    public void removeContext(LoggerContext context) {

    }
}
