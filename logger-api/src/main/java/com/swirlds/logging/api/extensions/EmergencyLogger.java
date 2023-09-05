package com.swirlds.logging.api.extensions;

public interface EmergencyLogger extends System.Logger {

    void logNPE(String nameOfNullParam);

    void log(LogEvent event);
}
