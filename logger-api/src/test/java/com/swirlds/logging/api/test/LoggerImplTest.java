package com.swirlds.logging.api.test;

import com.swirlds.logging.api.internal.LoggerImpl;
import com.swirlds.logging.api.internal.event.SimpleLogEventFactory;
import com.swirlds.logging.api.test.util.DummyConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoggerImplTest {

    @Test
    void testSimpleLogger() {
        //given
        LoggerImpl logger = new LoggerImpl("test-name", new SimpleLogEventFactory(), new DummyConsumer());

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
        Assertions.assertThrows(NullPointerException.class,
                () -> new LoggerImpl("test-name", new SimpleLogEventFactory(), null));
    }

    @Test
    void testNullLogEventFactory() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new LoggerImpl("test-name", null, new DummyConsumer()));
    }

    @Test
    void testNullName() {
        //given
        LoggerImpl logger = new LoggerImpl(null, new SimpleLogEventFactory(), new DummyConsumer());

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

    @Test
    void testSpecWithNullName() {
        //given
        LoggerImpl logger = new LoggerImpl(null, new SimpleLogEventFactory(), new DummyConsumer());

        //then
        LoggerApiSpecTest.testSpec(logger);
    }

    @Test
    void testSpecWithSimpleLogger() {
        //given
        LoggerImpl logger = new LoggerImpl("test-name", new SimpleLogEventFactory(), new DummyConsumer());

        //then
        LoggerApiSpecTest.testSpec(logger);
    }

    @Test
    void testSpecWithDifferentLoggers() {
        LoggerApiSpecTest.testSpec(new LoggerImpl("test-name", new SimpleLogEventFactory(), new DummyConsumer()));
        LoggerApiSpecTest.testSpec(new LoggerImpl(null, new SimpleLogEventFactory(), new DummyConsumer()));
    }
}
