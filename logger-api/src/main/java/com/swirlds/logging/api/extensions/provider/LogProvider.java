package com.swirlds.logging.api.extensions.provider;


import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogEventConsumer;

public interface LogProvider {

    boolean isActive(Configuration configuration);

    String getName();

    void install(LogEventConsumer logEventConsumer);

}
