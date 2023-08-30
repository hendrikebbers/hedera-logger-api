package com.swirlds.logging.adapter.system;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;

public class SystemLoggerProviderFactory implements LogProviderFactory {
    @Override
    public LogProvider apply(Configuration configuration) {
        return new SystemLoggerProvider(configuration);
    }
}
