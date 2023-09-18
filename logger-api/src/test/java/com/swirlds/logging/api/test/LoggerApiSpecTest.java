package com.swirlds.logging.api.test;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.internal.LoggerImpl;
import com.swirlds.logging.api.internal.LoggingSystem;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoggerApiSpecTest {

    private final static String MESSAGE = "test-message";

    private final static String MESSAGE_WITH_1_PLACEHODLER = "test-message {}";

    private final static String MESSAGE_WITH_2_PLACEHODLER = "test-message {} {}";

    private final static String MESSAGE_WITH_5_PLACEHODLER = "test-message {} {} {} {} {}";

    private final static Throwable THROW = new RuntimeException("test-exception");

    private final static String[][] ARGS_VARIANTS = {{}, {null}, {""}, {"arg"},
            {null, null}, {null, ""}, {null, "arg2"},
            {"", null}, {"", ""}, {"", "arg2"},
            {"arg1", null}, {"arg1", ""}, {"arg1", "arg2"},
            {null, null, null}, {null, null, ""}, {null, null, "arg3"},
            {null, "", null}, {null, "", ""}, {null, "", "arg3"},
            {null, "arg2", null}, {null, "arg2", ""}, {null, "arg2", "arg3"},
            {"", null, null}, {"", null, ""}, {"", null, "arg3"},
            {"", "", null}, {"", "", ""}, {"", "", "arg3"},
            {"", "arg2", null}, {"", "arg2", ""}, {"", "arg2", "arg3"},
            {"arg1", null, null}, {"arg1", null, ""}, {"arg1", null, "arg3"},
            {"arg1", "", null}, {"arg1", "", ""}, {"arg1", "", "arg3"},
            {"arg1", "arg2", null}, {"arg1", "arg2", ""}, {"arg1", "arg2", "arg3"},
    };

    private final static String LOG_ERROR_MESSAGE = "a log call must never throw an exception";

    @Test
    void testNullName() {
        //given
        LoggerImpl logger = new LoggerImpl(null, new DummyConsumer());

        //then
        testSpec(logger);
    }

    @Test
    void testSimpleLogger() {
        //given
        LoggerImpl logger = new LoggerImpl("test-name", new DummyConsumer());

        //then
        testSpec(logger);
    }

    @Test
    void testSpecWithDifferentLoggers() {
        LoggerApiSpecTest.testSpec(new LoggerImpl("test-name", new DummyConsumer()));
        LoggerApiSpecTest.testSpec(new LoggerImpl(null, new DummyConsumer()));
    }

    @Test
    void testLoggingSystemWithoutHandler() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);

        //then
        testSpec(loggingSystem);
    }

    @Test
    void testLoggingSystemWithHandler() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        InMemoryHandler handler = new InMemoryHandler();
        loggingSystem.addHandler(handler);

        //then
        testSpec(loggingSystem);
    }

    public static void testSpec(LoggingSystem system) {
        LoggerApiSpecTest.testSpec(system.getLogger("test-name"));
        LoggerApiSpecTest.testSpec(system.getLogger(null));
    }


    public static void testSpec(Logger logger) {
        testLogger(logger);
        testLogger(logger.withMarker("test-marker"));
        testLogger(logger.withContext("key", "value"));
        testLogger(logger.withContext("key", "value").withMarker("test-marker"));
        testLogger(logger.withMarker("test-marker").withContext("key", "value"));
    }

    private static void testLogger(Logger logger) {
        testLogCall(logger, Level.TRACE, MESSAGE);
        testLogCall(logger, Level.TRACE, MESSAGE_WITH_1_PLACEHODLER);
        testLogCall(logger, Level.TRACE, MESSAGE_WITH_2_PLACEHODLER);
        testLogCall(logger, Level.TRACE, MESSAGE_WITH_5_PLACEHODLER);
        testLogCall(logger, Level.TRACE, null);

        testLogCall(logger, Level.DEBUG, MESSAGE);
        testLogCall(logger, Level.DEBUG, MESSAGE_WITH_1_PLACEHODLER);
        testLogCall(logger, Level.DEBUG, MESSAGE_WITH_2_PLACEHODLER);
        testLogCall(logger, Level.DEBUG, MESSAGE_WITH_5_PLACEHODLER);
        testLogCall(logger, Level.DEBUG, null);

        testLogCall(logger, Level.INFO, MESSAGE);
        testLogCall(logger, Level.INFO, MESSAGE_WITH_1_PLACEHODLER);
        testLogCall(logger, Level.INFO, MESSAGE_WITH_2_PLACEHODLER);
        testLogCall(logger, Level.INFO, MESSAGE_WITH_5_PLACEHODLER);
        testLogCall(logger, Level.INFO, null);

        testLogCall(logger, Level.WARN, MESSAGE);
        testLogCall(logger, Level.WARN, MESSAGE_WITH_1_PLACEHODLER);
        testLogCall(logger, Level.WARN, MESSAGE_WITH_2_PLACEHODLER);
        testLogCall(logger, Level.WARN, MESSAGE_WITH_5_PLACEHODLER);
        testLogCall(logger, Level.WARN, null);

        testLogCall(logger, Level.ERROR, MESSAGE);
        testLogCall(logger, Level.ERROR, MESSAGE_WITH_1_PLACEHODLER);
        testLogCall(logger, Level.ERROR, MESSAGE_WITH_2_PLACEHODLER);
        testLogCall(logger, Level.ERROR, MESSAGE_WITH_5_PLACEHODLER);
        testLogCall(logger, Level.ERROR, null);

        testLogCall(logger, null, MESSAGE);
        testLogCall(logger, null, MESSAGE_WITH_1_PLACEHODLER);
        testLogCall(logger, null, MESSAGE_WITH_2_PLACEHODLER);
        testLogCall(logger, null, MESSAGE_WITH_5_PLACEHODLER);
        testLogCall(logger, null, null);

        testEnabledCall(logger);

        testMarker(logger);

        testContext(logger);

        Assertions.assertDoesNotThrow(logger::getName, LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(logger::toString, LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(logger::hashCode, LOG_ERROR_MESSAGE);
    }

    private static void testMarker(Logger logger) {
        Assertions.assertDoesNotThrow(() -> logger.withMarker(null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withMarker(""), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withMarker("marker"), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withMarker(null).withMarker(null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withMarker(null).withMarker(""), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withMarker(null).withMarker("marker2"), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withMarker("").withMarker(null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withMarker("").withMarker(""), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withMarker("").withMarker("marker2"), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withMarker("marker1").withMarker(null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withMarker("marker1").withMarker(""), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withMarker("marker1").withMarker("marker2"), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withMarker("sameMarker").withMarker("sameMarker"),
                LOG_ERROR_MESSAGE);
    }

    private static void testContext(Logger logger) {
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", "value"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", ""), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", (String) null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, "value"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, ""), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, (String) null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", 1), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", 1L), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", 1.0F), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", 1.0D), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", true), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, 1), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, 1L), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, 1.0F), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, 1.0D), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, true), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Integer.MAX_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Integer.MIN_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Integer.MAX_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Integer.MIN_VALUE), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Long.MAX_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Long.MIN_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Long.MAX_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Long.MIN_VALUE), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Float.MAX_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Float.MIN_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Float.POSITIVE_INFINITY), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Float.NEGATIVE_INFINITY), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Float.NaN), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Float.MIN_NORMAL), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Float.MAX_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Float.MIN_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Float.POSITIVE_INFINITY), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Float.NEGATIVE_INFINITY), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Float.NaN), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Float.MIN_NORMAL), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Double.MAX_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Double.MIN_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Double.POSITIVE_INFINITY), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Double.NEGATIVE_INFINITY), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Double.NaN), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", Double.MIN_NORMAL), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Double.MAX_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Double.MIN_VALUE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Double.POSITIVE_INFINITY), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Double.NEGATIVE_INFINITY), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Double.NaN), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, Double.MIN_NORMAL), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withContext("key", "value1", "value2"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", "value1", ""), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", "value1", null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", null, "value2"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", null, ""), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext("key", null, null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, "value1", "value2"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, "value1", ""), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, "value1", null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, null, "value2"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, null, ""), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, null, null), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withContext("key", 1, 2), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, 1, 2), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withContext("key", 1L, 2L), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, 1L, 2L), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withContext("key", 1.0D, 2.0D), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, 1.0D, 2.0D), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withContext("key", 1.0F, 2.0F), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, 1.0F, 2.0F), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.withContext("key", true, false), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.withContext(null, true, false), LOG_ERROR_MESSAGE);
    }

    private static void testLogCall(Logger logger, Level level, String message) {
        System.out.println("Checking logger call for level '" + level + "' and message '" + message + "'");

        Assertions.assertDoesNotThrow(() -> logger.log(level, message), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.log(level, message, THROW), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, (Throwable) null), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.log(level, message, "arg"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, (String) null), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.log(level, message, "arg1", "arg2"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, "arg1", null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, (String) null, "arg2"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, (String) null, null), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.log(level, message, THROW, "arg"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, THROW, null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, null, "arg"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, null, null), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.log(level, message, THROW, "arg1", "arg2"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, THROW, "arg1", null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, THROW, null, "arg2"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, THROW, null, null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, null, "arg1", "arg2"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, null, "arg1", null), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, null, null, "arg2"), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.log(level, message, null, null, null), LOG_ERROR_MESSAGE);

        Assertions.assertDoesNotThrow(() -> logger.logImpl(level, message, THROW), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.logImpl(level, message, null), LOG_ERROR_MESSAGE);

        Arrays.stream(ARGS_VARIANTS).forEach(args -> {
            Assertions.assertDoesNotThrow(() -> logger.log(level, message, args), LOG_ERROR_MESSAGE);
            Assertions.assertDoesNotThrow(() -> logger.log(level, message, THROW, args), LOG_ERROR_MESSAGE);
        });
    }

    private static void testEnabledCall(Logger logger) {
        Assertions.assertDoesNotThrow(logger::isTraceEnabled, LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(logger::isDebugEnabled, LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(logger::isInfoEnabled, LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(logger::isWarnEnabled, LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(logger::isErrorEnabled, LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.isEnabled(Level.TRACE), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.isEnabled(Level.DEBUG), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.isEnabled(Level.INFO), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.isEnabled(Level.WARN), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.isEnabled(Level.ERROR), LOG_ERROR_MESSAGE);
        Assertions.assertDoesNotThrow(() -> logger.isEnabled(null), LOG_ERROR_MESSAGE);

        Assertions.assertEquals(logger.isTraceEnabled(), logger.isEnabled(Level.TRACE),
                "Methods should always return the same result");
        Assertions.assertEquals(logger.isDebugEnabled(), logger.isEnabled(Level.DEBUG),
                "Methods should always return the same result");
        Assertions.assertEquals(logger.isInfoEnabled(), logger.isEnabled(Level.INFO),
                "Methods should always return the same result");
        Assertions.assertEquals(logger.isWarnEnabled(), logger.isEnabled(Level.WARN),
                "Methods should always return the same result");
        Assertions.assertEquals(logger.isErrorEnabled(), logger.isEnabled(Level.ERROR),
                "Methods should always return the same result");
    }
}
