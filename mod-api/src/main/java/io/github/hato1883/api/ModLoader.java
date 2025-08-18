package io.github.hato1883.api;

import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.*;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.asset.IModAssetLoader;
import io.github.hato1883.api.registries.*;
import io.github.hato1883.api.service.IServiceLocator;
import org.jetbrains.annotations.NotNull;

public final class ModLoader {

    private static IServiceLocator serviceProvider;

    public static IModDiscovery discovery() { return getProvider().requireService(IModDiscovery.class); }
    public static IModMetadataReader metadataReader() { return getProvider().requireService(IModMetadataReader.class); }
    public static IModClassLoaderFactory classLoaderFactory() { return getProvider().requireService(IModClassLoaderFactory.class); }
    public static IModListenerScanner listenerScanner() { return getProvider().requireService(IModListenerScanner.class); }
    public static IEventListenerRegistrar listenerRegistrar() { return getProvider().requireService(IEventListenerRegistrar.class); }
    public static IRegistryLoader registryLoader() { return getProvider().requireService(IRegistryLoader.class); }
    public static IModAssetLoader modAssetLoader() { return getProvider().requireService(IModAssetLoader.class); }
    public static IModInitializer initializer() { return getProvider().requireService(IModInitializer.class); }

    public static void initialize(@NotNull IServiceLocator provider) {
        if (serviceProvider != null) {
            throw new IllegalStateException("ModLoader already initialized");
        }
        serviceProvider = provider;
    }

    private static IServiceLocator getProvider() {
        if (serviceProvider == null) {
            throw new IllegalStateException("ModLoader has not been initialized!");
        }
        return serviceProvider;
    }
}
