package com.swirlds.logging.api.test;

import com.swirlds.base.testfixture.io.SystemErrProvider;
import com.swirlds.base.testfixture.io.WithSystemError;
import com.swirlds.base.testfixture.io.WithSystemOut;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.LogMessage;
import com.swirlds.logging.api.internal.emergency.EmergencyLoggerImpl;
import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@WithSystemError
@WithSystemOut
public class EmergencyLoggerTest {

    @Inject
    SystemErrProvider systemErrProvider;

    @Test
    void testLog1Line() {
        //given
        EmergencyLoggerImpl emergencyLogger = EmergencyLoggerImpl.getInstance();

        //when
        emergencyLogger.log(Level.INFO, "test");

        //then
        Assertions.assertEquals(1, systemErrProvider.getLines().count());
        Assertions.assertTrue(
                systemErrProvider.getLines().toList().get(0).endsWith(" INFO [main] EMERGENCY-LOGGER - test"));
    }

    @Test
    void testLogMultipleLines() {
        //given
        EmergencyLoggerImpl emergencyLogger = EmergencyLoggerImpl.getInstance();

        //when
        emergencyLogger.log(Level.INFO, "test");
        emergencyLogger.log(Level.INFO, "test1");
        emergencyLogger.log(Level.INFO, "test2");
        emergencyLogger.log(Level.INFO, "test3");

        //then
        Assertions.assertEquals(4, systemErrProvider.getLines().count());
    }

    @Test
    void testDefaultLevel() {
        //given
        EmergencyLoggerImpl emergencyLogger = EmergencyLoggerImpl.getInstance();

        //when
        emergencyLogger.log(Level.ERROR, "test");
        emergencyLogger.log(Level.WARN, "test");
        emergencyLogger.log(Level.INFO, "test");
        emergencyLogger.log(Level.DEBUG, "test");
        emergencyLogger.log(Level.TRACE, "test");

        //then
        Assertions.assertEquals(4, systemErrProvider.getLines().count(),
                "Only ERROR, WARNING, INFO and DEBUG should be logged by default");
    }

    @Test
    void loggerMustBe100Solid() {
        //given
        EmergencyLoggerImpl emergencyLogger = EmergencyLoggerImpl.getInstance();
        emergencyLogger.publishLoggedEvents(); //clear the queue

        //when
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(Level.INFO, null));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(null, "message"));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(null, null));

        Assertions.assertDoesNotThrow(() -> emergencyLogger.logNPE(null));

        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(null, "message", new RuntimeException()));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(Level.INFO, null, new RuntimeException()));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(Level.INFO, "message", null));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(null, null, null));

        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(null));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(new LogEvent(Level.INFO, "loggerName", "threadName",
                Instant.now(), (String) null,
                new RuntimeException(), new Marker("marker"),
                Map.of()
        )));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(new LogEvent(Level.INFO, "loggerName", "threadName",
                Instant.now(), (LogMessage) null,
                new RuntimeException(), new Marker("marker"),
                Map.of()
        )));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(new LogEvent(Level.INFO, "loggerName", "threadName",
                null, "message",
                new RuntimeException(), new Marker("marker"),
                Map.of()
        )));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(new LogEvent(Level.INFO, "loggerName", null,
                Instant.now(), "message",
                new RuntimeException(), new Marker("marker"),
                Map.of()
        )));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(new LogEvent(Level.INFO, null, "threadName",
                Instant.now(), "message",
                new RuntimeException(), new Marker("marker"),
                Map.of()
        )));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(new LogEvent(null, "loggerName", "threadName",
                Instant.now(), "message",
                new RuntimeException(), new Marker("marker"),
                Map.of()
        )));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(new LogEvent(Level.INFO, "loggerName", "threadName",
                Instant.now(), "message",
                new RuntimeException(), null,
                Map.of()
        )));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(new LogEvent(Level.INFO, "loggerName", "threadName",
                Instant.now(), "message",
                new RuntimeException(), new Marker("marker"),
                null
        )));
        Assertions.assertDoesNotThrow(() -> emergencyLogger.log(new LogEvent(Level.INFO, "loggerName", "threadName",
                Instant.now(), "message",
                null, new Marker("marker"),
                Map.of()
        )));

        //then
        final List<String> allLines = systemErrProvider.getLines().toList();
        final List<String> onlyBasicLines = systemErrProvider.getLines()
                .filter(line -> !line.startsWith("\tat "))
                .filter(line -> !line.startsWith("java.lang"))
                .toList();
        final List<String> onlyTrace = systemErrProvider.getLines()
                .filter(line -> line.startsWith("\tat "))
                .toList();
        final List<String> onlyException = systemErrProvider.getLines()
                .filter(line -> line.startsWith("java.lang"))
                .toList();

        Assertions.assertEquals(allLines.size(), onlyBasicLines.size() + onlyTrace.size() + onlyException.size());
        Assertions.assertEquals(19, onlyBasicLines.size());
        Assertions.assertEquals(13, onlyException.size());
        Assertions.assertTrue(onlyException.get(0).startsWith("java.lang.NullPointerException"));
        Assertions.assertTrue(onlyException.get(1).startsWith("java.lang.RuntimeException"));
        Assertions.assertTrue(onlyException.get(2).startsWith("java.lang.RuntimeException"));
        Assertions.assertTrue(onlyException.get(3).startsWith("java.lang.NullPointerException"));
        Assertions.assertTrue(onlyException.get(4).startsWith("java.lang.RuntimeException"));
        Assertions.assertTrue(onlyException.get(5).startsWith("java.lang.RuntimeException"));
        Assertions.assertTrue(onlyException.get(6).startsWith("java.lang.RuntimeException"));
        Assertions.assertTrue(onlyException.get(7).startsWith("java.lang.RuntimeException"));
        Assertions.assertTrue(onlyException.get(8).startsWith("java.lang.RuntimeException"));
        Assertions.assertTrue(onlyException.get(9).startsWith("java.lang.NullPointerException"));
        Assertions.assertTrue(onlyException.get(10).startsWith("java.lang.RuntimeException"));
        Assertions.assertTrue(onlyException.get(11).startsWith("java.lang.RuntimeException"));
        Assertions.assertTrue(onlyException.get(12).startsWith("java.lang.RuntimeException"));

        Assertions.assertTrue(onlyTrace.size() > 39);

        final List<LogEvent> loggedEvents = emergencyLogger.publishLoggedEvents();

        Assertions.assertEquals(19, loggedEvents.size());
    }
}
