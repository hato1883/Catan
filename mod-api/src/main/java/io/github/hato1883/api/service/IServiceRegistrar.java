package io.github.hato1883.api.service;

import java.util.function.Supplier;

public interface IServiceRegistrar {
    <T> void register(Class<T> serviceType, Supplier<? extends T> supplier);
    <T> void registerIfAbsent(Class<T> serviceType, Supplier<? extends T> supplier);
    <T> void register(Class<T> serviceType, T instance);
    <T> void registerIfAbsent(Class<T> serviceType, T instance);
    <T> T requireService(Class<T> serviceType);
}
