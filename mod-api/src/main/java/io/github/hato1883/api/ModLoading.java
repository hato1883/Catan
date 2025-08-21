package io.github.hato1883.api;

import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.asset.IModAssetLoader;
import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.services.IServiceLocator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class ModLoading {

    private static IServiceLocator serviceProvider;
    private static volatile IModDiscovery discovery;
    private static volatile IDependencyResolver dependencyResolver;
    private static volatile IModMetadataReader metadataReader;
    private static volatile IModClassLoaderFactory classLoaderFactory;
    private static volatile IModListenerScanner listenerScanner;
    private static volatile IEventListenerRegistrar listenerRegistrar;
    private static volatile IRegistryLoader registryLoader;
    private static volatile IModAssetLoader modAssetLoader;
    private static volatile IModInitializer initializer;

    // Prevent instantiation
    private ModLoading() {
        throw new UnsupportedOperationException("ModLoading is a utility class");
    }


    public static Builder builder() {
        return new Builder();
    }

    // FIXME: Provide a way for unit test to create a Mocked version of the ModLoading
    public static class Builder {
        private IServiceLocator serviceLocator;
        private final Map<Class<?>, Object> overrides = new HashMap<>();

        public Builder withServiceLocator(IServiceLocator locator) {
            this.serviceLocator = locator;
            return this;
        }

        // Allow specific service overrides for testing or customization
        public <T> Builder override(Class<T> serviceType, T implementation) {
            overrides.put(serviceType, implementation);
            return this;
        }

        public Builder withCustomDiscovery(IModDiscovery discovery) {
            return override(IModDiscovery.class, discovery);
        }

        public void build() {
            if (serviceLocator == null) {
                throw new IllegalStateException("ServiceLocator must be provided");
            }
            // The API should not know about OverrideServiceLocator. Assume the locator is already wrapped if needed.
            ModLoading.initialize(serviceLocator);
        }
    }

    public static IModDiscovery discovery() {
        if (discovery == null) discovery = getProvider().require(IModDiscovery.class);
        return discovery;
    }

    public static IDependencyResolver dependencyResolver() {
        if (dependencyResolver == null) dependencyResolver = getProvider().require(IDependencyResolver.class);
        return dependencyResolver;
    }

    public static IModMetadataReader metadataReader() {
        if (metadataReader == null) metadataReader = getProvider().require(IModMetadataReader.class);
        return metadataReader;
    }

    public static IModClassLoaderFactory classLoaderFactory() {
        if (classLoaderFactory == null) classLoaderFactory = getProvider().require(IModClassLoaderFactory.class);
        return classLoaderFactory;
    }

    public static IModListenerScanner listenerScanner() {
        if (listenerScanner == null) listenerScanner = getProvider().require(IModListenerScanner.class);
        return listenerScanner;
    }

    public static IEventListenerRegistrar listenerRegistrar() {
        if (listenerRegistrar == null) listenerRegistrar = getProvider().require(IEventListenerRegistrar.class);
        return listenerRegistrar;
    }

    public static IRegistryLoader registryLoader() {
        if (registryLoader == null) registryLoader = getProvider().require(IRegistryLoader.class);
        return registryLoader;
    }

    public static IModAssetLoader modAssetLoader() {
        if (modAssetLoader == null) modAssetLoader = getProvider().require(IModAssetLoader.class);
        return modAssetLoader;
    }

    public static IModInitializer initializer() {
        if (initializer == null) initializer = getProvider().require(IModInitializer.class);
        return initializer;
    }

    static void initialize(@NotNull IServiceLocator provider) {
        if (serviceProvider != null) {
            throw new IllegalStateException("ModLoading already initialized");
        }
        serviceProvider = provider;
    }

    /**
     * Resets all static state for testing/mocking purposes.
     */
    static void reset() {
        serviceProvider = null;
        discovery = null;
        dependencyResolver = null;
        metadataReader = null;
        classLoaderFactory = null;
        listenerScanner = null;
        listenerRegistrar = null;
        registryLoader = null;
        modAssetLoader = null;
        initializer = null;
    }

    private static IServiceLocator getProvider() {
        if (serviceProvider == null) {
            throw new IllegalStateException("ModLoading has not been initialized!");
        }
        return serviceProvider;
    }
}
