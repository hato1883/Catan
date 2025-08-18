package io.github.hato1883.api;

import io.github.hato1883.api.service.IServiceLocator;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class ModServices {

    private static IServiceLocator serviceProvider;

    /**
     * Gets a service, throwing exception if not found.
     */
    public static <T> Optional<T> getService(Class<T> serviceType) {
        return getServiceProvider().getService(serviceType);
    }

    /**
     * Gets a service, throwing exception if not found.
     */
    public static <T> T requireService(Class<T> serviceType) {
        return getServiceProvider().requireService(serviceType);
    }

    private static IServiceLocator getServiceProvider() {
        if (serviceProvider == null) {
            throw new IllegalStateException("ModService has not been initialized!");
        }
        return serviceProvider;
    }

    public static void initialize(@NotNull IServiceLocator provider) {
        if (serviceProvider != null) {
            throw new IllegalStateException("ModService already initialized");
        }
        serviceProvider = provider;
    }
}
