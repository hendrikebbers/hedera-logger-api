package com.swirlds.logging.api.internal;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ParallelStressTester {

    private static final ExecutorService SINGLE_EXECUTOR = Executors.newSingleThreadExecutor();

    private static final ExecutorService POOL_EXECUTOR = Executors.newCachedThreadPool();

    private final Duration maxWaitTime;

    public ParallelStressTester(Duration maxWaitTime) {
        this.maxWaitTime = Objects.requireNonNull(maxWaitTime, "maxWaitTime must not be null");
    }

    public ParallelStressTester() {
        this(Duration.ofMinutes(1));
    }

    public void runInParallel(Collection<Runnable> runnables) {
        runInParallel(runnables.toArray(new Runnable[runnables.size()]));
    }

    public void runInParallel(Runnable... runnable) {
        List<Future<Void>> results = runInParallelImpl(runnable);
        try {
            waitForAllDone(results);
        } catch (Exception e) {
            throw new RuntimeException("Error in wait", e);
        }
    }

    public List<Future<Void>> runInParallelImpl(Runnable... runnable) {
        final List<Future<Void>> results = new ArrayList<>();

        final Lock callLock = new ReentrantLock();
        final Condition allPassedToExecutor = callLock.newCondition();

        callLock.lock();
        try {
            SINGLE_EXECUTOR.submit(() -> {
                callLock.lock();
                try {
                    Arrays.stream(runnable)
                            .map(r -> POOL_EXECUTOR.submit(r, (Void) null))
                            .forEach(results::add);
                    allPassedToExecutor.signal(); // now all futures in results and the original method can return
                } finally {
                    callLock.unlock();
                }

                //In the single executor we will wait until all tasks are done.
                // By doing so we ensure that only 1 call to this utils is executed in parallel. All other calls will be queued.
                try {
                    waitForAllDone(results);
                } catch (Exception e) {
                    throw new RuntimeException("Error in wait", e);
                }
            });
            allPassedToExecutor.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupt in wait", e);
        } finally {
            callLock.unlock();
        }

        return results;
    }

    private long waitFor(Future<Void> future, long maxWaitInMillis)
            throws ExecutionException, InterruptedException, TimeoutException {
        long startTime = System.currentTimeMillis();
        future.get(maxWaitInMillis, MILLISECONDS);
        return maxWaitInMillis - (System.currentTimeMillis() - startTime);
    }

    private void waitForAllDone(List<Future<Void>> results)
            throws InterruptedException, ExecutionException, TimeoutException {
        long remainingAllowedTimeInMillis = maxWaitTime.toMillis();
        if (results.isEmpty()) {
            return;
        }
        final Future<Void> first = results.get(0);
        remainingAllowedTimeInMillis = waitFor(first, remainingAllowedTimeInMillis);
        for (final Future<Void> future : results) {
            if (remainingAllowedTimeInMillis > 0) {
                remainingAllowedTimeInMillis = waitFor(future, remainingAllowedTimeInMillis);
            }
            if (!future.isDone()) {
                throw new IllegalStateException("Not all futures are done after " + maxWaitTime);
            }
        }
    }
}
