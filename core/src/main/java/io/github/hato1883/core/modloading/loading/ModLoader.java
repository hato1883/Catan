package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.ModLoading;
import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.ModMetadata;
import io.github.hato1883.api.mod.load.dependency.ModDependencyException;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.hato1883.api.mod.load.dependency.ModDependencyException.logDependencyException;


public class ModLoader {

    private static final Logger LOGGER = LogManager.getLogger("ModLoading");

    public ModLoader() {}

    /**
     * Full lifecycle: discover -> load -> scan listeners -> register content -> load assets -> initialize
     */
    public List<ILoadedMod> loadAll(Path modsDir) throws IOException {
        LOGGER.info("Finding all mods...");
        List<Path> modPaths = ModLoading.discovery().discoverMods(modsDir);

        LOGGER.info("Loading mod metadata...");
        Map<ModMetadata, Path> modData = readAllMetadata(modPaths);

        LOGGER.info("Fixing Mod Loading order....");
        Map<ModMetadata, Path> loadOrder = Map.of();
        LOGGER.info("From: {}", modData);
        try {
            loadOrder = ModLoading.dependencyResolver().resolveLoadOrder(modData);
        } catch (Exception ex) {
            LOGGER.error("Mod loading failed due to dependency issues.");

            if (ex.getCause() != null) {
                LOGGER.error("Detailed technical info: {}", ex.getCause().getMessage());
            }

            if (ex instanceof ModDependencyException modDepException) {
                logDependencyException(modDepException);
            }

            LOGGER.error("You can find the full technical log in the game log files.");
            throw new ModLoadingException("Mod loading failed due to dependency issues.", ex);
        }
        LOGGER.info("to: {}", loadOrder);

        LOGGER.info("Creating mod instances...");
        List<ILoadedMod> loaded = createModInstances(loadOrder);

        LOGGER.info("Scanning and registering listeners...");
        ModLoading.listenerScanner().scanAndRegister(loaded);

        LOGGER.info("Registering mod content...");
        ModLoading.registryLoader().loadRegistries(loaded);

        LOGGER.info("Loading mod assets...");
        ModLoading.modAssetLoader().loadAssets(loaded);

        LOGGER.info("Initializing mods...");
        ModLoading.initializer().initializeAll(loaded);

        return List.copyOf(loaded);
    }

    private Map<ModMetadata, Path> readAllMetadata(List<Path> modPaths) {
        Map<ModMetadata, Path> metadataMap = new HashMap<>();
        for (Path p : modPaths) {
            try {
                ModMetadata meta = ModLoading.metadataReader().readMetadata(p);
                metadataMap.put(meta, p);
                LOGGER.info("Found mod {} v{}", meta.id(), meta.version());
            } catch (Exception e) {
                LOGGER.error("Failed to read metadata from {}: {}", p.getFileName(), e.getMessage(), e);
            }
        }
        return metadataMap;
    }

    private List<ILoadedMod> createModInstances(Map<ModMetadata, Path> mods) {
        List<ILoadedMod> loaded = new ArrayList<>();
        for (ModMetadata data : mods.keySet()) {
            Path modPath = mods.get(data);
            try {
                ClassLoader cl = ModLoading.classLoaderFactory().createClassLoader(modPath);
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
            throw new IllegalStateException("GameGUIMain class does not implement CatanMod: " + mainClass);
        }
        return (CatanMod) inst;
    }
}
