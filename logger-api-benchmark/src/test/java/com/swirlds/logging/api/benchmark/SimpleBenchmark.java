package com.swirlds.logging.api.benchmark;

import static com.swirlds.logging.api.benchmark.BenchmarkConstants.FORK_COUNT;
import static com.swirlds.logging.api.benchmark.BenchmarkConstants.MEASUREMENT_ITERATIONS;
import static com.swirlds.logging.api.benchmark.BenchmarkConstants.MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION;
import static com.swirlds.logging.api.benchmark.BenchmarkConstants.PARALLEL_THREAD_COUNT;
import static com.swirlds.logging.api.benchmark.BenchmarkConstants.WARMUP_ITERATIONS;
import static com.swirlds.logging.api.benchmark.BenchmarkConstants.WARMUP_TIME_IN_SECONDS_PER_ITERATION;

import com.swirlds.logging.api.Logger;
import com.swirlds.logging.api.internal.LoggingSystem;
import com.swirlds.logging.handler.console.ConsoleHandler;
import java.util.UUID;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@Fork(FORK_COUNT)
@Threads(PARALLEL_THREAD_COUNT)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
@Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
public class SimpleBenchmark {

    private Logger logger;

    private static final Throwable THROWABLE = new RuntimeException("OH NO!");

    @Setup(org.openjdk.jmh.annotations.Level.Iteration)
    public void init(Blackhole blackhole) throws Exception {
        SimpleConfiguration configuration = new SimpleConfiguration();
        configuration.setProperty("logging.handler.console.enabled", "true");
        configuration.setProperty("logging.level", "TRACE");
        LoggingSystem loggingSystem = new LoggingSystem(configuration);
        loggingSystem.addHandler(new BlackholeHandler(blackhole));
        loggingSystem.addHandler(new ConsoleHandler(configuration));
        logger = loggingSystem.getLogger(SimpleBenchmark.class.getName());
    }

    @Benchmark
    public void simpleLogging() {
        logger.info("Hello World");
    }

    @Benchmark
    public void logLikeHell() {
        logger.info("L0, Hello world!");
        logger.info("L1, A quick brown fox jumps over the lazy dog.");
        logger.info("L2, Hello world!", THROWABLE);
        logger.info("L3, Hello {}!", "placeholder");
        logger.info("L4, Hello {}!", THROWABLE, "placeholder");
        logger.withContext("key", "value").info("L5, Hello world!");
        logger.withMarker("marker").info("L6, Hello world!");
        logger.withContext("user-id", UUID.randomUUID().toString())
                .info("L7, Hello world!");
        logger.withContext("user-id", UUID.randomUUID().toString())
                .info("L8, Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!",
                        1, 2, 3, 4, 5, 6, 7, 8, 9);
        logger.withContext("user-id", UUID.randomUUID().toString())
                .info("L9, Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!", THROWABLE,
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
    }

}
