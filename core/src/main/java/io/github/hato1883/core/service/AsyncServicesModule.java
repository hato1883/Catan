package io.github.hato1883.core.service;

import io.github.hato1883.api.events.IEventBusService;
import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.service.IServiceModule;
import io.github.hato1883.api.service.IServiceRegistrar;
import io.github.hato1883.api.unknown.IAsyncExecutionService;
import io.github.hato1883.core.events.EventBusServiceImpl;
import io.github.hato1883.core.events.EventListenerRegistrar;
import io.github.hato1883.core.unknown.AsyncExecutionService;
import io.github.hato1883.core.unknown.AsyncExecutorConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class AsyncServicesModule implements IServiceModule {

    @Override
    public void registerServices(IServiceRegistrar registrar) {
        registerAsyncExecutionService(registrar);
        registerEventBusService(registrar);
    }

    @Override
    public String getModuleName() {
        return "AsyncServices";
    }

    private void registerAsyncExecutionService(IServiceRegistrar registrar) {
        AsyncExecutorConfig config = new AsyncExecutorConfig.Builder()
            .withGeneralPoolSize(getOptimalGeneralPoolSize())
            .withIoPoolSize(getOptimalIoPoolSize())
            .build();

        registrar.register(IAsyncExecutionService.class, (Supplier<? extends IAsyncExecutionService>) () -> new AsyncExecutionService(config));
    }

    private void registerEventBusService(IServiceRegistrar registrar) {
        registrar.register(IEventBusService.class, (Supplier<? extends IEventBusService>) () -> {
            IAsyncExecutionService asyncService = registrar.requireService(IAsyncExecutionService.class);
            return new EventBusServiceImpl(asyncService, true);
        });

        registrar.registerIfAbsent(IEventListenerRegistrar.class, (Supplier<? extends IEventListenerRegistrar>) EventListenerRegistrar::new);
    }

    private ExecutorService createEventExecutor() {
        return Executors.newFixedThreadPool(2, r -> {
            Thread t = new Thread(r, "Catan-EventBus");
            t.setDaemon(true);
            return t;
        });
    }

    private static int getOptimalGeneralPoolSize() {
        int cores = Runtime.getRuntime().availableProcessors();
        return Math.max(2, cores - 1);
    }

    private static int getOptimalIoPoolSize() {
        int cores = Runtime.getRuntime().availableProcessors();
        return Math.max(2, cores);
    }
}
