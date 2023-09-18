/*
 * Copyright (C) 2023 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.swirlds.logging.api.extensions;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Marker;
import java.time.Instant;
import java.util.Map;

public record LogEvent(Level level, String loggerName, String threadName, Instant timestamp, String message,
                       Throwable throwable, Marker marker,
                       Map<String, String> context
) {

    public LogEvent(String message, String loggerName, Level level) {
        this(message, loggerName, level, null);
    }

    public LogEvent(String message, String loggerName, Level level, Throwable throwable) {
        this(level, loggerName, Thread.currentThread().getName(), Instant.now(), message, throwable, null,
                Map.of()
        );
    }

    public static LogEvent createCopyWithDifferentContext(LogEvent logEvent,
            Map<String, String> context) {
        return new LogEvent(logEvent.level, logEvent.loggerName, logEvent.threadName, logEvent.timestamp,
                logEvent.message,
                logEvent.throwable, logEvent.marker, context);
    }

    public static LogEvent createCopyWithDifferentName(LogEvent logEvent,
            String loggerName) {
        return new LogEvent(logEvent.level, loggerName, logEvent.threadName, logEvent.timestamp, logEvent.message,
                logEvent.throwable, logEvent.marker, logEvent.context);
    }
}
