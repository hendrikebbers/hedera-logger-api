package com.swirlds.logging.adapter.jul;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.provider.LogProvider;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;

public class JulProviderFactory implements LogProviderFactory {
    @Override
    public LogProvider apply(Configuration configuration) {
        return new JulProvider(configuration);
    }
}
