package com.swirlds.logging.usage;

import com.swirlds.base.context.Context;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Loggers;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.MarkerManager;

public class Demo2 {

    private final static Logger logger = Loggers.getLogger(Demo2.class);

    public static void main(String[] args) throws Exception {
        Context.getGlobalContext().add("app", "demo2");

        logger.log(Level.TRACE, "Hello world!");
        logger.log(Level.INFO, "Hello world!");
        logger.log(Level.DEBUG, "Hello world!");
        logger.log(Level.WARN, "Hello world!");
        logger.log(Level.ERROR, "Hello world!");

        logger.info("Hello world!");
        logger.debug("Hello world!");
        logger.warn("Hello world!");
        logger.error("Hello world!");

        logger.info("Hello world!", new RuntimeException("OH NO!"));
        logger.debug("Hello world!", new RuntimeException("OH NO!"));
        logger.warn("Hello world!", new RuntimeException("OH NO!"));
        logger.error("Hello world!", new RuntimeException("OH NO!"));

        logger.info("Hello {}!", "world");
        logger.debug("Hello {}!", "world");
        logger.warn("Hello {}!", "world");
        logger.error("Hello {}!", "world");

        logger.info("Hello {}!", new RuntimeException("OH NO!"), "world");
        logger.debug("Hello {}!", new RuntimeException("OH NO!"), "world");
        logger.warn("Hello {}!", new RuntimeException("OH NO!"), "world");
        logger.error("Hello {}!", new RuntimeException("OH NO!"), "world");

        logger.withMarker("LOGGING_DEMO").info("Hello world!");
        logger.withMarker("LOGGING_DEMO").debug("Hello world!");
        logger.withMarker("LOGGING_DEMO").warn("Hello world!");
        logger.withMarker("LOGGING_DEMO").error("Hello world!");

        logger.withMarker("LOGGING_DEMO").info("Hello world!", new RuntimeException("OH NO!"));
        logger.withMarker("LOGGING_DEMO").debug("Hello world!", new RuntimeException("OH NO!"));
        logger.withMarker("LOGGING_DEMO").warn("Hello world!", new RuntimeException("OH NO!"));
        logger.withMarker("LOGGING_DEMO").error("Hello world!", new RuntimeException("OH NO!"));

        logger.withMarker("DEMO").withMarker("LOGGING_DEMO").info("Hello world!");
        logger.withMarker("DEMO").withMarker("LOGGING_DEMO").debug("Hello world!");
        logger.withMarker("DEMO").withMarker("LOGGING_DEMO").warn("Hello world!");
        logger.withMarker("DEMO").withMarker("LOGGING_DEMO").error("Hello world!");

        logger.withContext("context", "value").info("Hello world!");
        logger.withContext("context", "value").debug("Hello world!");
        logger.withContext("context", "value").warn("Hello world!");
        logger.withContext("context", "value").error("Hello world!");

        logger.withContext("context", "value").info("Hello world!", new RuntimeException("OH NO!"));
        logger.withContext("context", "value").debug("Hello world!", new RuntimeException("OH NO!"));
        logger.withContext("context", "value").warn("Hello world!", new RuntimeException("OH NO!"));
        logger.withContext("context", "value").error("Hello world!", new RuntimeException("OH NO!"));

        logger.withContext("context1", "value1")
                .withContext("context2", "value2")
                .info("Hello world!");
        logger.withContext("context1", "value1")
                .withContext("context2", "value2")
                .debug("Hello world!");
        logger.withContext("context1", "value1")
                .withContext("context2", "value2")
                .warn("Hello world!");
        logger.withContext("context1", "value1")
                .withContext("context2", "value2")
                .error("Hello world!");

        logger.withContext("context1", "value1")
                .withContext("context2", "value2")
                .info("Hello world!", new RuntimeException("OH NO!"));
        logger.withContext("context1", "value1")
                .withContext("context2", "value2")
                .debug("Hello world!", new RuntimeException("OH NO!"));
        logger.withContext("context1", "value1")
                .withContext("context2", "value2")
                .warn("Hello world!", new RuntimeException("OH NO!"));
        logger.withContext("context1", "value1")
                .withContext("context2", "value2")
                .error("Hello world!", new RuntimeException("OH NO!"));

        logger.withMarker("LOGGING_DEMO").withContext("context", "value").info("Hello world!");
        logger.withMarker("LOGGING_DEMO").withContext("context", "value").debug("Hello world!");
        logger.withMarker("LOGGING_DEMO").withContext("context", "value").warn("Hello world!");
        logger.withMarker("LOGGING_DEMO").withContext("context", "value").error("Hello world!");

        logger.withMarker("LOGGING_DEMO")
                .withContext("context", "value")
                .info("Hello world!", new RuntimeException("OH NO!"));
        logger.withMarker("LOGGING_DEMO")
                .withContext("context", "value")
                .debug("Hello world!", new RuntimeException("OH NO!"));
        logger.withMarker("LOGGING_DEMO")
                .withContext("context", "value")
                .warn("Hello world!", new RuntimeException("OH NO!"));
        logger.withMarker("LOGGING_DEMO")
                .withContext("context", "value")
                .error("Hello world!", new RuntimeException("OH NO!"));

        Context.getGlobalContext().add("app", "demo");

        logger.info("Hello world!");
        logger.debug("Hello world!");
        logger.warn("Hello world!");
        logger.error("Hello world!");

        logger.withContext("context", "value").info("Hello world!");
        logger.withContext("context", "value").debug("Hello world!");
        logger.withContext("context", "value").warn("Hello world!");
        logger.withContext("context", "value").error("Hello world!");

        Context.getThreadLocalContext().add("transaction", "17");

        logger.info("Hello world!");
        logger.debug("Hello world!");
        logger.warn("Hello world!");
        logger.error("Hello world!");

        logger.withContext("context", "value").info("Hello world!");
        logger.withContext("context", "value").debug("Hello world!");
        logger.withContext("context", "value").warn("Hello world!");
        logger.withContext("context", "value").error("Hello world!");

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
                    Context.getThreadLocalContext().add("transaction", "18");

                    logger.info("Hello world!");
                    logger.debug("Hello world!");
                    logger.warn("Hello world!");
                    logger.error("Hello world!");

                    logger.withContext("context", "value").info("Hello world!");
                    logger.withContext("context", "value").debug("Hello world!");
                    logger.withContext("context", "value").warn("Hello world!");
                    logger.withContext("context", "value").error("Hello world!");
                })
                .get();
        executorService.shutdown();

        logger.info("Hello world!");
        logger.debug("Hello world!");
        logger.warn("Hello world!");
        logger.error("Hello world!");

        logger.withContext("context", "value").info("Hello world!");
        logger.withContext("context", "value").debug("Hello world!");
        logger.withContext("context", "value").warn("Hello world!");
        logger.withContext("context", "value").error("Hello world!");

        final System.Logger systemLogger = System.getLogger(Demo2.class.getName());
        systemLogger.log(System.Logger.Level.TRACE, "Hello system!");
        systemLogger.log(System.Logger.Level.INFO, "Hello system!");
        systemLogger.log(System.Logger.Level.DEBUG, "Hello system!");
        systemLogger.log(System.Logger.Level.WARNING, "Hello system!");
        systemLogger.log(System.Logger.Level.ERROR, "Hello system!");

        systemLogger.log(System.Logger.Level.TRACE, "Hello system!", new RuntimeException("OH NO!"));
        systemLogger.log(System.Logger.Level.INFO, "Hello system!", new RuntimeException("OH NO!"));
        systemLogger.log(System.Logger.Level.DEBUG, "Hello system!", new RuntimeException("OH NO!"));
        systemLogger.log(System.Logger.Level.WARNING, "Hello system!", new RuntimeException("OH NO!"));
        systemLogger.log(System.Logger.Level.ERROR, "Hello system!", new RuntimeException("OH NO!"));

        systemLogger.log(System.Logger.Level.TRACE, "Hello {0}!", "system");
        systemLogger.log(System.Logger.Level.INFO, "Hello {0}!", "system");
        systemLogger.log(System.Logger.Level.DEBUG, "Hello {0}!", "system");
        systemLogger.log(System.Logger.Level.WARNING, "Hello {0}!", "system");
        systemLogger.log(System.Logger.Level.ERROR, "Hello {0}!", "system");

        java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger(Demo2.class.getName());
        julLogger.severe("Hello jul!");
        julLogger.warning("Hello jul!");
        julLogger.info("Hello jul!");
        julLogger.config("Hello jul!");
        julLogger.fine("Hello jul!");
        julLogger.finer("Hello jul!");
        julLogger.finest("Hello jul!");

        julLogger.log(java.util.logging.Level.SEVERE, "Hello {0}!", "jul");
        julLogger.log(java.util.logging.Level.WARNING, "Hello {0}!", "jul");
        julLogger.log(java.util.logging.Level.INFO, "Hello {0}!", "jul");
        julLogger.log(java.util.logging.Level.CONFIG, "Hello {0}!", "jul");
        julLogger.log(java.util.logging.Level.FINE, "Hello {0}!", "jul");
        julLogger.log(java.util.logging.Level.FINER, "Hello {0}!", "jul");
        julLogger.log(java.util.logging.Level.FINEST, "Hello {0}!", "jul");

        org.apache.logging.log4j.Logger log4jLogger = org.apache.logging.log4j.LogManager.getLogger(Demo2.class);

        log4jLogger.error("Hello log4j!");
        log4jLogger.warn("Hello log4j!");
        log4jLogger.info("Hello log4j!");
        log4jLogger.debug("Hello log4j!");
        log4jLogger.trace("Hello log4j!");

        log4jLogger.error("Hello log4j!", new RuntimeException("OH NO!"));
        log4jLogger.warn("Hello log4j!", new RuntimeException("OH NO!"));
        log4jLogger.info("Hello log4j!", new RuntimeException("OH NO!"));
        log4jLogger.debug("Hello log4j!", new RuntimeException("OH NO!"));
        log4jLogger.trace("Hello log4j!", new RuntimeException("OH NO!"));

        log4jLogger.error("Hello {}!", "log4j");
        log4jLogger.warn("Hello {}!", "log4j");
        log4jLogger.info("Hello {}!", "log4j");
        log4jLogger.debug("Hello {}!", "log4j");
        log4jLogger.trace("Hello {}!", "log4j");

        log4jLogger.error(MarkerManager.getMarker("LOG4J-MARKER"), "Hello log4j!");
        log4jLogger.warn(MarkerManager.getMarker("LOG4J-MARKER"), "Hello log4j!");
        log4jLogger.info(MarkerManager.getMarker("LOG4J-MARKER"), "Hello log4j!");
        log4jLogger.debug(MarkerManager.getMarker("LOG4J-MARKER"), "Hello log4j!");
        log4jLogger.trace(MarkerManager.getMarker("LOG4J-MARKER"), "Hello log4j!");

        log4jLogger.error(MarkerManager.getMarker("LOG4J-MARKER"), "Hello log4j!", new RuntimeException("OH NO!"));
        log4jLogger.warn(MarkerManager.getMarker("LOG4J-MARKER"), "Hello log4j!", new RuntimeException("OH NO!"));
        log4jLogger.info(MarkerManager.getMarker("LOG4J-MARKER"), "Hello log4j!", new RuntimeException("OH NO!"));
        log4jLogger.debug(MarkerManager.getMarker("LOG4J-MARKER"), "Hello log4j!", new RuntimeException("OH NO!"));
        log4jLogger.trace(MarkerManager.getMarker("LOG4J-MARKER"), "Hello log4j!", new RuntimeException("OH NO!"));

        log4jLogger.error(MarkerManager.getMarker("LOG4J-MARKER"), "Hello {}!", "log4j");
        log4jLogger.warn(MarkerManager.getMarker("LOG4J-MARKER"), "Hello {}!", "log4j");
        log4jLogger.info(MarkerManager.getMarker("LOG4J-MARKER"), "Hello {}!", "log4j");
        log4jLogger.debug(MarkerManager.getMarker("LOG4J-MARKER"), "Hello {}!", "log4j");
        log4jLogger.trace(MarkerManager.getMarker("LOG4J-MARKER"), "Hello {}!", "log4j");
    }
}
