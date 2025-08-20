package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.events.IEventBusService;
import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.services.IServiceModule;
import io.github.hato1883.api.services.IServiceRegistrar;
import io.github.hato1883.api.async.IAsyncExecutionService;
import io.github.hato1883.core.events.bus.EventBusService;
import io.github.hato1883.core.events.bus.EventListenerRegistrar;
import io.github.hato1883.core.async.AsyncExecutionService;
import io.github.hato1883.core.config.AsyncExecutorConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class AsyncServicesModule implements IServiceModule {

    @Override
    public void registerServices(IServiceContainer registrar) {
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

        registrar.registerIfAbsent(IAsyncExecutionService.class, (Supplier<? extends IAsyncExecutionService>) () -> new AsyncExecutionService(config));
    }

    private void registerEventBusService(IServiceContainer registrar) {
        registrar.registerIfAbsent(IEventBusService.class, (Supplier<? extends IEventBusService>) () -> {
            IAsyncExecutionService asyncService = registrar.require(IAsyncExecutionService.class);
            return new EventBusService(asyncService, true);
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
