package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.Services;
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
    public void registerServices(IServiceContainer registrar) {
        registrar.registerIfAbsent(IModDiscovery.class, (Supplier<? extends IModDiscovery>) FilesystemModDiscovery::new);
        registrar.registerIfAbsent(IDependencyResolver.class, (Supplier<? extends IDependencyResolver>) DependencyResolver::new);
        registrar.registerIfAbsent(IModMetadataReader.class, (Supplier<? extends IModMetadataReader>) DefaultModMetadataReader::new);
        registrar.registerIfAbsent(IModClassLoaderFactory.class, (Supplier<? extends IModClassLoaderFactory>) () -> new UrlModClassLoaderFactory(ModLoader.class.getClassLoader()));
        registrar.registerIfAbsent(IModListenerScanner.class, (Supplier<? extends IModListenerScanner>) () -> new ClassGraphListenerScanner(Services.require(IAsyncExecutionService.class)));
        registrar.registerIfAbsent(IEventListenerRegistrar.class, (Supplier<? extends IEventListenerRegistrar>) EventListenerRegistrar::new);
        registrar.registerIfAbsent(IRegistryLoader.class, (Supplier<? extends IRegistryLoader>) DefaultRegistryLoader::new);
        registrar.registerIfAbsent(IModAssetLoader.class, (Supplier<? extends IModAssetLoader>) () -> {
                return new ModTextureModAssetLoader(
                    new CombinedTextureDiscoveryService(),
                    new DefaultTextureAtlasBuilder(AssetConfig.defaultConfig()),
                    PathResolver.getGameDataDir().resolve("assets").resolve("textures")
                );
            }
        );
        registrar.registerIfAbsent(IModInitializer.class, (Supplier<? extends IModInitializer>) DefaultModInitializer::new);

        // Add other game logic services as they're created
        // registrar.registerIfAbsent(IGameEngine.class, GameEngine::new);
        // registrar.registerIfAbsent(IPlayerManager.class, PlayerManager::new);
    }

    @Override
    public String getModuleName() {
        return "ModLoadingServices";
    }
}
