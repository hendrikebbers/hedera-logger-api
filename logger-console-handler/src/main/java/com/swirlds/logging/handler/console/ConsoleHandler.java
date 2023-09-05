package com.swirlds.logging.handler.console;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.format.LineBasedFormat;
import com.swirlds.logging.handler.synced.AbstractSyncedHandler;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.PrintWriter;

public class ConsoleHandler extends AbstractSyncedHandler {

    private final LineBasedFormat lineBasedFormat;

    public ConsoleHandler(@NonNull Configuration configuration) {
        super("console", configuration);
        lineBasedFormat = new LineBasedFormat(new PrintWriter(System.out));
    }

    @Override
    protected void handleSynced(@NonNull LogEvent event) {
        lineBasedFormat.print(event);
    }
}
