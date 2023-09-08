package com.swirlds.logging.api.extensions.emergency;

import com.swirlds.logging.api.internal.emergency.EmergencyLoggerImpl;

public class EmergencyLoggerProvider {

    public static EmergencyLogger getEmergencyLogger() {
        return EmergencyLoggerImpl.getInstance();
    }
}
