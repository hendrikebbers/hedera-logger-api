package com.swirlds.logging.adapter.system.test;

import com.swirlds.logging.test.api.LoggerMirror;
import com.swirlds.logging.test.api.LoggerTestSupport;
import java.lang.System.Logger.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SystemAdapterTest {

    private LoggerMirror loggerMirror;

    @BeforeEach
    void setUp() {
        loggerMirror = LoggerTestSupport.createMirror(SystemAdapterTest.class);
    }

    @AfterEach
    void tearDown() {
        loggerMirror.dispose();
    }


    @Test
    void test() {
        //given
        final System.Logger systemLogger = System.getLogger(SystemAdapterTest.class.getName());

        //when
        systemLogger.log(Level.INFO, "Hello World!");

        //then
        Assertions.assertEquals(1, loggerMirror.getEventCount());
    }
}
