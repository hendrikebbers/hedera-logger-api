package com.swirlds.logging.usage;

import com.swirlds.base.context.Context;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Loggers;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlamegraphDemo {

    private final static Logger logger = Loggers.getLogger(FlamegraphDemo.class);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100000; i++) {
            run();
        }
    }

    public static void run() throws Exception {
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

        Context.getGlobalContext().put("app", "demo");

        logger.info("Hello world!");
        logger.debug("Hello world!");
        logger.warn("Hello world!");
        logger.error("Hello world!");

        logger.withContext("context", "value").info("Hello world!");
        logger.withContext("context", "value").debug("Hello world!");
        logger.withContext("context", "value").warn("Hello world!");
        logger.withContext("context", "value").error("Hello world!");

        Context.getThreadLocalContext().put("transaction", "17");

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
                    Context.getThreadLocalContext().put("transaction", "18");

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
    }
}
