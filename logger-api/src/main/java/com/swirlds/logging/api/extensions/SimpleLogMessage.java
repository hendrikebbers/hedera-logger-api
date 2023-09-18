package com.swirlds.logging.api.extensions;

public record SimpleLogMessage(String message) implements LogMessage {

    @Override
    public String getMessage() {
        return message;
    }
}
