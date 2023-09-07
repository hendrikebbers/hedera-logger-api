package com.swirlds.logging.api.extensions;

import com.swirlds.logging.api.Level;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

public interface EmergencyLogger {

    void logNPE(@NonNull String nameOfNullParam);

    void log(LogEvent event);

    void log(@NonNull Level level, @NonNull String message);

    void log(@NonNull Level level, @NonNull String message, @Nullable Throwable thrown);

    boolean isLoggable(@NonNull Level level);
}
