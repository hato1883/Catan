package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.events.IEventBusService;
import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.asset.*;
import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.services.IServiceModule;
import io.github.hato1883.api.async.IAsyncExecutionService;
import io.github.hato1883.core.events.bus.EventListenerRegistrar;
import io.github.hato1883.core.events.listeners.ClassGraphListenerScanner;
import io.github.hato1883.core.modloading.assets.ModTextureModAssetLoader;
import io.github.hato1883.core.modloading.assets.textures.CombinedTextureDiscoveryService;
import io.github.hato1883.core.modloading.assets.textures.DefaultTextureAtlasBuilder;
import io.github.hato1883.core.modloading.dependency.DependencyResolver;
import io.github.hato1883.core.modloading.loading.*;
import io.github.hato1883.core.common.util.PathResolver;

import java.util.function.Supplier;

public class ModLoadingServicesModule implements IServiceModule {

    @Override
    public void registerServices(IServiceContainer serviceContainer) {
        serviceContainer.registerIfAbsent(
            IModDiscovery.class,
            (Supplier<? extends IModDiscovery>) FilesystemModDiscovery::new
        );
        serviceContainer.registerIfAbsent(
            IDependencyResolver.class,
            (Supplier<? extends IDependencyResolver>) DependencyResolver::new
        );
        serviceContainer.registerIfAbsent(
            IModMetadataReader.class,
            (Supplier<? extends IModMetadataReader>) DefaultModMetadataReader::new
        );
        serviceContainer.registerIfAbsent(
            IModClassLoaderFactory.class,
            (Supplier<? extends IModClassLoaderFactory>) () -> new UrlModClassLoaderFactory(
                ModLoader.class.getClassLoader()
            )
        );
        serviceContainer.registerIfAbsent(
            IModListenerScanner.class,
            (Supplier<? extends IModListenerScanner>) () -> new ClassGraphListenerScanner(
                serviceContainer.require(IAsyncExecutionService.class),
                serviceContainer.require(IEventListenerRegistrar.class)
            )
        );
        serviceContainer.registerIfAbsent(
            IEventListenerRegistrar.class,
            (Supplier<? extends IEventListenerRegistrar>) () -> new EventListenerRegistrar(
                serviceContainer.require(IEventBusService.class)
            )
        );
        serviceContainer.registerIfAbsent(
            IRegistryLoader.class,
            (Supplier<? extends IRegistryLoader>) () -> new DefaultRegistryLoader(
                serviceContainer
            )
        );
        serviceContainer.registerIfAbsent(IModAssetLoader.class, (Supplier<? extends IModAssetLoader>) () ->
                new ModTextureModAssetLoader(
                    new CombinedTextureDiscoveryService(),
                    new DefaultTextureAtlasBuilder(AssetConfig.defaultConfig()),
                    PathResolver.getGameDataDir().resolve("assets").resolve("textures")
                )
        );
        serviceContainer.registerIfAbsent(IModInitializer.class, (Supplier<? extends IModInitializer>) DefaultModInitializer::new);

        // Add other game logic services as they're created
        // serviceContainer.registerIfAbsent(IGameEngine.class, GameEngine::new);
        // serviceContainer.registerIfAbsent(IPlayerManager.class, PlayerManager::new);
    }

    @Override
    public String getModuleName() {
        return "ModLoadingServices";
    }
}
