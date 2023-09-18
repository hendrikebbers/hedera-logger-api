package com.swirlds.logging.api.extensions;

public sealed interface LogMessage permits SimpleLogMessage, ParameterizedLogMessage {

    String getMessage();
}
