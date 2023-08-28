package com.swirlds.logging.adapter.system;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogAdapter;

public class SystemLoggerAdapter implements LogAdapter {

    @Override
    public boolean isActive(Configuration configuration) {
        return true;
    }

    @Override
    public String getName() {
        return "Adapter for java.lang.System.Logger";
    }

    @Override
    public void install() {
        // no-op
    }
}
