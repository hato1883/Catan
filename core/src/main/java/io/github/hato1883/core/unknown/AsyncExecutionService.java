package io.github.hato1883.core.unknown;


import io.github.hato1883.api.unknown.IAsyncExecutionService;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// Implementation with configurable thread pools
public class AsyncExecutionService implements IAsyncExecutionService {
    private final ExecutorService generalPool;
    private final ExecutorService ioPool;
    private final ScheduledExecutorService scheduledPool;
    private final boolean ownsExecutors;
    private volatile boolean shutdown = false;

    // Constructor with default configuration
    public AsyncExecutionService() {
        this(createDefaultConfiguration());
    }

    // Constructor with custom configuration (DIP - depend on configuration abstraction)
    public AsyncExecutionService(AsyncExecutorConfig config) {
        this.generalPool = config.createGeneralPool();
        this.ioPool = config.createIoPool();
        this.scheduledPool = config.createScheduledPool();
        this.ownsExecutors = true;
    }

    // Constructor for dependency injection of existing executors
    public AsyncExecutionService(ExecutorService generalPool, ExecutorService ioPool,
                                 ScheduledExecutorService scheduledPool) {
        this.generalPool = generalPool;
        this.ioPool = ioPool;
        this.scheduledPool = scheduledPool;
        this.ownsExecutors = false;
    }

    @Override
    public CompletableFuture<Void> executeAsync(Runnable task) {
        return executeAsync(task, "unnamed-task");
    }

    @Override
    public <T> CompletableFuture<T> executeAsync(Supplier<T> task) {
        return executeAsync(task, "unnamed-supplier");
    }

    @Override
    public CompletableFuture<Void> executeAsync(Runnable task, String taskName) {
        checkNotShutdown();
        return CompletableFuture.runAsync(wrapWithErrorHandling(task, taskName), getAppropriateExecutor(taskName));
    }

    @Override
    public <T> CompletableFuture<T> executeAsync(Supplier<T> task, String taskName) {
        checkNotShutdown();
        return CompletableFuture.supplyAsync(wrapSupplierWithErrorHandling(task, taskName), getAppropriateExecutor(taskName));
    }

    @Override
    public CompletableFuture<Void> executeAllAsync(Collection<Runnable> tasks) {
        checkNotShutdown();
        List<CompletableFuture<Void>> futures = tasks.stream()
            .map(task -> executeAsync(task, "batch-task"))
            .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public <T> CompletableFuture<List<T>> executeAllAsync(Collection<Supplier<T>> tasks, Class<T> resultType) {
        checkNotShutdown();
        List<CompletableFuture<T>> futures = tasks.stream()
            .map(task -> executeAsync(task, "batch-supplier"))
            .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }

    @Override
    public void shutdown() {
        if (!shutdown && ownsExecutors) {
            shutdown = true;
            shutdownExecutor(generalPool, "general");
            shutdownExecutor(ioPool, "io");
            shutdownExecutor(scheduledPool, "scheduled");
        }
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        if (!ownsExecutors) return;

        long timeoutMillis = unit.toMillis(timeout);
        long deadline = System.currentTimeMillis() + timeoutMillis;

        awaitExecutorTermination(generalPool, "general", deadline);
        awaitExecutorTermination(ioPool, "io", deadline);
        awaitExecutorTermination(scheduledPool, "scheduled", deadline);
    }

    // Private helper methods (SRP)
    private ExecutorService getAppropriateExecutor(String taskName) {
        // Route IO-heavy tasks to IO pool (file loading, network, etc.)
        if (taskName.contains("load") || taskName.contains("io") || taskName.contains("file")) {
            return ioPool;
        }
        if (taskName.contains("event")) {
            return scheduledPool;
        }
        return generalPool;
    }

    private Runnable wrapWithErrorHandling(Runnable task, String taskName) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                handleAsyncException(taskName, e);
            }
        };
    }

    private <T> Supplier<T> wrapSupplierWithErrorHandling(Supplier<T> task, String taskName) {
        return () -> {
            try {
                return task.get();
            } catch (Exception e) {
                handleAsyncException(taskName, e);
                return null;
            }
        };
    }

    private void handleAsyncException(String taskName, Exception e) {
        // In LibGDX, you might want to use Gdx.app.error instead
        System.err.println("Error in async task '" + taskName + "': " + e.getMessage());
        e.printStackTrace();

        // Could also post an error event to the event bus
        // eventBus.dispatchAsync(new AsyncTaskErrorEvent(taskName, e));
    }

    private void checkNotShutdown() {
        if (shutdown) {
            throw new IllegalStateException("AsyncExecutionService has been shutdown");
        }
    }

    private void shutdownExecutor(ExecutorService executor, String name) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Executor '" + name + "' did not terminate gracefully, forcing shutdown");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void awaitExecutorTermination(ExecutorService executor, String name, long deadline)
        throws InterruptedException {
        long remaining = deadline - System.currentTimeMillis();
        if (remaining > 0 && !executor.awaitTermination(remaining, TimeUnit.MILLISECONDS)) {
            System.err.println("Executor '" + name + "' did not terminate within timeout");
        }
    }

    private static AsyncExecutorConfig createDefaultConfiguration() {
        return new AsyncExecutorConfig.Builder()
            .withGeneralPoolSize(Math.max(2, Runtime.getRuntime().availableProcessors()))
            .withIoPoolSize(Math.max(2, Runtime.getRuntime().availableProcessors() / 2))
            .withScheduledPoolSize(2)
            .build();
    }
}
