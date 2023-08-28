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

package com.swirlds.logging.test.api.internal;

import com.swirlds.logging.api.internal.LogEvent;
import com.swirlds.logging.test.api.LoggerMirror;
import com.swirlds.logging.api.Level;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractLoggerMirror implements LoggerMirror {

    protected abstract LoggerMirror filter(Function<LogEvent, Boolean> filter);

    protected abstract List<LogEvent> getList();

    @Override
    public int getEventCount() {
        return getList().size();
    }

    @Override
    public LoggerMirror filterByLevel(Level level) {
        Function<LogEvent, Boolean> filter = event -> event.level().ordinal() >= level.ordinal();
        return filter(filter);
    }

    @Override
    public LoggerMirror filterByContext(String key, String value) {
        Function<LogEvent, Boolean> filter = event -> event.context().containsKey(key) && event.context().get(key)
                .equals(value);
        return filter(filter);
    }
}
