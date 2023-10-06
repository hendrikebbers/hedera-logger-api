package com.swirlds.logging.api.test;

import com.swirlds.base.testfixture.concurrent.TestExecutor;
import com.swirlds.base.testfixture.concurrent.WithTestExecutor;
import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.internal.level.LoggingLevelConfig;
import com.swirlds.logging.api.test.util.SimpleConfiguration;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

@WithTestExecutor
public class LoggingLevelConfigStressTest {

    @Inject
    TestExecutor testExecutor;

    private static Level getRandomLevel() {
        final Level[] levels = Level.values();
        return levels[(int) (Math.random() * levels.length)];
    }

    private static Runnable createRunnable(final LoggingLevelConfig config) {
        return () -> {
            for (int i = 0; i < 20; i++) {
                // update config
                final Configuration configuration = new SimpleConfiguration()
                        .withProperty("logging.level", getRandomLevel().name())
                        .withProperty("logging.level.com.sample", getRandomLevel().name())
                        .withProperty("logging.level.com.sample.package", getRandomLevel().name())
                        .withProperty("logging.level.com.sample.package.Class", getRandomLevel().name());
                config.update(configuration);

                // do some checks
                int count = (int) (Math.random() * 20.0d);
                for (int j = 0; j < count; j++) {
                    config.isEnabled(null, Level.ERROR);
                    config.isEnabled(null, Level.WARN);
                    config.isEnabled(null, Level.INFO);
                    config.isEnabled(null, Level.DEBUG);
                    config.isEnabled(null, Level.TRACE);
                    config.isEnabled("logging.level", Level.ERROR);
                    config.isEnabled("logging.level.a", Level.WARN);
                    config.isEnabled("logging.level.b", Level.INFO);
                    config.isEnabled("logging.level.c", Level.DEBUG);
                    config.isEnabled("logging.level.d", Level.TRACE);
                    config.isEnabled("logging.level.other", Level.ERROR);
                    config.isEnabled("logging.level.other.a", Level.WARN);
                    config.isEnabled("logging.level.other.b", Level.INFO);
                    config.isEnabled("logging.level.other.c", Level.DEBUG);
                    config.isEnabled("logging.level.other.d", Level.TRACE);
                    config.isEnabled("logging.level.com.sample.package.Class", Level.ERROR);
                    config.isEnabled("logging.level.com.sample.package.Class", Level.WARN);
                    config.isEnabled("logging.level.com.sample.package.Class", Level.INFO);
                    config.isEnabled("logging.level.com.sample.package.Class", Level.DEBUG);
                    config.isEnabled("logging.level.com.sample.package.Class", Level.TRACE);
                    config.isEnabled("logging.level.com.sample.package.Class" + j, Level.ERROR);
                    config.isEnabled("logging.level.com.sample.package.Class" + j, Level.WARN);
                    config.isEnabled("logging.level.com.sample.package.Class" + j, Level.INFO);
                    config.isEnabled("logging.level.com.sample.package.Class" + j, Level.DEBUG);
                    config.isEnabled("logging.level.com.sample.package.Class" + j, Level.TRACE);
                }

                try {
                    Thread.sleep((long) (Math.random() * 20.0d));
                } catch (InterruptedException e) {
                    throw new RuntimeException("INTERRUPT!", e);
                }
            }
        };
    }

    @Test
    void testWithConfig() {
        //given
        final LoggingLevelConfig config = new LoggingLevelConfig(new SimpleConfiguration());
        final List<Runnable> runnables = IntStream.range(0, 20)
                .mapToObj(i -> createRunnable(config))
                .toList();
        testExecutor.executeAndWait(runnables);
    }
}
