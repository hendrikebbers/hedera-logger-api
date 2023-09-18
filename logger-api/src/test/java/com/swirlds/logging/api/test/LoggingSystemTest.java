package com.swirlds.logging.api.test;

import com.swirlds.base.context.Context;
import com.swirlds.base.testfixture.context.WithContext;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Marker;
import com.swirlds.logging.api.extensions.LogEvent;
import com.swirlds.logging.api.extensions.handler.LogHandler;
import com.swirlds.logging.api.internal.LoggerImpl;
import com.swirlds.logging.api.internal.LoggingSystem;
import com.swirlds.logging.api.internal.emergency.EmergencyLoggerImpl;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WithContext
public class LoggingSystemTest {

    @BeforeEach
    void setUp() {
        //Once we are on gradle we can use the @WithLoggerMirror testfixture here...
        EmergencyLoggerImpl.getInstance().publishLoggedEvents();
    }


    @Test
    @DisplayName("Test that a logger name is always created correctly")
    void testLoggerName() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);

        //when
        final LoggerImpl testNameLogger = loggingSystem.getLogger("test-name");
        final LoggerImpl nullNameLogger = loggingSystem.getLogger(null);
        final LoggerImpl blankNameLogger = loggingSystem.getLogger("");
        final LoggerImpl ToTrimNameLogger = loggingSystem.getLogger("  test-name  ");
        final LoggerImpl ToTrimBlankNameLogger = loggingSystem.getLogger("    ");

        //then
        Assertions.assertEquals("test-name", testNameLogger.getName());
        Assertions.assertEquals("", nullNameLogger.getName());
        Assertions.assertEquals("", blankNameLogger.getName());
        Assertions.assertEquals("test-name", ToTrimNameLogger.getName());
        Assertions.assertEquals("", ToTrimBlankNameLogger.getName());
    }

    @Test
    @DisplayName("Test that creating loggers with same name ends in same logger instance")
    void testSameLoggerByName() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);

        //when
        final LoggerImpl testNameLogger = loggingSystem.getLogger("test-name");
        final LoggerImpl nullNameLogger = loggingSystem.getLogger(null);
        final LoggerImpl blankNameLogger = loggingSystem.getLogger("");
        final LoggerImpl ToTrimNameLogger = loggingSystem.getLogger("  test-name  ");
        final LoggerImpl ToTrimBlankNameLogger = loggingSystem.getLogger("    ");
        final LoggerImpl testNameLogger2 = loggingSystem.getLogger("test-name");

        //then
        Assertions.assertSame(testNameLogger, ToTrimNameLogger);
        Assertions.assertSame(nullNameLogger, blankNameLogger);
        Assertions.assertSame(nullNameLogger, ToTrimBlankNameLogger);
        Assertions.assertSame(testNameLogger, testNameLogger2);
    }

    @Test
    @DisplayName("Test that INFO is default level for a non configured logging system")
    void testDefaultLevel() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);

        //when
        final boolean rootTraceEnabled = loggingSystem.isEnabled("", Level.TRACE);
        final boolean rootDebugEnabled = loggingSystem.isEnabled("", Level.DEBUG);
        final boolean rootInfoEnabled = loggingSystem.isEnabled("", Level.INFO);
        final boolean rootWarnEnabled = loggingSystem.isEnabled("", Level.WARN);
        final boolean rootErrorEnabled = loggingSystem.isEnabled("", Level.ERROR);

        final boolean rootTrimTraceEnabled = loggingSystem.isEnabled("  ", Level.TRACE);
        final boolean rootTrimDebugEnabled = loggingSystem.isEnabled("  ", Level.DEBUG);
        final boolean rootTrimInfoEnabled = loggingSystem.isEnabled("  ", Level.INFO);
        final boolean rootTrimWarnEnabled = loggingSystem.isEnabled("  ", Level.WARN);
        final boolean rootTrimErrorEnabled = loggingSystem.isEnabled("  ", Level.ERROR);

        final boolean rootNullTraceEnabled = loggingSystem.isEnabled(null, Level.TRACE);
        final boolean rootNullDebugEnabled = loggingSystem.isEnabled(null, Level.DEBUG);
        final boolean rootNullInfoEnabled = loggingSystem.isEnabled(null, Level.INFO);
        final boolean rootNullWarnEnabled = loggingSystem.isEnabled(null, Level.WARN);
        final boolean rootNullErrorEnabled = loggingSystem.isEnabled(null, Level.ERROR);

        final boolean testTraceEnabled = loggingSystem.isEnabled("test", Level.TRACE);
        final boolean testDebugEnabled = loggingSystem.isEnabled("test", Level.DEBUG);
        final boolean testInfoEnabled = loggingSystem.isEnabled("test", Level.INFO);
        final boolean testWarnEnabled = loggingSystem.isEnabled("test", Level.WARN);
        final boolean testErrorEnabled = loggingSystem.isEnabled("test", Level.ERROR);

        final boolean testBlankTraceEnabled = loggingSystem.isEnabled("  test  ", Level.TRACE);
        final boolean testBlankDebugEnabled = loggingSystem.isEnabled("  test  ", Level.DEBUG);
        final boolean testBlankInfoEnabled = loggingSystem.isEnabled("  test  ", Level.INFO);
        final boolean testBlankWarnEnabled = loggingSystem.isEnabled("  test  ", Level.WARN);
        final boolean testBlankErrorEnabled = loggingSystem.isEnabled("  test  ", Level.ERROR);

        //then
        Assertions.assertFalse(rootTraceEnabled, "INFO should be default level");
        Assertions.assertFalse(rootDebugEnabled, "INFO should be default level");
        Assertions.assertTrue(rootInfoEnabled, "INFO should be default level");
        Assertions.assertTrue(rootWarnEnabled, "INFO should be default level");
        Assertions.assertTrue(rootErrorEnabled, "INFO should be default level");

        Assertions.assertFalse(rootNullTraceEnabled, "INFO should be default level");
        Assertions.assertFalse(rootNullDebugEnabled, "INFO should be default level");
        Assertions.assertTrue(rootNullInfoEnabled, "INFO should be default level");
        Assertions.assertTrue(rootNullWarnEnabled, "INFO should be default level");
        Assertions.assertTrue(rootNullErrorEnabled, "INFO should be default level");

        Assertions.assertFalse(rootTrimTraceEnabled, "INFO should be default level");
        Assertions.assertFalse(rootTrimDebugEnabled, "INFO should be default level");
        Assertions.assertTrue(rootTrimInfoEnabled, "INFO should be default level");
        Assertions.assertTrue(rootTrimWarnEnabled, "INFO should be default level");
        Assertions.assertTrue(rootTrimErrorEnabled, "INFO should be default level");

        Assertions.assertFalse(testTraceEnabled, "INFO should be default level");
        Assertions.assertFalse(testDebugEnabled, "INFO should be default level");
        Assertions.assertTrue(testInfoEnabled, "INFO should be default level");
        Assertions.assertTrue(testWarnEnabled, "INFO should be default level");
        Assertions.assertTrue(testErrorEnabled, "INFO should be default level");

        Assertions.assertFalse(testBlankTraceEnabled, "INFO should be default level");
        Assertions.assertFalse(testBlankDebugEnabled, "INFO should be default level");
        Assertions.assertTrue(testBlankInfoEnabled, "INFO should be default level");
        Assertions.assertTrue(testBlankWarnEnabled, "INFO should be default level");
        Assertions.assertTrue(testBlankErrorEnabled, "INFO should be default level");
    }

    @Test
    @DisplayName("Test that logging system can handle null params for isEnabled")
    void testNullLevel() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);

        //when
        final boolean rootEnabled = loggingSystem.isEnabled("", null);
        final boolean rootTrimEnabled = loggingSystem.isEnabled("  ", null);
        final boolean rootNullEnabled = loggingSystem.isEnabled(null,
                null);
        final boolean testEnabled = loggingSystem.isEnabled("test", null);
        final boolean testBlankEnabled = loggingSystem.isEnabled("  test  ", null);

        //then
        Assertions.assertTrue(rootEnabled, "For a NULL level all must be enabled");
        Assertions.assertTrue(rootTrimEnabled, "For a NULL level all must be enabled");
        Assertions.assertTrue(rootNullEnabled, "For a NULL level all must be enabled");
        Assertions.assertTrue(testEnabled, "For a NULL level all must be enabled");
        Assertions.assertTrue(testBlankEnabled, "For a NULL level all must be enabled");
    }


    @Test
    @DisplayName("Test that isEnabled logs errors to emergency logger")
    void testErrorsForEnabled() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);

        //when
        loggingSystem.isEnabled("test", Level.TRACE); // no logged error
        loggingSystem.isEnabled("test", null); // 1 logged error
        loggingSystem.isEnabled(null, Level.TRACE); // 1 logged error
        loggingSystem.isEnabled(null, null); // 2 logged errors

        final List<LogEvent> loggedErrorEvents = getLoggedEvents();

        Assertions.assertEquals(4, loggedErrorEvents.size(), "There should be 6 ERROR events");
    }

    @Test
    @DisplayName("Test that accept logs errors to emergency logger")
    void testErrorsForAccept() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);

        //when
        loggingSystem.accept(null); // 1 logged error

        final List<LogEvent> loggedErrorEvents = getLoggedEvents();

        Assertions.assertEquals(1, loggedErrorEvents.size(), "There should be 1 ERROR event");
    }

    private List<LogEvent> getLoggedEvents() {
        return EmergencyLoggerImpl.getInstance().publishLoggedEvents()
                .stream()
                .filter(event -> event.level() == Level.ERROR)
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("Test that log level can be configured")
    void testCustomLevel() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        configuration.setProperty("logging.level", "ERROR");
        configuration.setProperty("logging.level.test", "TRACE");
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);

        //when
        final boolean rootTraceEnabled = loggingSystem.isEnabled("", Level.TRACE);
        final boolean rootDebugEnabled = loggingSystem.isEnabled("", Level.DEBUG);
        final boolean rootInfoEnabled = loggingSystem.isEnabled("", Level.INFO);
        final boolean rootWarnEnabled = loggingSystem.isEnabled("", Level.WARN);
        final boolean rootErrorEnabled = loggingSystem.isEnabled("", Level.ERROR);

        final boolean rootNullTraceEnabled = loggingSystem.isEnabled(null, Level.TRACE);
        final boolean rootNullDebugEnabled = loggingSystem.isEnabled(null, Level.DEBUG);
        final boolean rootNullInfoEnabled = loggingSystem.isEnabled(null, Level.INFO);
        final boolean rootNullWarnEnabled = loggingSystem.isEnabled(null, Level.WARN);
        final boolean rootNullErrorEnabled = loggingSystem.isEnabled(null, Level.ERROR);

        final boolean rootTrimTraceEnabled = loggingSystem.isEnabled("  ", Level.TRACE);
        final boolean rootTrimDebugEnabled = loggingSystem.isEnabled("  ", Level.DEBUG);
        final boolean rootTrimInfoEnabled = loggingSystem.isEnabled("  ", Level.INFO);
        final boolean rootTrimWarnEnabled = loggingSystem.isEnabled("  ", Level.WARN);
        final boolean rootTrimErrorEnabled = loggingSystem.isEnabled("  ", Level.ERROR);

        final boolean testTraceEnabled = loggingSystem.isEnabled("test", Level.TRACE);
        final boolean testDebugEnabled = loggingSystem.isEnabled("test", Level.DEBUG);
        final boolean testInfoEnabled = loggingSystem.isEnabled("test", Level.INFO);
        final boolean testWarnEnabled = loggingSystem.isEnabled("test", Level.WARN);
        final boolean testErrorEnabled = loggingSystem.isEnabled("test", Level.ERROR);

        final boolean testBlankTraceEnabled = loggingSystem.isEnabled("  test  ", Level.TRACE);
        final boolean testBlankDebugEnabled = loggingSystem.isEnabled("  test  ", Level.DEBUG);
        final boolean testBlankInfoEnabled = loggingSystem.isEnabled("  test  ", Level.INFO);
        final boolean testBlankWarnEnabled = loggingSystem.isEnabled("  test  ", Level.WARN);
        final boolean testBlankErrorEnabled = loggingSystem.isEnabled("  test  ", Level.ERROR);

        //then
        Assertions.assertFalse(rootTraceEnabled, "ERROR is configured for root");
        Assertions.assertFalse(rootDebugEnabled, "ERROR is configured for root");
        Assertions.assertFalse(rootInfoEnabled, "ERROR is configured for root");
        Assertions.assertFalse(rootWarnEnabled, "ERROR is configured for root");
        Assertions.assertTrue(rootErrorEnabled, "ERROR is configured for root");

        Assertions.assertFalse(rootNullTraceEnabled, "ERROR is configured for root");
        Assertions.assertFalse(rootNullDebugEnabled, "ERROR is configured for root");
        Assertions.assertFalse(rootNullInfoEnabled, "ERROR is configured for root");
        Assertions.assertFalse(rootNullWarnEnabled, "ERROR is configured for root");
        Assertions.assertTrue(rootNullErrorEnabled, "ERROR is configured for root");

        Assertions.assertFalse(rootTrimTraceEnabled, "ERROR is configured for root");
        Assertions.assertFalse(rootTrimDebugEnabled, "ERROR is configured for root");
        Assertions.assertFalse(rootTrimInfoEnabled, "ERROR is configured for root");
        Assertions.assertFalse(rootTrimWarnEnabled, "ERROR is configured for root");
        Assertions.assertTrue(rootTrimErrorEnabled, "ERROR is configured for root");

        Assertions.assertTrue(testTraceEnabled, "TRACE is configured");
        Assertions.assertTrue(testDebugEnabled, "TRACE is configured");
        Assertions.assertTrue(testInfoEnabled, "TRACE is configured");
        Assertions.assertTrue(testWarnEnabled, "TRACE is configured");
        Assertions.assertTrue(testErrorEnabled, "TRACE is configured");

        Assertions.assertTrue(testBlankTraceEnabled, "TRACE is configured");
        Assertions.assertTrue(testBlankDebugEnabled, "TRACE is configured");
        Assertions.assertTrue(testBlankInfoEnabled, "TRACE is configured");
        Assertions.assertTrue(testBlankWarnEnabled, "TRACE is configured");
        Assertions.assertTrue(testBlankErrorEnabled, "TRACE is configured");
    }

    @Test
    @DisplayName("Test that addHandler logs errors to emergency logger")
    void testNullHandler() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);

        //when
        loggingSystem.addHandler(null);

        //then
        final List<LogEvent> loggedErrorEvents = getLoggedEvents();
        Assertions.assertEquals(1, loggedErrorEvents.size());
    }

    @Test
    @DisplayName("Test that getLogger logs errors to emergency logger")
    void testNullLogger() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);

        //when
        final LoggerImpl logger = loggingSystem.getLogger(null);

        //then
        Assertions.assertNotNull(logger);
        Assertions.assertEquals("", logger.getName());
        Assertions.assertFalse(logger.isEnabled(Level.TRACE), "logger should be configured as root logger");
        Assertions.assertFalse(logger.isEnabled(Level.DEBUG), "logger should be configured as root logger");
        Assertions.assertTrue(logger.isEnabled(Level.INFO), "logger should be configured as root logger");
        Assertions.assertTrue(logger.isEnabled(Level.WARN), "logger should be configured as root logger");
        Assertions.assertTrue(logger.isEnabled(Level.ERROR), "logger should be configured as root logger");
        final List<LogEvent> loggedErrorEvents = getLoggedEvents();
        Assertions.assertEquals(1, loggedErrorEvents.size());
    }

    @Test
    @DisplayName("Test that all logging is forwarded to emergency logger if no handler is defined")
    void testEmergencyLoggerIsUsedIfNoAppender() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        final LoggerImpl logger = loggingSystem.getLogger("");
        EmergencyLoggerImpl.getInstance().publishLoggedEvents(); // reset Emergency logger to remove the init logging

        //when
        logger.trace("trace-message"); // should not be logged since root logger is defined as INFO level
        logger.debug("debug-message"); // should not be logged since root logger is defined as INFO level
        logger.info("info-message");
        logger.warn("warn-message");
        logger.error("error-message");

        //then
        final List<LogEvent> loggedEvents = EmergencyLoggerImpl.getInstance().publishLoggedEvents();
        Assertions.assertEquals(3, loggedEvents.size());
        Assertions.assertEquals("info-message", loggedEvents.get(0).message());
        Assertions.assertEquals(Level.INFO, loggedEvents.get(0).level());
        Assertions.assertEquals("warn-message", loggedEvents.get(1).message());
        Assertions.assertEquals(Level.WARN, loggedEvents.get(1).level());
        Assertions.assertEquals("error-message", loggedEvents.get(2).message());
        Assertions.assertEquals(Level.ERROR, loggedEvents.get(2).level());
    }

    @Test
    @DisplayName("Test that all logging for a configured level is forwarded to emergency logger if no handler is defined")
    void testEmergencyLoggerIsUsedForConfiguredLevelIfNoAppender() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        configuration.setProperty("logging.level.test", "ERROR");
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        final LoggerImpl logger = loggingSystem.getLogger("test");
        EmergencyLoggerImpl.getInstance().publishLoggedEvents(); // reset Emergency logger to remove the init logging

        //when
        logger.trace("trace-message"); // should not be logged since logger is defined as ERROR level
        logger.debug("debug-message"); // should not be logged since logger is defined as ERROR level
        logger.info("info-message"); // should not be logged since logger is defined as ERROR level
        logger.warn("warn-message"); // should not be logged since logger is defined as ERROR level
        logger.error("error-message");

        //then
        final List<LogEvent> loggedEvents = EmergencyLoggerImpl.getInstance().publishLoggedEvents();
        Assertions.assertEquals(1, loggedEvents.size());
        Assertions.assertEquals("error-message", loggedEvents.get(0).message());
        Assertions.assertEquals(Level.ERROR, loggedEvents.get(0).level());
    }

    @Test
    @DisplayName("That that checks if simple log calls are forwarded correctly will all informations to the configured handler")
    void testSimpleLoggingHandling() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        final InMemoryHandler handler = new InMemoryHandler();
        loggingSystem.addHandler(handler);
        final LoggerImpl logger = loggingSystem.getLogger("test-logger");
        final Instant startTime = Instant.now();

        //when
        logger.trace("trace-message"); //Should not be forwarded since INFO is configured as root level
        logger.debug("debug-message"); //Should not be forwarded since INFO is configured as root level
        logger.info("info-message");
        logger.warn("warn-message");
        logger.error("error-message");

        //then
        final List<LogEvent> loggedEvents = handler.getEvents();
        Assertions.assertEquals(3, loggedEvents.size());

        final LogEvent event1 = loggedEvents.get(0);
        Assertions.assertEquals("info-message", event1.message());
        Assertions.assertEquals(Level.INFO, event1.level());
        Assertions.assertEquals(Map.of(), event1.context());
        Assertions.assertEquals("test-logger", event1.loggerName());
        Assertions.assertNull(event1.marker());
        Assertions.assertEquals(Thread.currentThread().getName(), event1.threadName());
        Assertions.assertNull(event1.throwable());
        Assertions.assertTrue(event1.timestamp().isAfter(startTime));
        Assertions.assertTrue(event1.timestamp().isBefore(Instant.now()));

        final LogEvent event2 = loggedEvents.get(1);
        Assertions.assertEquals("warn-message", event2.message());
        Assertions.assertEquals(Level.WARN, event2.level());
        Assertions.assertEquals(Map.of(), event2.context());
        Assertions.assertEquals("test-logger", event2.loggerName());
        Assertions.assertNull(event2.marker());
        Assertions.assertEquals(Thread.currentThread().getName(), event2.threadName());
        Assertions.assertNull(event2.throwable());
        Assertions.assertTrue(event2.timestamp().isAfter(startTime));
        Assertions.assertTrue(event2.timestamp().isBefore(Instant.now()));

        final LogEvent event3 = loggedEvents.get(2);
        Assertions.assertEquals("error-message", event3.message());
        Assertions.assertEquals(Level.ERROR, event3.level());
        Assertions.assertEquals(Map.of(), event3.context());
        Assertions.assertEquals("test-logger", event3.loggerName());
        Assertions.assertNull(event3.marker());
        Assertions.assertEquals(Thread.currentThread().getName(), event3.threadName());
        Assertions.assertNull(event3.throwable());
        Assertions.assertTrue(event3.timestamp().isAfter(startTime));
        Assertions.assertTrue(event3.timestamp().isBefore(Instant.now()));

        Assertions.assertTrue(event1.timestamp().isBefore(event2.timestamp()));
        Assertions.assertTrue(event2.timestamp().isBefore(event3.timestamp()));
    }

    @Test
    @DisplayName("That that accept passes events to the configured handler")
    void testAcceptHandling() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        final InMemoryHandler handler = new InMemoryHandler();
        loggingSystem.addHandler(handler);
        final LocalDateTime startTime = LocalDateTime.now();
        LogEvent event1 = new LogEvent("info-message", "test-logger", Level.INFO);
        LogEvent event2 = new LogEvent("trace-message", "test-logger",
                Level.TRACE); //should not be forwarded since INFO is configured as root level
        LogEvent event3 = new LogEvent("error-message", "test-logger", Level.ERROR);
        LogEvent event4 = new LogEvent("info-message", "test-logger", Level.INFO);

        //when
        loggingSystem.accept(event1);
        loggingSystem.accept(event2);
        loggingSystem.accept(event3);
        loggingSystem.accept(event4);

        //then
        final List<LogEvent> loggedEvents = handler.getEvents();
        Assertions.assertEquals(3, loggedEvents.size());
        Assertions.assertEquals(event1, loggedEvents.get(0));
        Assertions.assertEquals(event3, loggedEvents.get(1));
        Assertions.assertEquals(event4, loggedEvents.get(2));
    }

    @Test
    @DisplayName("That that checks if complex log calls are forwarded correctly with all informations to the configured handler")
    void testComplexLoggingHandling() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        final InMemoryHandler handler = new InMemoryHandler();
        loggingSystem.addHandler(handler);
        final LoggerImpl logger = loggingSystem.getLogger("test-logger");
        final Instant startTime = Instant.now();

        //when
        Context.getGlobalContext().add("global", "global-value");
        logger.withMarker("TRACE_MARKER")
                .withContext("level", "trace")
                .withContext("context", "unit-test")
                .trace("trace-message {}", new RuntimeException("trace-error"),
                        "ARG"); //Should not be forwarded since INFO is configured as root level

        try (final AutoCloseable closeable = Context.getThreadLocalContext()
                .add("thread-local", "thread-local-value")) {
            logger.withMarker("INFO_MARKER")
                    .withContext("level", "info")
                    .withContext("context", "unit-test")
                    .info("info-message {}", new RuntimeException("info-error"),
                            "ARG");
        } catch (final Exception e) {
            Assertions.fail();
        }

        Context.getGlobalContext().remove("global");

        logger.withMarker("INFO_MARKER")
                .withContext("level", "info")
                .withContext("context", "unit-test")
                .info("info-message2 {}", new RuntimeException("info-error2"),
                        "ARG2");

        //then
        final List<LogEvent> loggedEvents = handler.getEvents();
        Assertions.assertEquals(2, loggedEvents.size());

        final LogEvent event1 = loggedEvents.get(0);
        Assertions.assertEquals("info-message ARG", event1.message());
        Assertions.assertEquals(Level.INFO, event1.level());
        Assertions.assertEquals(
                Map.of("context", "unit-test", "global", "global-value", "thread-local", "thread-local-value", "level",
                        "info"), event1.context());
        Assertions.assertEquals("test-logger", event1.loggerName());
        Assertions.assertEquals(new Marker("INFO_MARKER"), event1.marker());
        Assertions.assertEquals(Thread.currentThread().getName(), event1.threadName());
        Assertions.assertNotNull(event1.throwable());
        Assertions.assertEquals("info-error", event1.throwable().getMessage());
        Assertions.assertEquals(RuntimeException.class, event1.throwable().getClass());
        Assertions.assertTrue(event1.timestamp().isAfter(startTime));
        Assertions.assertTrue(event1.timestamp().isBefore(Instant.now()));

        final LogEvent event2 = loggedEvents.get(1);
        Assertions.assertEquals("info-message2 ARG2", event2.message());
        Assertions.assertEquals(Level.INFO, event2.level());
        Assertions.assertEquals(
                Map.of("context", "unit-test", "level", "info"), event2.context());
        Assertions.assertEquals("test-logger", event2.loggerName());
        Assertions.assertEquals(new Marker("INFO_MARKER"), event2.marker());
        Assertions.assertEquals(Thread.currentThread().getName(), event2.threadName());
        Assertions.assertNotNull(event2.throwable());
        Assertions.assertEquals("info-error2", event2.throwable().getMessage());
        Assertions.assertEquals(RuntimeException.class, event2.throwable().getClass());
        Assertions.assertTrue(event2.timestamp().isAfter(startTime));
        Assertions.assertTrue(event2.timestamp().isBefore(Instant.now()));

        Assertions.assertTrue(event1.timestamp().isBefore(event2.timestamp()));
    }

    @Test
    @DisplayName("That that accept passes complex log calls correctly with all informations to the configured handler")
    void testAcceptComnplexHandling() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        final InMemoryHandler handler = new InMemoryHandler();
        loggingSystem.addHandler(handler);

        LogEvent event1 = new LogEvent(Level.INFO, "test-logger", Thread.currentThread().getName(), Instant.now(),
                "message",
                new RuntimeException("error"), new Marker("INFO_MARKER"),
                Map.of("context", "unit-test", "level", "info")
        );

        LogEvent event2 = new LogEvent("trace-message", "test-logger",
                Level.TRACE); //should not be forwarded since INFO is configured as root level
        LogEvent event3 = new LogEvent("error-message", "test-logger", Level.ERROR);
        LogEvent event4 = new LogEvent(Level.INFO, "test-logger", Thread.currentThread().getName(), Instant.now(),
                "message",
                new RuntimeException("error"), new Marker("INFO_MARKER"), Map.of("context", "unit-test")
        );

        //when
        loggingSystem.accept(event1);
        Context.getGlobalContext().add("new-global", "new-global-value");
        loggingSystem.accept(event2);
        loggingSystem.accept(event3);
        loggingSystem.accept(event4);

        //then
        final List<LogEvent> loggedEvents = handler.getEvents();
        Assertions.assertEquals(3, loggedEvents.size());
        Assertions.assertEquals(event1, loggedEvents.get(0));
        Assertions.assertEquals(
                LogEvent.createCopyWithDifferentContext(event3, Map.of("new-global", "new-global-value")),
                loggedEvents.get(1));
        Assertions.assertEquals(LogEvent.createCopyWithDifferentContext(event4,
                Map.of("context", "unit-test", "new-global", "new-global-value")), loggedEvents.get(2));
    }

    @Test
    @DisplayName("Test that any exception in a handler will not be thrown but logged instead")
    void testExceptionInHandler() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        loggingSystem.addHandler(new LogHandler() {
            @Override
            public String getName() {
                return "ExceptionThrowingHandler";
            }

            @Override
            public boolean isActive() {
                return true;
            }

            @Override
            public void accept(LogEvent event) {
                throw new RuntimeException("Exception in handler");
            }
        });

        //when
        loggingSystem.accept(new LogEvent("message", "logger", Level.INFO));

        final List<LogEvent> loggedErrorEvents = getLoggedEvents();

        Assertions.assertEquals(1, loggedErrorEvents.size(), "There should be 1 ERROR event");
    }
}
