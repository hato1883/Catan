package io.github.hato1883.core.config;

import io.github.hato1883.api.LogManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

// Configuration class for executor setup (SRP, OCP)
public class AsyncExecutorConfig {
    private final int generalPoolSize;
    private final int ioPoolSize;
    private final int scheduledPoolSize;
    private final ThreadFactory threadFactory;

    private AsyncExecutorConfig(Builder builder) {
        this.generalPoolSize = builder.generalPoolSize;
        this.ioPoolSize = builder.ioPoolSize;
        this.scheduledPoolSize = builder.scheduledPoolSize;
        this.threadFactory = builder.threadFactory;
    }

    public ExecutorService createGeneralPool() {
        return Executors.newFixedThreadPool(generalPoolSize,
            createNamedThreadFactory("Catan-General"));
    }

    public ExecutorService createIoPool() {
        return Executors.newFixedThreadPool(ioPoolSize,
            createNamedThreadFactory("Catan-IO"));
    }

    public ScheduledExecutorService createScheduledPool() {
        return Executors.newScheduledThreadPool(scheduledPoolSize,
            createNamedThreadFactory("Catan-Scheduled"));
    }

    private ThreadFactory createNamedThreadFactory(String prefix) {
        AtomicInteger counter = new AtomicInteger(0);
        return r -> {
            Thread thread = threadFactory != null ? threadFactory.newThread(r) : new Thread(r);
            thread.setName(prefix + "-" + counter.incrementAndGet());
            thread.setDaemon(true);
            thread.setUncaughtExceptionHandler((t, e) -> {
                LogManager.getLogger("").error("Uncaught exception in thread {}: {}", t.getName(), e.getMessage(), e);
            });
            return thread;
        };
    }

    public static class Builder {
        private int generalPoolSize = Runtime.getRuntime().availableProcessors();
        private int ioPoolSize = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);
        private int scheduledPoolSize = 2;
        private ThreadFactory threadFactory = null;

        public Builder withGeneralPoolSize(int size) {
            this.generalPoolSize = Math.max(1, size);
            return this;
        }

        public Builder withIoPoolSize(int size) {
            this.ioPoolSize = Math.max(1, size);
            return this;
        }

        public Builder withScheduledPoolSize(int size) {
            this.scheduledPoolSize = Math.max(1, size);
            return this;
        }

        public Builder withThreadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        public AsyncExecutorConfig build() {
            return new AsyncExecutorConfig(this);
        }
    }
}
