package com.swirlds.logging.api.extensions.provider;

import com.swirlds.config.api.Configuration;

public interface LogProviderFactory {

    LogProvider create(Configuration configuration);

}
