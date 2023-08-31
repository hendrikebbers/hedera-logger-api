package com.swirlds.logging.api.internal;

import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.LogEventConsumer;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoggerImplTest {

    private final static LogEventConsumer DUMMY_CONSUMER = event -> {
    };

    @Test
    void testSpecWithDifferentLoggers() {
        LoggerApiSpecTest.testSpec(new LoggerImpl("test-name", DUMMY_CONSUMER));
        LoggerApiSpecTest.testSpec(new LoggerImpl(null, DUMMY_CONSUMER));
        LoggerApiSpecTest.testSpec(new LoggerImpl("test-name", null));
        LoggerApiSpecTest.testSpec(new LoggerImpl(null, null));
    }

    @Test
    void testSimpleLogger() {
        //given
        LoggerImpl logger = new LoggerImpl("test-name", DUMMY_CONSUMER);

        //when
        final String name = logger.getName();
        final boolean traceEnabled = logger.isTraceEnabled();
        final boolean debugEnabled = logger.isDebugEnabled();
        final boolean infoEnabled = logger.isInfoEnabled();
        final boolean warnEnabled = logger.isWarnEnabled();
        final boolean errorEnabled = logger.isErrorEnabled();
        final Marker marker = logger.getMarker();
        final Map<String, String> context = logger.getContext();

        //then
        Assertions.assertEquals("test-name", name);
        Assertions.assertTrue(traceEnabled);
        Assertions.assertTrue(debugEnabled);
        Assertions.assertTrue(infoEnabled);
        Assertions.assertTrue(warnEnabled);
        Assertions.assertTrue(errorEnabled);
        Assertions.assertNull(marker);
        Assertions.assertNotNull(context);
        Assertions.assertTrue(context.isEmpty());

        LoggerApiSpecTest.testSpec(logger);
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
        final Marker marker = logger.getMarker();
        final Map<String, String> context = logger.getContext();

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
        Assertions.assertNull(marker);
        Assertions.assertNotNull(context);
        Assertions.assertTrue(context.isEmpty());

        LoggerApiSpecTest.testSpec(logger);
    }

    @Test
    void testNullName() {
        //given
        LoggerImpl logger = new LoggerImpl(null, DUMMY_CONSUMER);

        //when
        final String name = logger.getName();
        final boolean traceEnabled = logger.isTraceEnabled();
        final boolean debugEnabled = logger.isDebugEnabled();
        final boolean infoEnabled = logger.isInfoEnabled();
        final boolean warnEnabled = logger.isWarnEnabled();
        final boolean errorEnabled = logger.isErrorEnabled();
        final Marker marker = logger.getMarker();
        final Map<String, String> context = logger.getContext();

        //then
        Assertions.assertEquals("", name);
        Assertions.assertTrue(traceEnabled);
        Assertions.assertTrue(debugEnabled);
        Assertions.assertTrue(infoEnabled);
        Assertions.assertTrue(warnEnabled);
        Assertions.assertTrue(errorEnabled);
        Assertions.assertNull(marker);
        Assertions.assertNotNull(context);
        Assertions.assertTrue(context.isEmpty());

        LoggerApiSpecTest.testSpec(logger);
    }
}
