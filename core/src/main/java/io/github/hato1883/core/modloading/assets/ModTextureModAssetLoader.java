package io.github.hato1883.core.modloading.assets;

import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.mod.load.ILoadedMod;
import io.github.hato1883.api.mod.load.asset.*;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class ModTextureModAssetLoader implements IModAssetLoader {

    private static final Logger LOGGER = LogManager.getLogger("ModTextureAssetLoader");

    private final TextureDiscoveryService discovery;
    private final TextureAtlasBuilder atlasBuilder;
    private final Path cacheDir;

    public ModTextureModAssetLoader(TextureDiscoveryService discovery,
                                    TextureAtlasBuilder atlasBuilder,
                                    Path cacheDir) {
        this.discovery = discovery;
        this.atlasBuilder = atlasBuilder;
        this.cacheDir = cacheDir;
    }

    record AtlasBuildResult(ILoadedMod mod, AssetCategory category, int lod,
                            Path atlasFile, Path atlasDir, List<TextureEntry> textures) {}

    @Override
    public void loadAssets(List<ILoadedMod> mods) throws IOException {
        Map<AssetCategory, Map<Integer, List<AtlasBuildResult>>> combinedGroups = new HashMap<>();

        var discovered = discovery.discover(mods); // now Map<ILoadedMod, DiscoveredModTextures>
        LOGGER.info("Loading assets for: {}", discovered.keySet());

        for (var modEntry : discovered.entrySet()) {
            ILoadedMod mod = modEntry.getKey();
            Map<AssetCategory, Map<Integer, List<TextureEntry>>> categories = modEntry.getValue();
            Path modCache = cacheDir.resolve(mod.id());

            try {
                for (var catEntry : categories.entrySet()) {
                    AssetCategory category = catEntry.getKey();
                    for (var lodEntry : catEntry.getValue().entrySet()) {
                        int lod = lodEntry.getKey();
                        var textures = lodEntry.getValue();
                        Path lodAssetsDir = modCache.resolve(String.format("%s/lod%d", category.getCategory(), lod));

                        try {
                            atlasBuilder.ensureAtlas(mod, category, lod, textures, lodAssetsDir);
                        } catch (Exception e) {
                            LOGGER.info("A Problem occurred when loading textures for mod={} category={} lod={}", mod.id(), category, lod);
                            throw new RuntimeException("Texture load Error", e);
                        }

                        Path atlasPath = lodAssetsDir.resolve(String.format("%s_%s_lod%d.atlas", mod.id(), category.getCategory(), lod));
                        combinedGroups
                            .computeIfAbsent(category, k -> new HashMap<>())
                            .computeIfAbsent(lod, k -> new ArrayList<>())
                            .add(new AtlasBuildResult(mod, category, lod, atlasPath, lodAssetsDir, textures));

                    }
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }

        // Merge per category/lod as before...
        for (var catEntry : combinedGroups.entrySet()) {
            AssetCategory category = catEntry.getKey();
            for (var lodEntry : catEntry.getValue().entrySet()) {
                mergeAtlases(category, lodEntry.getKey(), lodEntry.getValue());
            }
        }
    }


    /**
     * Merges multiple per-mod atlases into one combined atlas for the category/lod.
     */
    private void mergeAtlases(AssetCategory category, int lod, List<AtlasBuildResult> sources) throws IOException {
        if (sources.isEmpty()) return;

        Path combinedDir = cacheDir.resolve("combined")
            .resolve(category.getCategory())
            .resolve("lod" + lod);
        Files.createDirectories(combinedDir);

        Path combinedAtlas = combinedDir.resolve("combined_" + category.getCategory() + "_lod" + lod + ".atlas");

        try (BufferedWriter writer = Files.newBufferedWriter(combinedAtlas)) {
            int pageCounter = 0;
            for (AtlasBuildResult src : sources) {
                List<String> lines = Files.readAllLines(src.atlasFile);

                for (String line : lines) {
                    if (line.endsWith(".png")) {
                        // Page reference line
                        String newPageName = String.format("page_%d.png", pageCounter++);
                        Path srcPng = src.atlasDir.resolve(line);
                        Files.copy(srcPng, combinedDir.resolve(newPageName), StandardCopyOption.REPLACE_EXISTING);
                        writer.write(newPageName);
                        writer.newLine();
                    } else {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        }

        // Save combined cache info
        Map<String, String> allMods = new HashMap<>();
        List<TextureEntry> allTextures = new ArrayList<>();
        for (AtlasBuildResult src : sources) {
            allMods.put(src.mod.id(), src.mod.metadata().version());
            allTextures.addAll(src.textures);
        }

        AtlasCacheInfo.save(
            combinedDir.resolve("combined_" + category.getCategory() + "_lod" + lod + ".cache.json"),
            allMods,
            allTextures,
            category.getCategory(),
            lod
        );
    }
}

