package io.github.hato1883.core.service;

import io.github.hato1883.api.service.IServiceRegistrar;

import java.util.Objects;
import java.util.function.Supplier;

public class ServiceRegistrarImpl implements IServiceRegistrar {
    private final ServiceLocator serviceLocator;

    public ServiceRegistrarImpl(ServiceLocator serviceLocator) {
        this.serviceLocator = Objects.requireNonNull(serviceLocator, "ServiceLocator cannot be null");
    }

    @Override
    public <T> void register(Class<T> serviceType, Supplier<? extends T> supplier) {
        serviceLocator.registerService(serviceType, supplier);
    }

    @Override
    public <T> void registerIfAbsent(Class<T> serviceType, Supplier<? extends T> supplier) {
        serviceLocator.registerServiceIfAbsent(serviceType, supplier);
    }

    @Override
    public <T> void register(Class<T> serviceType, T instance) {
        serviceLocator.registerService(serviceType, instance);
    }

    @Override
    public <T> void registerIfAbsent(Class<T> serviceType, T instance) {
        serviceLocator.registerServiceIfAbsent(serviceType, instance);
    }

    @Override
    public <T> T requireService(Class<T> serviceType) {
        return serviceLocator.requireService(serviceType);
    }
}
