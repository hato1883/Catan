package io.github.hato1883.core.service;

import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.asset.*;
import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.service.IServiceModule;
import io.github.hato1883.api.service.IServiceRegistrar;
import io.github.hato1883.api.unknown.IAsyncExecutionService;
import io.github.hato1883.core.events.EventListenerRegistrar;
import io.github.hato1883.core.events.listeners.ClassGraphListenerScanner;
import io.github.hato1883.core.modloading.*;
import io.github.hato1883.core.modloading.assets.ModTextureModAssetLoader;
import io.github.hato1883.core.modloading.assets.textures.CombinedTextureDiscoveryService;
import io.github.hato1883.core.modloading.assets.textures.DefaultTextureAtlasBuilder;
import io.github.hato1883.core.modloading.dependency.DependencyResolver;
import io.github.hato1883.core.util.PathResolver;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

public class ModLoadingServicesModule implements IServiceModule {

    @Override
    public void registerServices(IServiceRegistrar registrar) {
        registrar.registerIfAbsent(IModDiscovery.class, (Supplier<? extends IModDiscovery>) FilesystemModDiscovery::new);
        registrar.registerIfAbsent(IDependencyResolver.class, (Supplier<? extends IDependencyResolver>) DependencyResolver::new);
        registrar.registerIfAbsent(IModMetadataReader.class, (Supplier<? extends IModMetadataReader>) DefaultModMetadataReader::new);
        registrar.registerIfAbsent(IModClassLoaderFactory.class, (Supplier<? extends IModClassLoaderFactory>) () -> new UrlModClassLoaderFactory(ModLoadingServicesModule.class.getClassLoader()));
        registrar.registerIfAbsent(IModListenerScanner.class, (Supplier<? extends IModListenerScanner>) () -> new ClassGraphListenerScanner(ServiceBootstrap.getProvider().requireService(IAsyncExecutionService.class)));
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
        // registrar.register(IGameEngine.class, GameEngine::new);
        // registrar.register(IPlayerManager.class, PlayerManager::new);
    }

    @Override
    public String getModuleName() {
        return "ModLoadingServices";
    }
}
