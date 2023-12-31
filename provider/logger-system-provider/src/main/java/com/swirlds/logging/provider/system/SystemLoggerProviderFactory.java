package com.swirlds.logging.provider.system;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;

public class SystemLoggerProviderFactory implements LogProviderFactory {
    @Override
    public LogProvider create(Configuration configuration) {
        return new SystemLoggerProvider(configuration);
    }
}
