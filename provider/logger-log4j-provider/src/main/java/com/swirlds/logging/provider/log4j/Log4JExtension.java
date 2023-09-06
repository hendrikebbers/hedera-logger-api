package com.swirlds.logging.provider.log4j;

import org.apache.logging.log4j.spi.Provider;

public class Log4JExtension extends Provider {
    public Log4JExtension() {
        super(15, "2.6.0", Log4JContextFactory.class, Log4JThreadContextMap.class);
    }
}
