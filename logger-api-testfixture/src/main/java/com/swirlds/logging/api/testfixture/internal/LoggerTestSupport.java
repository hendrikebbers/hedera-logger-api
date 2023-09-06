package com.swirlds.logging.api.testfixture.internal;

import com.swirlds.logging.api.extensions.LogListener;
import com.swirlds.logging.api.internal.DefaultLoggingSystem;
import com.swirlds.logging.api.testfixture.LoggingMirror;

public class LoggerTestSupport {

    public static LoggingMirror createMirror() {
        LoggingMirrorImpl mirror = new LoggingMirrorImpl();
        DefaultLoggingSystem.getInstance().addListener(mirror);
        return mirror;
    }

    public static void disposeMirror(LoggingMirror mirror) {
        if (mirror instanceof LogListener loggerMirror) {
            DefaultLoggingSystem.getInstance().removeListener(loggerMirror);
        }
    }
}
