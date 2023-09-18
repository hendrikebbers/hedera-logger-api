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

package com.swirlds.logging.api.testfixture.internal;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.testfixture.LoggingMirror;
import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractLoggingMirror implements LoggingMirror {

    protected abstract LoggingMirror filter(Function<LogEvent, Boolean> filter);

    @Override
    public int getEventCount() {
        return getEvents().size();
    }

    @Override
    public LoggingMirror filterByLevel(Level level) {
        Function<LogEvent, Boolean> filter = event -> event.level().ordinal() >= level.ordinal();
        return filter(filter);
    }

    @Override
    public LoggingMirror filterByContext(String key, String value) {
        Function<LogEvent, Boolean> filter = event -> event.context().containsKey(key) && event.context().get(key)
                .equals(value);
        return filter(filter);
    }

    @Override
    public LoggingMirror filterByThread(String threadName) {
        Function<LogEvent, Boolean> filter = event -> Objects.equals(event.threadName(), threadName);
        return filter(filter);
    }

    @Override
    public LoggingMirror filterByLogger(String loggerName) {
        Function<LogEvent, Boolean> filter = event -> event.loggerName().startsWith(loggerName);
        return filter(filter);
    }
}
