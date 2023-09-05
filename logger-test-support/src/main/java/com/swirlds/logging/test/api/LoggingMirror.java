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

package com.swirlds.logging.test.api;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.LogEvent;
import java.util.List;

public interface LoggingMirror extends AutoCloseable {

    int getEventCount();

    @Override
    default void close() throws Exception {
        dispose();
    }

    void dispose();

    LoggingMirror filterByLevel(Level level);

    LoggingMirror filterByContext(String key, String value);

    default LoggingMirror filterByCurrentThread() {
        return filterByThread(Thread.currentThread().getName());
    }

    LoggingMirror filterByThread(String threadName);

    default LoggingMirror filterByLogger(Class<?> clazz) {
        return filterByLogger(clazz.getName());
    }

    LoggingMirror filterByLogger(String loggerName);

    List<LogEvent> getEvents();
}
