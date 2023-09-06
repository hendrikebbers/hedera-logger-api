package com.swirlds.logging.adapter.system.test;

import com.swirlds.logging.api.testfixture.LoggingMirror;
import com.swirlds.logging.api.testfixture.WithLoggingMirror;
import jakarta.inject.Inject;
import java.lang.System.Logger.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@WithLoggingMirror
public class SystemAdapterTest {

    @Inject
    private LoggingMirror loggingMirror;

    @Test
    void test() {
        //given
        final System.Logger systemLogger = System.getLogger(SystemAdapterTest.class.getName());

        //when
        systemLogger.log(Level.INFO, "Hello World!");

        //then
        Assertions.assertEquals(1, loggingMirror.getEventCount());
    }
}
