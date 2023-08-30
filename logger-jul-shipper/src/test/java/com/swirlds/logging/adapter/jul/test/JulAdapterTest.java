package com.swirlds.logging.adapter.jul.test;

import com.swirlds.logging.test.api.LoggerMirror;
import com.swirlds.logging.test.api.LoggerTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JulAdapterTest {

    private LoggerMirror loggerMirror;

    @BeforeEach
    void setUp() {
        loggerMirror = LoggerTestSupport.createMirror(JulAdapterTest.class);
    }

    @AfterEach
    void tearDown() {
        loggerMirror.dispose();
    }


    @Test
    void testLogging() {
        //given
        java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger(JulAdapterTest.class.getName());

        //when
        julLogger.info("Hello World!");

        //then
        Assertions.assertEquals(1, loggerMirror.getEventCount());
    }
}
