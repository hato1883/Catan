package io.github.hato1883.core.modloading;

import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.ModMetadata;
import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.mod.load.IModListenerScanner;
import io.github.hato1883.api.mod.load.asset.IModAssetLoader;
import io.github.hato1883.api.mod.load.dependency.ModDependencyException;
import io.github.hato1883.core.service.ServiceBootstrap;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.hato1883.api.mod.load.dependency.ModDependencyException.logDependencyException;


public class ModLoader {

    private static final Logger LOGGER = LogManager.getLogger("ModLoader");

    private IModDiscovery discovery;
    private IDependencyResolver dependencyResolver;
    private IModMetadataReader metadataReader;
    private IModClassLoaderFactory classLoaderFactory;
    private IModListenerScanner listenerScanner;
    private IEventListenerRegistrar listenerRegistrar;
    private IRegistryLoader registryLoader;
    private IModAssetLoader modAssetLoader;
    private IModInitializer initializer;

    public ModLoader() {}

    /**
     * Full lifecycle: discover -> load -> scan listeners -> register content -> load assets -> initialize
     */
    public List<ILoadedMod> loadAll(Path modsDir) throws IOException {
        LOGGER.info("Finding all mods...");
        if (discovery == null) {
            discovery = ServiceBootstrap.getProvider().requireService(IModDiscovery.class);
        }
        ServiceBootstrap.getProvider().requireService(IModDiscovery.class);
        List<Path> modPaths = discovery.discoverMods(modsDir);

        LOGGER.info("Loading mod metadata...");
        Map<ModMetadata, Path> modData = readAllMetadata(modPaths);

        LOGGER.info("Fixing Mod Loading order....");
        if (dependencyResolver == null) {
            dependencyResolver = ServiceBootstrap.getProvider().requireService(IDependencyResolver.class);
        }
        Map<ModMetadata, Path> loadOrder = Map.of();
        try {
            loadOrder = dependencyResolver.resolveLoadOrder(modData);
        } catch (Exception ex) {
            LOGGER.error("Mod loading failed due to dependency issues.");

            if (ex.getCause() != null) {
                LOGGER.error("Detailed technical info: {}", ex.getCause().getMessage());
            }

            if (ex instanceof ModDependencyException modDepException) {
                logDependencyException(modDepException);
            }

            LOGGER.error("You can find the full technical log in the game log files.");
            System.exit(1); // Fail fast
        }

        LOGGER.info("Creating mod instances...");
        List<ILoadedMod> loaded = createModInstances(loadOrder);

        LOGGER.info("Scanning and registering listeners...");
        if (listenerScanner == null) {
            listenerScanner = ServiceBootstrap.getProvider().requireService(IModListenerScanner.class);
        }
        if (listenerRegistrar == null) {
            listenerRegistrar = ServiceBootstrap.getProvider().requireService(IEventListenerRegistrar.class);
        }
        listenerScanner.scanAndRegister(loaded, listenerRegistrar);

        LOGGER.info("Registering mod content...");
        if (registryLoader == null) {
            registryLoader = ServiceBootstrap.getProvider().requireService(IRegistryLoader.class);
        }
        registryLoader.loadRegistries(loaded);

        LOGGER.info("Loading mod assets...");
        if (modAssetLoader == null) {
            modAssetLoader = ServiceBootstrap.getProvider().requireService(IModAssetLoader.class);
        }
        modAssetLoader.loadAssets(loaded);

        LOGGER.info("Initializing mods...");
        if (initializer == null) {
            initializer = ServiceBootstrap.getProvider().requireService(IModInitializer.class);
        }
        initializer.initializeAll(loaded);

        return List.copyOf(loaded);
    }

    private Map<ModMetadata, Path> readAllMetadata(List<Path> modPaths) {
        if (metadataReader == null) {
            metadataReader = ServiceBootstrap.getProvider().requireService(IModMetadataReader.class);
        }
        Map<ModMetadata, Path> metadataMap = new HashMap<>();
        for (Path p : modPaths) {
            try {
                ModMetadata meta = metadataReader.readMetadata(p);
                metadataMap.put(meta, p);
                LOGGER.info("Found mod {} v{}", meta.id(), meta.version());
            } catch (Exception e) {
                LOGGER.error("Failed to read metadata from {}: {}", p.getFileName(), e.getMessage(), e);
            }
        }
        return metadataMap;
    }

    private List<ILoadedMod> createModInstances(Map<ModMetadata, Path> mods) {
        if (classLoaderFactory == null) {
            classLoaderFactory = ServiceBootstrap.getProvider().requireService(IModClassLoaderFactory.class);
        }
        List<ILoadedMod> loaded = new ArrayList<>();
        for (ModMetadata data : mods.keySet()) {
            Path modPath = mods.get(data);
            try {
                ClassLoader cl = classLoaderFactory.createClassLoader(modPath);
                CatanMod instance = instantiateMod(data.entrypoint(), cl);
                loaded.add(new LoadedMod(modPath, data, instance, cl));
                LOGGER.info("Loaded mod {} v{}", data.id(), data.version());
            } catch (Exception e) {
                LOGGER.error("Failed to load mod from {}: {}", modPath.getFileName(), e.getMessage(), e);
            }
        }
        return loaded;
    }

    private CatanMod instantiateMod(String mainClass, ClassLoader cl) throws ReflectiveOperationException {
        Class<?> clazz = Class.forName(mainClass, true, cl);
        Object inst = clazz.getDeclaredConstructor().newInstance();
        if (!(inst instanceof CatanMod)) {
            throw new IllegalStateException("Main class does not implement CatanMod: " + mainClass);
        }
        return (CatanMod) inst;
    }

    public IModAssetLoader getAssetLoader() {
        return modAssetLoader;
    }
}
