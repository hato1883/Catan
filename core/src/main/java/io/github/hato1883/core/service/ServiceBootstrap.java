package io.github.hato1883.core.service;

import io.github.hato1883.api.service.IServiceModule;
import io.github.hato1883.api.service.IServiceRegistrar;

public final class ServiceBootstrap {

    private ServiceBootstrap() {} // prevent instantiation

    private static ServiceLocator provider;
    private static boolean init = false;

    /**
     * Initializes a singleton instance of a ServiceLocator with core services.
     * Services are organized into modules for maintainability.
     *
     * @throws IllegalStateException if already initialized
     */
    public static void initialize() {
        if (init) {
            throw new IllegalStateException("ServiceBootstrap is already initialized");
        }
        init = true;
        provider = new ServiceLocator();

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
        provider = new ServiceLocator();

        ServiceModuleManager moduleManager = new ServiceModuleManager.Builder()
            .withCoreModules()
            .withModules(customModules)
            .build();

        IServiceRegistrar registrar = new ServiceRegistrarImpl(provider);
        moduleManager.registerAllServices(registrar);
    }

    /**
     * Retrieves the singleton Service Provider.
     *
     * @return singleton ServiceLocator instance
     * @throws IllegalStateException if not initialized
     */
    public static ServiceLocator getProvider() {
        if (!init) {
            throw new IllegalStateException("ServiceBootstrap is not initialized!");
        }
        return provider;
    }

    private static void registerCoreServices() {
        ServiceModuleManager moduleManager = new ServiceModuleManager.Builder()
            .withCoreModules()
            .build();

        IServiceRegistrar registrar = new ServiceRegistrarImpl(provider);
        moduleManager.registerAllServices(registrar);
    }
}
