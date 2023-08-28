package com.swirlds.logging.adapter.system;

import com.swirlds.logging.api.extensions.DefaultLoggerSystem;
import java.lang.System.Logger;
import java.lang.System.LoggerFinder;

public class LoggerFinderImpl extends LoggerFinder {

    @Override
    public Logger getLogger(String name, Module module) {
        return new SystemLoggerWrapper(DefaultLoggerSystem.getInstance().getLogger(name));
    }
}
