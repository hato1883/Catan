package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.load.asset.IModAssetLoader;
import io.github.hato1883.api.mod.load.asset.AssetCategory;
import io.github.hato1883.api.mod.load.asset.TextureEntry;
import io.github.hato1883.api.mod.load.ILoadedMod;
import org.slf4j.Logger;
import io.github.hato1883.api.LogManager;
import java.nio.file.Path;
import java.util.*;

/**
 * Step 5: Gather and load all assets (textures, etc.) into the asset system.
 */
public class DefaultModAssetLoadingStep implements ModLoadingStep {
    private static final Logger LOGGER = LogManager.getLogger("ModLoading");
    private final IModAssetLoader modAssetLoader;
    private final Path atlasDir;
    private final String baseName;

    public DefaultModAssetLoadingStep(IModAssetLoader modAssetLoader, Path atlasDir, String baseName) {
        this.modAssetLoader = modAssetLoader;
        this.atlasDir = atlasDir;
        this.baseName = baseName;
    }

    @Override
    public void execute(ModLoadingContext context) throws Exception {
        List<ILoadedMod> loadedMods = context.getLoadedMods();
        List<List<TextureEntry>> allSources = new ArrayList<>();
        for (ILoadedMod loadedMod : loadedMods) {
            Map<ILoadedMod, Map<AssetCategory, Map<Integer, List<TextureEntry>>>> discovered = modAssetLoader.getDiscovery().discover(List.of(loadedMod));
            for (Map<AssetCategory, Map<Integer, List<TextureEntry>>> catMap : discovered.values()) {
                for (Map<Integer, List<TextureEntry>> lodMap : catMap.values()) {
                    allSources.addAll(lodMap.values());
                }
            }
        }
        context.setAllTextureSources(allSources);
        modAssetLoader.loadAssetsWithPrecedence(allSources, atlasDir, baseName);
        LOGGER.info("Asset loading complete. Atlases built at {}.", atlasDir);
    }
}
