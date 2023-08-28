package com.swirlds.logging.api.extensions;


import com.swirlds.config.api.Configuration;

public interface LogAdapter {

    boolean isActive(Configuration configuration);

    String getName();

    void install();

}
