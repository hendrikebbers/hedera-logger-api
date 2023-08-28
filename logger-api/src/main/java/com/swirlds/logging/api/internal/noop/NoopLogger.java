package com.swirlds.logging.api.internal.noop;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.internal.AbstractLogger;
import java.util.Map;

public class NoopLogger extends AbstractLogger {

    public NoopLogger(String name) {
        super(name);
    }

    @Override
    protected void logImpl(Level level, String message, Throwable throwable) {
        // We do not want to have this removed by JIT for JMH tests
        if(level != null) {
            level.hashCode();
        }
        if(message != null) {
            message.hashCode();
        }
        if(throwable != null) {
            throwable.hashCode();
        }
    }

    @Override
    protected Logger withMarkerAndContext(Marker marker, Map<String, String> context) {
        return new NoopLogger(getName());
    }
}
