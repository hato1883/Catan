package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.asset.IModAssetLoader;
import io.github.hato1883.api.mod.load.dependency.ModWithPath;
import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.mod.load.dependency.ModDependency;
import io.github.hato1883.api.mod.load.dependency.ModDependencyException;
import io.github.hato1883.api.services.IServiceLocator;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static io.github.hato1883.api.mod.load.dependency.ModDependencyException.logDependencyException;


public class ModLoader {

    private static final Logger LOGGER = LogManager.getLogger("ModLoading");

    private final IModDiscovery discovery;
    private final IDependencyResolver dependencyResolver;
    private final IModMetadataReader metadataReader;
    private final IModClassLoaderFactory classLoaderFactory;
    private final IModListenerScanner listenerScanner;
    private final IRegistryLoader registryLoader;
    private final IModAssetLoader modAssetLoader;
    private final IModInitializer initializer;

    /**
     * Production factory: uses ModLoading facade for all dependencies.
     */
    public static ModLoader createDefault(IServiceLocator serviceLocator) {
        return new ModLoader(
            serviceLocator.require(IModDiscovery.class),
            serviceLocator.require(IDependencyResolver.class),
            serviceLocator.require(IModMetadataReader.class),
            serviceLocator.require(IModClassLoaderFactory.class),
            serviceLocator.require(IModListenerScanner.class),
            serviceLocator.require(IRegistryLoader.class),
            serviceLocator.require(IModAssetLoader.class),
            serviceLocator.require(IModInitializer.class)
        );
    }

    /**
     * Dependency-injection constructor for testability.
     */
    public ModLoader(
        IModDiscovery discovery,
        IDependencyResolver dependencyResolver,
        IModMetadataReader metadataReader,
        IModClassLoaderFactory classLoaderFactory,
        IModListenerScanner listenerScanner,
        IRegistryLoader registryLoader,
        IModAssetLoader modAssetLoader,
        IModInitializer initializer
    ) {
        this.discovery = discovery;
        this.dependencyResolver = dependencyResolver;
        this.metadataReader = metadataReader;
        this.classLoaderFactory = classLoaderFactory;
        this.listenerScanner = listenerScanner;
        this.registryLoader = registryLoader;
        this.modAssetLoader = modAssetLoader;
        this.initializer = initializer;
    }

