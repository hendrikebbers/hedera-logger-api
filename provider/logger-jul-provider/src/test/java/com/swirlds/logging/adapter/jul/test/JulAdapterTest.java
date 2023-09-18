package com.swirlds.logging.adapter.jul.test;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.testfixture.LoggingMirror;
import com.swirlds.logging.api.testfixture.WithLoggingMirror;
import jakarta.inject.Inject;
import java.time.Instant;
import java.util.Map;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@WithLoggingMirror
public class JulAdapterTest {

    @Inject
    LoggingMirror loggingMirror;

    @Test
    void testLogging() {
        //given
        final Logger julLogger = Logger.getLogger(JulAdapterTest.class.getName());
        final Instant start = Instant.now();

        //when
        julLogger.info("Hello World!");

        //then
        Assertions.assertEquals(1, loggingMirror.getEventCount());
        LogEvent event = loggingMirror.getEvents().get(0);
        Assertions.assertEquals("Hello World!", event.message().getMessage());
        Assertions.assertEquals(Level.INFO, event.level());
        Assertions.assertEquals(JulAdapterTest.class.getName(), event.loggerName());
        Assertions.assertEquals(Thread.currentThread().getName(), event.threadName());
        Assertions.assertEquals(null, event.throwable());
        Assertions.assertEquals(null, event.marker());
        Assertions.assertEquals(Map.of(), event.context());
        Assertions.assertTrue(event.timestamp().isAfter(start));
        Assertions.assertTrue(event.timestamp().isBefore(Instant.now()));
    }
}
