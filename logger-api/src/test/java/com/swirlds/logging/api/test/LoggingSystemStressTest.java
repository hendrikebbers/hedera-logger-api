package com.swirlds.logging.api.test;

import com.swirlds.base.testfixture.concurrent.TestExecutor;
import com.swirlds.base.testfixture.concurrent.WithTestExecutor;
import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.internal.LoggingSystem;
import com.swirlds.logging.api.test.util.InMemoryHandler;
import com.swirlds.logging.api.test.util.LoggingUtils;
import com.swirlds.logging.api.test.util.SimpleConfiguration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@WithTestExecutor
public class LoggingSystemStressTest {

    @Test
    void testMultipleLoggersInParallel(TestExecutor testExecutor) {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        final InMemoryHandler handler = new InMemoryHandler();
        loggingSystem.addHandler(handler);
        final List<Runnable> runnables = IntStream.range(0, 100)
                .mapToObj(i -> loggingSystem.getLogger("logger-" + i))
                .map(l -> (Runnable) () -> LoggingUtils.logLikeHell(l))
                .collect(Collectors.toList());

        //when
        testExecutor.executeAndWait(runnables);

        //then
        Assertions.assertEquals(140000, handler.getEvents().size());
        IntStream.range(0, 100).forEach(i -> Assertions.assertEquals(1400,
                handler.getEvents().stream().filter(e -> Objects.equals(e.loggerName(), "logger-" + i)).count()));

    }

    @Test
    void testOneLoggerInParallel(TestExecutor testExecutor) {
        //given
        final SimpleConfiguration configuration = new SimpleConfiguration();
        final LoggingSystem loggingSystem = new LoggingSystem(configuration);
        final Logger logger = loggingSystem.getLogger("logger");
        final InMemoryHandler handler = new InMemoryHandler();
        loggingSystem.addHandler(handler);
        final List<Runnable> runnables = IntStream.range(0, 100)
                .mapToObj(l -> (Runnable) () -> LoggingUtils.logLikeHell(logger))
                .collect(Collectors.toList());

        //when
        testExecutor.executeAndWait(runnables);

        //then
        Assertions.assertEquals(140000, handler.getEvents().size());
    }
}
