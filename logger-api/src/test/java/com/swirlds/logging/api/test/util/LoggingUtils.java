package com.swirlds.logging.api.test.util;

import com.swirlds.logging.api.Logger;
import java.util.UUID;
import java.util.stream.IntStream;

public class LoggingUtils {

    public static void logLikeHell(Logger logger) {
        IntStream.range(0, 100).forEach(i -> {
            logger.info("L0, Hello world!");
            logger.info("L1, A quick brown fox jumps over the lazy dog.");
            logger.info("L2, Hello world!", new RuntimeException("test"));
            logger.info("L3, Hello {}!", "placeholder");
            logger.info("L4, Hello {}!", new RuntimeException("test"), "placeholder");
            logger.withContext("key", "value").info("L5, Hello world!");
            logger.withMarker("marker").info("L6, Hello world!");
            logger.withContext("user-id", UUID.randomUUID().toString())
                    .info("L7, Hello world!");
            logger.withContext("user-id", UUID.randomUUID().toString())
                    .info("L8, Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!",
                            1, 2, 3, 4, 5, 6, 7, 8, 9);
            logger.withContext("user-id", UUID.randomUUID().toString())
                    .info("L9, Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!", new RuntimeException("test"),
                            1, 2, 3, 4, 5, 6, 7, 8, 9);
            logger.withContext("user-id", UUID.randomUUID().toString())
                    .withContext("key", "value")
                    .info("L10, Hello world!");
            logger.withMarker("marker")
                    .info("L11, Hello world!");
            logger.withMarker("marker1")
                    .withMarker("marker2")
                    .info("L12, Hello world!");
            logger.withContext("key", "value")
                    .withMarker("marker1").withMarker("marker2")
                    .info("L13, Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!",
                            1, 2, 3, 4, 5, 6, 7, 8, 9);
        });
    }
}
