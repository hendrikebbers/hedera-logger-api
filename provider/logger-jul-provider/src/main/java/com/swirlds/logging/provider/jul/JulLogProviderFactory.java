package com.swirlds.logging.provider.jul;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;

/**
 * Factory for {@link JulLogProvider}.
 */
public class JulLogProviderFactory implements LogProviderFactory {
    @Override
    public LogProvider create(Configuration configuration) {
        return new JulLogProvider(configuration);
    }
}
