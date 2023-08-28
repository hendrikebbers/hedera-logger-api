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

package com.swirlds.logging.api.internal.console;

import com.swirlds.base.context.internal.GlobalContext;
import com.swirlds.base.context.internal.ThreadLocalContext;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.internal.AbstractLogger;
import com.swirlds.logging.api.internal.LogEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ConsoleLogger extends AbstractLogger {

    private final DateTimeFormatter formatter;

    private ConsoleLogger(String name, final Marker marker, Map<String, String> context) {
        super(name, marker, context);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public ConsoleLogger(String name) {
        this(name, null, Map.of());
    }

    protected void logImpl(Level level, String message, final Throwable throwable) {
        if (isEnabled(level)) {
            String timeStamp = LocalDateTime.now().format(formatter);
            String threadName = Thread.currentThread().getName();
            StringBuffer sb = new StringBuffer();
            sb.append(timeStamp)
                    .append(" ")
                    .append(level)
                    .append(" ")
                    .append("[")
                    .append(threadName)
                    .append("]")
                    .append(" ")
                    .append(getName())
                    .append(" - ")
                    .append(TextEffect.BOLD.apply(message));

            Marker marker = getMarker();
            if (marker != null) {
                sb.append(" - M:").append(marker.getName());
            }

            final Map<String, String> context = getContext();
            Map<String, String> mergedContext = new HashMap<>();
            mergedContext.putAll(GlobalContext.getContextMap());
            mergedContext.putAll(ThreadLocalContext.getContextMap());
            mergedContext.putAll(context);

            if (!mergedContext.isEmpty()) {
                sb.append(" - C:").append(mergedContext);
            }

            if (throwable != null) {
                sb.append(System.lineSeparator());
                StringBuffer stackTraceBuffer = new StringBuffer();
                PrintStream printStream = new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        stackTraceBuffer.append((char) b);
                    }
                });
                throwable.printStackTrace(printStream);
                sb.append(TextEffect.ITALIC.apply(stackTraceBuffer.toString()));
            }

            LogEvent logEvent = new LogEvent(message, LocalDateTime.now(), threadName, getName(), level, marker,
                    mergedContext, throwable);
            notifyListeners(logEvent);

            if (level == Level.ERROR) {
                System.out.println(TextEffect.RED.apply(sb.toString()));
            } else if (level == Level.WARN) {
                System.out.println(TextEffect.BRIGHT_RED.apply(sb.toString()));
            } else {
                System.out.println(sb.toString());
            }
        }
    }

    @Override
    protected Logger withMarkerAndContext(Marker marker, Map<String, String> context) {
        return new ConsoleLogger(getName(), marker, context);
    }
}
