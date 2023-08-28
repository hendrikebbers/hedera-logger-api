package com.swirlds.logging.handler.log4j;

import org.apache.logging.log4j.spi.AbstractLoggerAdapter;
import org.apache.logging.log4j.spi.LoggerContext;

public class TestFactory extends AbstractLoggerAdapter {

    @Override
    protected Object newLogger(String name, LoggerContext context) {
        return null;
    }

    @Override
    protected LoggerContext getContext() {
        return null;
    }
}
