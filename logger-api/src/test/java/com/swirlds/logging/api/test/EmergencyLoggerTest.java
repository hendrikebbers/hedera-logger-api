package com.swirlds.logging.api.test;

import com.swirlds.base.testfixture.io.SystemErrProvider;
import com.swirlds.base.testfixture.io.WithSystemError;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.internal.emergency.EmergencyLoggerImpl;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@WithSystemError
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

}
