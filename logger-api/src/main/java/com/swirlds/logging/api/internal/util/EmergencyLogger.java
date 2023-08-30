package com.swirlds.logging.api.internal.util;

import com.swirlds.logging.api.internal.LoggingSystem;
import java.io.PrintStream;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmergencyLogger implements System.Logger {

    private final static System.Logger innerLogger = System.getLogger(LoggingSystem.class.getName());

    private final static ThreadLocal<Boolean> recursionGuard = new ThreadLocal<>();

    private final static String name = "EMERGENCY-LOGGER";

    private final static EmergencyLogger INSTANCE = new EmergencyLogger();
    public static final String RECURSION_ERROR = "RECURSION IN EMERGENCY LOGGER!!!!!!!!";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLoggable(Level level) {
        if (innerLogger != null) {
            return innerLogger.isLoggable(level);
        }
        return true;
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        final Boolean guard = recursionGuard.get();
        if (guard != null && guard) {
            simplePrint(RECURSION_ERROR + " [" + level + "] " + msg, thrown);
        } else {
            recursionGuard.set(true);
            try {
                if (innerLogger != null) {
                    innerLogger.log(level, bundle, msg, thrown);
                } else {
                    simplePrint("[" + level + "] " + msg, thrown);
                }
            } finally {
                recursionGuard.set(false);
            }
        }
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        final Boolean guard = recursionGuard.get();
        if (guard != null && guard) {
            simplePrint(RECURSION_ERROR + " [" + level + "] " + format + " -> " + params);
        } else {
            recursionGuard.set(true);
            try {
                if (innerLogger != null) {
                    innerLogger.log(level, bundle, format, params);
                } else {
                    simplePrint("[" + level + "] " + format + " -> " + params);
                }
            } finally {
                recursionGuard.set(false);
            }
        }
    }

    private static void simplePrint(String message, Throwable thrown) {
        final PrintStream printStream = Optional.ofNullable(System.err)
                .orElse(System.out);
        if (printStream != null) {
            printStream.println(message);
            if (thrown != null) {
                thrown.printStackTrace(printStream);
            }
        }
    }

    private static void simplePrint(String message) {
        simplePrint(message, null);
    }

    public static EmergencyLogger getInstance() {
        return INSTANCE;
    }

}
