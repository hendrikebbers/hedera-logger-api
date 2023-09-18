package com.swirlds.logging.api.extensions;

import com.swirlds.logging.api.internal.format.MessageFormatter;

public record ParameterizedLogMessage(String message, Object... args) implements LogMessage {

    @Override
    public String getMessage() {
        return MessageFormatter.arrayFormat(message, args);
    }
    
}