package com.swirlds.logging.adapter.system.test;

import com.swirlds.logging.test.api.LoggingMirror;
import com.swirlds.logging.test.api.internal.LoggerTestSupport;
import java.lang.System.Logger.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SystemAdapterTest {

    private LoggingMirror loggingMirror;

    @BeforeEach
    void setUp() {
        loggingMirror = LoggerTestSupport.createMirror(SystemAdapterTest.class);
    }

    @AfterEach
    void tearDown() {
        loggingMirror.dispose();
    }


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
