package com.swirlds.logging.adapter.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerRegistry;

public class LoggerContextImpl implements LoggerContext {

    private final LoggerRegistry<ExtendedLogger> loggerRegistry = new LoggerRegistry<>();


    @Override
    public Object getExternalContext() {
        return null;
    }

    @Override
    public ExtendedLogger getLogger(String name) {
        return null;
    }

    @Override
    public ExtendedLogger getLogger(String name, MessageFactory messageFactory) {
        return null;
    }

    @Override
    public boolean hasLogger(String name) {
        return loggerRegistry.hasLogger(name);
    }

    @Override
    public boolean hasLogger(String name, MessageFactory messageFactory) {
        return loggerRegistry.hasLogger(name, messageFactory);
    }

    @Override
    public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
        return loggerRegistry.hasLogger(name, messageFactoryClass);
    }
}
