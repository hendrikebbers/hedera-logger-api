package com.swirlds.logging.test.api;

import com.swirlds.logging.api.extensions.LogListener;
import com.swirlds.logging.api.internal.DefaultLoggingSystem;
import com.swirlds.logging.test.api.internal.LoggerMirrorImpl;

public class LoggerTestSupport {

    public static LoggerMirror createMirror(Class<?> cls) {
        return createMirror(cls.getName());
    }

    public static LoggerMirror createMirror(String name) {
        LoggerMirrorImpl mirror = new LoggerMirrorImpl(name);
        DefaultLoggingSystem.getInstance().addListener(mirror);
        return mirror;
    }

    public static void disposeMirror(LoggerMirror mirror) {
        if (mirror instanceof LogListener loggerMirror) {
            DefaultLoggingSystem.getInstance().removeListener(loggerMirror);
        }
    }
}
