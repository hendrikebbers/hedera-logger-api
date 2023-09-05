package com.swirlds.logging.handler.console;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.handler.AbstractSyncedHandler;
import com.swirlds.logging.format.LineBasedFormat;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.PrintWriter;

public class ConsoleHandler extends AbstractSyncedHandler {

    private final LineBasedFormat lineBasedFormat;

    private final PrintWriter printWriter = new PrintWriter(System.out);

    public ConsoleHandler(@NonNull Configuration configuration) {
        super("console", configuration);
        lineBasedFormat = new LineBasedFormat(printWriter);
    }

    @Override
    protected void handleSynced(@NonNull LogEvent event) {
        lineBasedFormat.print(event);
        printWriter.flush();
    }

    @Override
    protected void handleStopAndFinalize() {
        super.handleStopAndFinalize();
        printWriter.close();
    }
}
