package com.swirlds.logging.api.extensions.provider;


import com.swirlds.logging.api.extensions.LogEventConsumer;

public interface LogProvider {

    boolean isActive();

    String getName();

    void install(LogEventConsumer logEventConsumer);

}
