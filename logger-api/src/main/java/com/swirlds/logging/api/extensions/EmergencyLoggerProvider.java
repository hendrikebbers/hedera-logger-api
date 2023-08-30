package com.swirlds.logging.api.extensions;

import com.swirlds.logging.api.internal.util.EmergencyLogger;

public class EmergencyLoggerProvider {

    public static System.Logger getEmergencyLogger() {
        return EmergencyLogger.getInstance();
    }
}
