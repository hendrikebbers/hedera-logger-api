package com.swirlds.logging.api.extensions;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * A log message that is part of a {@link LogEvent}. A message can be a simple String (see {@link SimpleLogMessage}) or
 * a parameterized String (see {@link ParameterizedLogMessage}).
 *
 * @see SimpleLogMessage
 * @see ParameterizedLogMessage
 */
public sealed interface LogMessage permits SimpleLogMessage, ParameterizedLogMessage {

    /**
     * Returns the message as a String. If the message is parameterized, the parameters are resolved.
     *
     * @return the message as a String
     */
    @NonNull
    String getMessage();
}
