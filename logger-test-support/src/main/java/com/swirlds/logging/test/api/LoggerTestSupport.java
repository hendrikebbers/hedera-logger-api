package com.swirlds.logging.test.api;

import com.swirlds.logging.api.extensions.DefaultLoggerSystem;
import com.swirlds.logging.api.extensions.LogListener;
import com.swirlds.logging.test.api.internal.LoggerMirrorImpl;

public class LoggerTestSupport {

    public static LoggerMirror createMirror(Class cls) {
        return createMirror(cls.getName());
    }

    public static LoggerMirror createMirror(String name) {
        LoggerMirrorImpl mirror = new LoggerMirrorImpl(name);
        DefaultLoggerSystem.getInstance().addListener(mirror);
        return mirror;
    }

    public static void disposeMirror(LoggerMirror mirror) {
        if (mirror instanceof LogListener loggerMirror) {
            DefaultLoggerSystem.getInstance().removeListener(loggerMirror);
        }
    }
}
