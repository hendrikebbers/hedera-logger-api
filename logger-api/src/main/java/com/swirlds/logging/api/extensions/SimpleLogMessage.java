package com.swirlds.logging.api.extensions;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * A simple log message that is just a String that does not need to be handled / parsed / ... in any specific way.
 *
 * @param message The message
 * @see LogMessage
 */
public record SimpleLogMessage(@NonNull String message) implements LogMessage {

    @Override
    public String getMessage() {
        return message;
    }
}
