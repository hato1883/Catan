package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.services.IServiceModule;
import io.github.hato1883.core.services.ServiceContainer;

public final class ServiceBootstrap {

    private ServiceBootstrap() {} // prevent instantiation

    private static IServiceContainer provider;
    private static boolean init = false;

    /**
     * Initializes a singleton instance of a ServiceContainer with core services.
     * Services are organized into modules for maintainability.
     *
     * @throws IllegalStateException if already initialized
     */
    public static void initialize() {
        if (init) {
            throw new IllegalStateException("ServiceBootstrap is already initialized");
        }
        init = true;
        provider = new ServiceContainer();

        registerCoreServices();
    }

    /**
     * Initializes with custom service modules for extensibility.
     *
     * @param customModules additional service modules to register
     * @throws IllegalStateException if already initialized
     */
    public static void initialize(IServiceModule... customModules) {
        if (init) {
            throw new IllegalStateException("ServiceBootstrap is already initialized");
        }
        init = true;
        provider = new ServiceContainer();

        ServiceModuleManager moduleManager = new ServiceModuleManager.Builder()
            .withCoreModules()
            .withModules(customModules)
            .build();

        moduleManager.registerAllServices(provider);
    }

    /**
     * Retrieves the singleton Service Provider.
     *
     * @return singleton ServiceContainer instance
     * @throws IllegalStateException if not initialized
     */
    public static IServiceContainer getContainer() {
        if (!init) {
            throw new IllegalStateException("ServiceBootstrap is not initialized!");
        }
        return provider;
    }

    private static void registerCoreServices() {
        ServiceModuleManager moduleManager = new ServiceModuleManager.Builder()
            .withCoreModules()
            .build();

        moduleManager.registerAllServices(provider);
    }
}
