package com.swirlds.logging.api.internal;

import com.swirlds.logging.api.Logger;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoggingSystemParallelStressTest {

    @Test
    void testMultipleLoggersInParallel() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        final InMemoryHandler handler = new InMemoryHandler();
        loggingSystem.addHandler(handler);
        final ParallelStressTester stressTester = new ParallelStressTester(Duration.ofSeconds(60));
        final List<Runnable> runnables = IntStream.range(0, 100)
                .mapToObj(i -> loggingSystem.getLogger("logger-" + i))
                .map(l -> (Runnable) () -> logLikeHell(l))
                .collect(Collectors.toList());

        //when
        stressTester.runInParallel(runnables);

        //then
        Assertions.assertEquals(140000, handler.getEvents().size());
        IntStream.range(0, 100).forEach(i -> Assertions.assertEquals(1400,
                handler.getEvents().stream().filter(e -> Objects.equals(e.loggerName(), "logger-" + i)).count()));

    }

    @Test
    void testOneLoggerInParallel() {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        final Logger logger = loggingSystem.getLogger("logger");
        final InMemoryHandler handler = new InMemoryHandler();
        loggingSystem.addHandler(handler);
        final ParallelStressTester stressTester = new ParallelStressTester(Duration.ofSeconds(60));
        final List<Runnable> runnables = IntStream.range(0, 100)
                .mapToObj(l -> (Runnable) () -> logLikeHell(logger))
                .collect(Collectors.toList());

        //when
        stressTester.runInParallel(runnables);

        //then
        Assertions.assertEquals(140000, handler.getEvents().size());
    }

    private void logLikeHell(Logger logger) {
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
