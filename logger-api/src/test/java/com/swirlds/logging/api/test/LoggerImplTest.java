package com.swirlds.logging.api.test;

import com.swirlds.logging.api.internal.LoggerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoggerImplTest {

    @Test
    void testSimpleLogger() {
        //given
        LoggerImpl logger = new LoggerImpl("test-name", new DummyConsumer());

        //when
        final String name = logger.getName();
        final boolean traceEnabled = logger.isTraceEnabled();
        final boolean debugEnabled = logger.isDebugEnabled();
        final boolean infoEnabled = logger.isInfoEnabled();
        final boolean warnEnabled = logger.isWarnEnabled();
        final boolean errorEnabled = logger.isErrorEnabled();

        //then
        Assertions.assertEquals("test-name", name);
        Assertions.assertTrue(traceEnabled);
        Assertions.assertTrue(debugEnabled);
        Assertions.assertTrue(infoEnabled);
        Assertions.assertTrue(warnEnabled);
        Assertions.assertTrue(errorEnabled);
    }

    @Test
    void testNullLogEventConsumer() {
        //given
        LoggerImpl logger = new LoggerImpl("test-name", null);

        //when
        final String name = logger.getName();
        final boolean traceEnabled = logger.isTraceEnabled();
        final boolean debugEnabled = logger.isDebugEnabled();
        final boolean infoEnabled = logger.isInfoEnabled();
        final boolean warnEnabled = logger.isWarnEnabled();
        final boolean errorEnabled = logger.isErrorEnabled();

        //then
        Assertions.assertEquals("test-name", name);
        Assertions.assertFalse(traceEnabled,
                "Without a given consumer the fallback to EmergencyLogger should be used that logs on DEBUG by default");
        Assertions.assertTrue(debugEnabled,
                "Without a given consumer the fallback to EmergencyLogger should be used that logs on DEBUG by default");
        Assertions.assertTrue(infoEnabled,
                "Without a given consumer the fallback to EmergencyLogger should be used that logs on DEBUG by default");
        Assertions.assertTrue(warnEnabled,
                "Without a given consumer the fallback to EmergencyLogger should be used that logs on DEBUG by default");
        Assertions.assertTrue(errorEnabled,
                "Without a given consumer the fallback to EmergencyLogger should be used that logs on DEBUG by default");
    }

    @Test
    void testNullName() {
        //given
        LoggerImpl logger = new LoggerImpl(null, new DummyConsumer());

        //when
        final String name = logger.getName();
        final boolean traceEnabled = logger.isTraceEnabled();
        final boolean debugEnabled = logger.isDebugEnabled();
        final boolean infoEnabled = logger.isInfoEnabled();
        final boolean warnEnabled = logger.isWarnEnabled();
        final boolean errorEnabled = logger.isErrorEnabled();

        //then
        Assertions.assertEquals("", name);
        Assertions.assertTrue(traceEnabled);
        Assertions.assertTrue(debugEnabled);
        Assertions.assertTrue(infoEnabled);
        Assertions.assertTrue(warnEnabled);
        Assertions.assertTrue(errorEnabled);
    }
}