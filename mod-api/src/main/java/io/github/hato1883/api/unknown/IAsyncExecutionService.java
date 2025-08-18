package io.github.hato1883.api.unknown;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

// Abstraction for async execution (DIP)
public interface IAsyncExecutionService {
    CompletableFuture<Void> executeAsync(Runnable task);
    <T> CompletableFuture<T> executeAsync(Supplier<T> task);
    CompletableFuture<Void> executeAsync(Runnable task, String taskName);
    <T> CompletableFuture<T> executeAsync(Supplier<T> task, String taskName);

    // Batch operations for performance
    CompletableFuture<Void> executeAllAsync(Collection<Runnable> tasks);
    <T> CompletableFuture<List<T>> executeAllAsync(Collection<Supplier<T>> tasks, Class<T> resultType);

    // Lifecycle management
    void shutdown();
    boolean isShutdown();
    void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
}
