package com.swirlds.logging.usage;

import com.swirlds.base.context.Context;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.Loggers;
import java.util.concurrent.Executors;

public class Demo2 {

    private final static Logger logger = Loggers.getLogger(Demo2.class);

    public static void main(String[] args) throws Exception {

        logger.info("Hello world!");
        logger.debug("Hello world!");
        logger.warn("Hello world!");
        logger.error("Hello world!");

        logger.info("Hello world!", new RuntimeException("OH NO!"));
        logger.debug("Hello world!", new RuntimeException("OH NO!"));
        logger.warn("Hello world!", new RuntimeException("OH NO!"));
        logger.error("Hello world!", new RuntimeException("OH NO!"));

        logger.info(() -> "Hello world!");
        logger.debug(() -> "Hello world!");
        logger.warn(() -> "Hello world!");
        logger.error(() -> "Hello world!");

        logger.info(() -> "Hello world!", new RuntimeException("OH NO!"));
        logger.debug(() -> "Hello world!", new RuntimeException("OH NO!"));
        logger.warn(() -> "Hello world!", new RuntimeException("OH NO!"));
        logger.error(() -> "Hello world!", new RuntimeException("OH NO!"));

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

        Executors.newSingleThreadExecutor()
                .submit(() -> {
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

        logger.info("Hello world!");
        logger.debug("Hello world!");
        logger.warn("Hello world!");
        logger.error("Hello world!");

        logger.withContext("context", "value").info("Hello world!");
        logger.withContext("context", "value").debug("Hello world!");
        logger.withContext("context", "value").warn("Hello world!");
        logger.withContext("context", "value").error("Hello world!");
    }
}
