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

import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.test.api.LoggerMirror;
import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;

public class FilteredLoggerMirror extends AbstractLoggerMirror {

    private final Function<LogEvent, Boolean> filter;

    private final List<LogEvent> list;

    private Runnable disposeAction;

    public FilteredLoggerMirror(final List<LogEvent> list, Function<LogEvent, Boolean> filter, Runnable disposeAction) {
        this.list = list;
        this.filter = filter;
        this.disposeAction = disposeAction;
    }

    @Override
    protected List<LogEvent> getList() {
        return list.stream().filter(e -> filter.apply(e)).toList();
    }

    @Override
    protected LoggerMirror filter(Function<LogEvent, Boolean> filter) {
        List liveList = new AbstractList() {
            @Override
            public int size() {
                return list.size();
            }

            @Override
            public Object get(int index) {
                return list.get(index);
            }
        };
        return new FilteredLoggerMirror(liveList, filter, disposeAction);
    }

    @Override
    public void dispose() {
        disposeAction.run();
    }

}
