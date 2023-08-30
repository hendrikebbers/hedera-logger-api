package com.swirlds.logging.api.extensions.provider;

import com.swirlds.config.api.Configuration;
import java.util.function.Function;

public interface LogProviderFactory extends Function<Configuration, LogProvider> {
}
