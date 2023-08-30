package com.swirlds.logging.api.extensions.shipper;


import com.swirlds.logging.api.extensions.LogEventConsumer;

public interface LogShipper {

    boolean isActive();

    String getName();

    void install(LogEventConsumer logEventConsumer);

}
