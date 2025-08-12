package io.github.hato1883.modloader;

import io.github.hato1883.api.modding.CatanMod;
import io.github.hato1883.modloader.assets.ModAssetManager;
import io.github.hato1883.registries.RegistryLoader;
import io.github.hato1883.api.LogManager;
import io.github.hato1883.util.PathResolver;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static io.github.hato1883.Main.LOGGER_ID;

/**
 * Central entry point for mod lifecycle management.
 *
 * <p>Responsible for:
 * <ul>
 *   <li>Discovering mod JARs in {@code <gameData>/mods}</li>
 *   <li>Loading mod metadata and instances</li>
 *   <li>Delegating listener registration to {@link ModListenerLoader}</li>
 *   <li>Calling {@link CatanMod#onInitialize()} on each loaded mod</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 *     ModLoader loader = new ModLoader();
 *     loader.loadAll();
 * }</pre>
 *
 * @see PathResolver#getGameDataDir()
 * @see ModListenerLoader
 * @see CatanMod
 */
public class ModLoader {

    private static final Logger LOGGER = LogManager.getLogger(LOGGER_ID);

    /** Loaded mods (metadata + instance) populated during {@link #loadMods} which is called by {@link #loadAll()}. */
    private final List<LoadedMod> loadedMods = new java.util.concurrent.CopyOnWriteArrayList<>();

    /**
     * High-level orchestration method:
     * <ol>
     *     <li>{@link #discoverAndLoadMods()} — Locate & load mods</li>
     *     <li>{@link ModListenerLoader#scanAndRegisterAllMods(List)} — Register listeners</li>
     *     <li>{@link #initMods(List)} — Call {@code onInitialize()}</li>
     * </ol>
     *
     * @throws IOException if the mods directory cannot be created or accessed
     */
    public void loadAll() throws IOException {
        discoverAndLoadMods();
        ModListenerLoader.scanAndRegisterAllMods(loadedMods);
        RegistryLoader.loadRegistries(loadedMods);
        ModAssetManager.loadAllAssets(loadedMods);
        initMods(loadedMods);
    }

    // ─────────────────────────────────────────────────────────────
    //  Phase 1 — Discovery & Metadata Loading
    // ─────────────────────────────────────────────────────────────

    /**
     * Discovers mods in {@code <gameData>/mods} and loads them into {@link #loadedMods}.
     *
     * @throws IOException if the mods directory cannot be accessed or created
     */
    private void discoverAndLoadMods() throws IOException {
        Path modsDir = ensureModsDirectoryExists();
        List<Path> modJars = findModJars(modsDir);
        loadMods(modJars);
    }

    /**
     * Ensures the mods directory exists.
     */
    private Path ensureModsDirectoryExists() throws IOException {
        Path gameDataDir = PathResolver.getGameDataDir();
        Path modsDir = gameDataDir.resolve("mods");

        if (!Files.exists(modsDir)) {
            Files.createDirectories(modsDir);
        }
        return modsDir;
    }

    /**
     * Finds all mod JAR files in the mods directory.
     */
    private List<Path> findModJars(Path modsDir) {
        List<Path> jarPaths = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir)) {
            for (Path modPath : stream) {
                if (Files.isDirectory(modPath)) {
                    LOGGER.trace("Skipping folder in mods dir: {}", modPath);
                } else if (modPath.toString().endsWith(".jar")) {
                    jarPaths.add(modPath);
                } else {
                    LOGGER.warn("Skipping unrecognized file in mods dir: {}", modPath);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to read mods directory {}: {}", modsDir, e.getMessage(), e);
        }
        return jarPaths;
    }

    /**
     * Loads metadata and creates mod instances from JAR paths.
     */
    private void loadMods(List<Path> modJarPaths) {
        for (Path modPath : modJarPaths) {
            try {
                ModMetadata metadata = ModMetadataReader.read(modPath);
                Class<?> mainClass = Class.forName(metadata.mainClass(), true, getClass().getClassLoader());

                if (!CatanMod.class.isAssignableFrom(mainClass)) {
                    LOGGER.error("Main class {} for mod {} does not implement CatanMod",
                        metadata.mainClass(), metadata.id());
                    continue;
                }

                CatanMod modInstance = (CatanMod) mainClass.getDeclaredConstructor().newInstance();
                loadedMods.add(new LoadedMod(modPath, metadata, modInstance));

                LOGGER.info("Loaded mod {} v{}", metadata.id(), metadata.version());

            } catch (Exception e) {
                LOGGER.error("Failed to load mod from {}: {}", modPath, e.getMessage(), e);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  Phase 3 — Initialization
    // ─────────────────────────────────────────────────────────────

    /**
     * Initializes each loaded mod by calling {@link CatanMod#onInitialize()}.
     *
     * <p>Errors during mod initialization are logged and do not stop other mods
     * from initializing.
     */
    private void initMods(List<LoadedMod> loadedMods) {
        for (LoadedMod loadedMod : loadedMods) {
            try {
                LOGGER.info("Initializing mod {}", loadedMod.metadata().id());
                loadedMod.instance().onInitialize();
            } catch (Exception e) {
                LOGGER.error("Error initializing mod {}: {}", loadedMod.metadata().id(), e.getMessage(), e);
            }
        }
    }
    /* Test / utility accessors (if needed in the future, keep encapsulated) */

    /** Returns an unmodifiable view of loaded mods for inspection/tests. */
    public List<LoadedMod> getLoadedMods() {
        return java.util.Collections.unmodifiableList(loadedMods);
    }
}
