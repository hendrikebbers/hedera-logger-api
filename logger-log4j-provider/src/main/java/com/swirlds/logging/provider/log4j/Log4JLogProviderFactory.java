package com.swirlds.logging.provider.log4j;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;

public class Log4JLogProviderFactory implements LogProviderFactory {

    @Override
    public LogProvider create(Configuration configuration) {
        return new Log4JLogProvider();
    }
}
