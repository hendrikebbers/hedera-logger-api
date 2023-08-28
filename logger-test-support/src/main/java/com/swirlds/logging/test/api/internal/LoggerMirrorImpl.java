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

import static com.swirlds.logging.test.api.LoggerTestSupport.*;

import com.swirlds.logging.api.internal.LogEvent;
import com.swirlds.logging.api.internal.LoggerListener;
import com.swirlds.logging.api.internal.LoggerManager;
import com.swirlds.logging.test.api.LoggerMirror;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class LoggerMirrorImpl extends AbstractLoggerMirror implements LoggerListener {

    private final List<LogEvent> events = new CopyOnWriteArrayList<>();

    private final String name;

    public LoggerMirrorImpl(String name) {
        this.name = name;
    }

    @Override
    public void onLogEvent(LogEvent event) {
        events.add(event);
    }

    @Override
    public void dispose() {
        disposeMirror(this);
    }


    public String getName() {
        return name;
    }

    @Override
    protected LoggerMirror filter(Function<LogEvent, Boolean> filter) {
        return new FilteredLoggerMirror(events, filter, () -> dispose());
    }

    @Override
    protected List<LogEvent> getList() {
        return events;
    }
}
