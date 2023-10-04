package com.swirlds.logging.api.test;

import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Loggers;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.internal.DefaultLoggingSystem;
import com.swirlds.logging.api.test.util.InMemoryHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoggersTest {

    private InMemoryHandler loggingMirror;

    @BeforeEach
    void init() {
        loggingMirror = new InMemoryHandler();
        //TODO: Replace with @WithLoggingMirror once we have a test fixture
        DefaultLoggingSystem.getInstance().addHandler(loggingMirror);
    }

    @AfterEach
    void cleanup() {
        DefaultLoggingSystem.getInstance().removeHandler(loggingMirror);
    }

    @Test
    void testLoggerCreationByName() {
        //given
        final String loggerName = "testLoggerCreationByName";

        //when
        final Logger logger = Loggers.getLogger(loggerName);

        //then
        Assertions.assertEquals(loggerName, logger.getName());
    }

    @Test
    void testLoggerCreationByNullName() {
        //given
        final String loggerName = null;

        //when
        final Logger logger = Loggers.getLogger(loggerName);

        //then
        Assertions.assertEquals("", logger.getName());
    }

    @Test
    void testLoggerCreationByClass() {
        //given
        final Class clazz = LoggersTest.class;

        //when
        final Logger logger = Loggers.getLogger(clazz);

        //then
        Assertions.assertEquals(clazz.getName(), logger.getName());
    }

    @Test
    void testLoggerCreationByNullClass() {
        //given
        final Class clazz = null;

        //when
        final Logger logger = Loggers.getLogger(clazz);

        //then
        Assertions.assertEquals("", logger.getName());
    }

    @Test
    void testLoggerCreationByClassReturnsUseableLogger() {
        //given
        final Class clazz = LoggersTest.class;
        final Logger logger = Loggers.getLogger(clazz);

        //when
        logger.error("test");

        //then
        Assertions.assertEquals(1, loggingMirror.getEvents().size());
        final LogEvent event = loggingMirror.getEvents().get(0);
        Assertions.assertEquals(clazz.getName(), event.loggerName());
        Assertions.assertEquals(Thread.currentThread().getName(), event.threadName());
        Assertions.assertEquals(Level.ERROR, event.level());
        DefaultLoggingSystem.getInstance().removeHandler(loggingMirror);
    }

    @Test
    void testLoggerCreationByNameReturnsUseableLogger() {
        //given
        final String loggerName = "testLoggerCreationByName";
        final Logger logger = Loggers.getLogger(loggerName);

        //when
        logger.error("test");

        //then
        Assertions.assertEquals(1, loggingMirror.getEvents().size());
        final LogEvent event = loggingMirror.getEvents().get(0);
        Assertions.assertEquals(loggerName, event.loggerName());
        Assertions.assertEquals(Thread.currentThread().getName(), event.threadName());
        Assertions.assertEquals(Level.ERROR, event.level());
    }

}
