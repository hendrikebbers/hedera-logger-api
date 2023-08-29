package com.swirlds.logging.adapter.system;

import com.swirlds.logging.api.extensions.DefaultLoggerSystem;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;

public class EmergencyLogger implements System.Logger {

    private final static String LOGGER_NAME = "EMERGENCY-LOGGER";

    private final static String LEVEL_PROPERTY_NAME = "com.swirlds.logging.emergency.level";

    private final static Level SUPPORTED_LEVEL;

    static {
        final String property = System.getProperty(LEVEL_PROPERTY_NAME);
        if (property == null) {
            SUPPORTED_LEVEL = Level.DEBUG;
        } else if (Objects.equals(property.toUpperCase(), "TRACE")) {
            SUPPORTED_LEVEL = Level.TRACE;
        } else if (Objects.equals(property.toUpperCase(), "DEBUG")) {
            SUPPORTED_LEVEL = Level.DEBUG;
        } else if (Objects.equals(property.toUpperCase(), "INFO")) {
            SUPPORTED_LEVEL = Level.INFO;
        } else if (Objects.equals(property.toUpperCase(), "WARN")) {
            SUPPORTED_LEVEL = Level.WARNING;
        } else if (Objects.equals(property.toUpperCase(), "ERROR")) {
            SUPPORTED_LEVEL = Level.ERROR;
        } else {
            SUPPORTED_LEVEL = Level.DEBUG;
        }
    }

    private final String name;


    public EmergencyLogger(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        if (DefaultLoggerSystem.isInitialized()) {
            return DefaultLoggerSystem.getInstance().getLogger(name).getName();
        }
        return LOGGER_NAME + ":" + name;
    }

    @Override
    public boolean isLoggable(Level level) {
        Objects.requireNonNull(level, "level must not be null");
        if (DefaultLoggerSystem.isInitialized()) {
            return DefaultLoggerSystem.getInstance().getLogger(name)
                    .isEnabled(SystemLoggerConverter.convertFromSystemLogger(level));
        }
        return SUPPORTED_LEVEL.getSeverity() <= level.getSeverity();
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        if (isLoggable(Objects.requireNonNull(level, "level must not be null"))) {
            final String message;
            if (bundle != null && bundle.containsKey(msg)) {
                message = bundle.getString(msg);
            } else {
                message = msg;
            }
            if (DefaultLoggerSystem.isInitialized()) {
                DefaultLoggerSystem.getInstance().getLogger(name)
                        .logImpl(SystemLoggerConverter.convertFromSystemLogger(level), msg, thrown);
            } else {
                System.err.println("[" + LOGGER_NAME + ":" + level + ":" + name + "] " + message);
                if (thrown != null) {
                    thrown.printStackTrace(System.err);
                }
            }
        }
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        if (isLoggable(Objects.requireNonNull(level, "level must not be null"))) {
            if (bundle != null && bundle.containsKey(format)) {
                final String translated = bundle.getString(format);
                final String message = MessageFormat.format(translated, params);
                log(level, EmptyResourceBundle.getInstance(), message, (Throwable) null);
            } else {
                final String message = MessageFormat.format(format, params);
                log(level, EmptyResourceBundle.getInstance(), message, (Throwable) null);
            }
        }
    }
}
