package com.swirlds.logging.test.api;

import com.swirlds.logging.api.internal.AbstractLogger;
import com.swirlds.logging.api.internal.LoggerListener;
import com.swirlds.logging.api.internal.LoggerManager;
import com.swirlds.logging.test.api.internal.AbstractLoggerMirror;
import com.swirlds.logging.test.api.internal.LoggerMirrorImpl;

public class LoggerTestSupport {

    public static LoggerMirror createMirror(Class cls) {
        return createMirror(cls.getName());
    }

    public static LoggerMirror createMirror(String name) {
        LoggerMirrorImpl mirror = new LoggerMirrorImpl(name);
        AbstractLogger logger = LoggerManager.getInstance().getLogger(name);
        logger.addListener(mirror);
        return mirror;
    }

    public static void disposeMirror(LoggerMirror mirror) {
        if(mirror instanceof LoggerMirrorImpl loggerMirror) {
            String name = loggerMirror.getName();
            AbstractLogger logger = LoggerManager.getInstance().getLogger(name);
            logger.removeListener(loggerMirror);
        }
    }
}