    /**
     * Full lifecycle: discover -> load -> scan listeners -> register content -> load assets -> initialize
     */
    public List<ILoadedMod> loadAll(Path modsDir) throws IOException {
        LOGGER.info("Finding all mods...");
        List<Path> modPaths = discovery.discoverMods(modsDir);

        LOGGER.info("Loading mod metadata...");
        Map<ModMetadata, Path> modData = readAllMetadata(modPaths);

        LOGGER.info("Fixing Mod Loading order....");
        List<ModWithPath> orderedMods;
        try {
            orderedMods = dependencyResolver.resolveLoadOrder(modData);
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

        LOGGER.info("Creating mod instances...");
        List<ILoadedMod> loaded = createModInstances(orderedMods);

        // Failure management for registry, assets, and initialization
        processWithFailureManagement(loaded, Step.REGISTRY);
        processWithFailureManagement(loaded, Step.ASSETS);
        processWithFailureManagement(loaded, Step.INITIALIZE);

        return List.copyOf(loaded);
    }

    private enum Step { REGISTRY, ASSETS, INITIALIZE }

    /**
     * Processes mods for a given step, removing failed mods and their dependents.
     */
    private void processWithFailureManagement(List<ILoadedMod> loaded, Step step) {
        boolean changed;
        do {
            changed = false;
            List<ILoadedMod> toRemove = new ArrayList<>();
            for (ILoadedMod mod : new ArrayList<>(loaded)) {
                try {
                    switch (step) {
                        case REGISTRY -> registryLoader.loadRegistries(List.of(mod));
                        case ASSETS -> modAssetLoader.loadAssets(List.of(mod));
                        case INITIALIZE -> initializer.initializeAll(List.of(mod));
                    }
                } catch (Exception e) {
                    LOGGER.error("Mod '{}' failed during {}: {}", mod.metadata().id(), step, e.getMessage(), e);
                    if (step == Step.REGISTRY) {
                        LOGGER.warn("No automatic registry cleanup available for mod '{}'. Manual cleanup may be required.", mod.metadata().id());
                    }
                    // Remove this mod and all dependents
                    Set<String> failedIds = new HashSet<>();
                    failedIds.add(mod.metadata().id());
                    findDependents(loaded, failedIds);
                    for (ILoadedMod m : new ArrayList<>(loaded)) {
                        if (failedIds.contains(m.metadata().id())) {
                            toRemove.add(m);
                        }
                    }
                    changed = true;
                }
            }
            loaded.removeAll(toRemove);
        } while (changed);
    }

    /**
     * Recursively finds all mods with a hard dependency on any id in failedIds and adds them to failedIds.
     */
    private void findDependents(List<ILoadedMod> loaded, Set<String> failedIds) {
        boolean found;
        do {
            found = false;
            for (ILoadedMod mod : loaded) {
                if (!failedIds.contains(mod.metadata().id())) {
                    for (ModDependency dep : mod.metadata().dependencies()) {
                        if (!dep.optional() && failedIds.contains(dep.modId())) {
                            if (failedIds.add(mod.metadata().id())) {
                                found = true;
                            }
                        }
                    }
                }
            }
        } while (found);
    }

    private Map<ModMetadata, Path> readAllMetadata(List<Path> modPaths) {
        Map<String, ModMetadata> highestVersionMeta = new HashMap<>();
        Map<String, Path> highestVersionPath = new HashMap<>();
        for (Path p : modPaths) {
            try {
                ModMetadata meta = metadataReader.readMetadata(p);
                String modid = meta.id();
                if (!highestVersionMeta.containsKey(modid)) {
                    highestVersionMeta.put(modid, meta);
                    highestVersionPath.put(modid, p);
                } else {
                    ModMetadata existing = highestVersionMeta.get(modid);
                    int cmp = compareSemanticVersion(meta.version(), existing.version());
                    if (cmp > 0) {
                        LOGGER.error("Duplicate modid '{}' found: version {} at {} is newer than version {} at {}. Keeping newer version.",
                            modid, meta.version(), p.getFileName(), existing.version(), highestVersionPath.get(modid).getFileName());
                        highestVersionMeta.put(modid, meta);
                        highestVersionPath.put(modid, p);
                    } else {
                        LOGGER.error("Duplicate modid '{}' found: version {} at {} is inferior to version {} at {}. Skipping inferior version.",
                            modid, meta.version(), p.getFileName(), existing.version(), highestVersionPath.get(modid).getFileName());
                    }
                }
                LOGGER.info("Found mod {} v{}", meta.id(), meta.version());
            } catch (Exception e) {
                LOGGER.error("Failed to read metadata from {}: {}", p.getFileName(), e.getMessage(), e);
            }
        }
        Map<ModMetadata, Path> metadataMap = new HashMap<>();
        for (String modid : highestVersionMeta.keySet()) {
            metadataMap.put(highestVersionMeta.get(modid), highestVersionPath.get(modid));
        }
        return metadataMap;
    }

    /**
     * Compares two version strings (semantic versioning, e.g. 1.2.3-alpha). Returns 1 if v1 > v2, -1 if v1 < v2, 0 if equal.
     * If the numeric parts (major.minor.patch) are equal, then compares the rest lexicographically (so 1.2.3 > 1.2.3-beta).
     */
    private int compareSemanticVersion(String v1, String v2) {
        String[] a1 = v1.split("[.-]", 4); // split on . and -
        String[] a2 = v2.split("[.-]", 4);
        for (int i = 0; i < 3; i++) {
            int n1 = i < a1.length ? parseIntSafe(a1[i]) : 0;
            int n2 = i < a2.length ? parseIntSafe(a2[i]) : 0;
            if (n1 != n2) return Integer.compare(n1, n2);
        }
        // If numeric parts are equal, compare the rest (alpha, beta, etc)
        if (a1.length == a2.length && a1.length > 3) {
            return a1[3].compareToIgnoreCase(a2[3]);
        } else if (a1.length > 3) {
            // v1 has a suffix, v2 does not: treat v2 as newer (release > pre-release)
            return -1;
        } else if (a2.length > 3) {
            // v2 has a suffix, v1 does not: treat v1 as newer
            return 1;
        }
        return 0;
    }
    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
    }

    private List<ILoadedMod> createModInstances(List<ModWithPath> mods) {
        List<ILoadedMod> loaded = new ArrayList<>(mods.size());
        Set<String> failedIds = new HashSet<>();
        for (ModWithPath mod : mods) {
            ModMetadata meta = mod.metadata();
            Path modPath = mod.path();
            boolean hasFailedDep = false;
            for (ModDependency dep : meta.dependencies()) {
                if (!dep.optional() && failedIds.contains(dep.modId())) {
                    hasFailedDep = true;
                    break;
                }
            }
            if (hasFailedDep) {
                failedIds.add(meta.id());
                LOGGER.warn("Skipping mod '{}' due to failed dependency.", meta.id());
                continue;
            }
            try {
                ClassLoader cl = classLoaderFactory.createClassLoader(modPath);
                CatanMod instance = instantiateMod(meta.entrypoint(), cl);
                loaded.add(new LoadedMod(modPath, meta, instance, cl));
                LOGGER.info("Loaded mod {} v{}", meta.id(), meta.version());
            } catch (Exception e) {
                LOGGER.error("Failed to load mod '{}' from {}: {}", meta.id(), modPath.getFileName(), e.getMessage(), e);
                failedIds.add(meta.id());
            }
        }
        // Remove any loaded mod that is in failedIds (should not be needed, but for safety)
        loaded.removeIf(m -> failedIds.contains(m.metadata().id()));
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
