package io.github.hato1883.core.modloading.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.github.hato1883.api.mod.load.asset.AssetCategory;

import java.util.Map;

public class LODTextureLoader {

    private final AssetManager assetManager;
    private final Map<AssetCategory, Map<Integer, String>> atlasPaths; // category -> lod -> path

    public LODTextureLoader(AssetManager assetManager,
                            Map<AssetCategory, Map<Integer, String>> atlasPaths) {
        this.assetManager = assetManager;
        this.atlasPaths = atlasPaths;
    }

    /** Queue atlases with LOD-priority: lowest resolution first (highest LOD number) */
    public void enqueueAtlases() {
        for (var catEntry : atlasPaths.entrySet()) {
            AssetCategory category = catEntry.getKey();
            Map<Integer, String> lodMap = catEntry.getValue();

            // Sort LODs descending: highest LOD number first (lowest resolution)
            lodMap.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getKey(), e1.getKey()))
                .forEachOrdered(entry -> {
                    int lod = entry.getKey();
                    String path = entry.getValue();
                    assetManager.load(path, TextureAtlas.class); // order in queue determines load
                });
        }
    }

    /** Returns true if all assets are loaded */
    public boolean update() {
        return assetManager.update();
    }
}
