package io.github.hato1883.core.modloading.assets.textures;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.Identifier;

import java.util.*;
import java.util.function.Consumer;

public class LazyMergedAtlasManager {

    private final AssetManager assetManager;
    private final Map<Integer, Map<Identifier, TextureRegion>> lodCache = new HashMap<>();
    private final Map<Integer, List<Consumer<TextureAtlas>>> atlasListeners = new HashMap<>();

    public LazyMergedAtlasManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Enqueue all atlases by LOD to AssetManager.
     */
    public void enqueueAtlases(List<Integer> lods) {
        for (int lod : lods) {
            String atlasPath = getAtlasPath(lod); // e.g., "tiles_lod0.atlas"
            TextureAtlas.AtlasRegion dummy = null; // parameter only
            AssetLoaderParameters<TextureAtlas> params = new AssetLoaderParameters<TextureAtlas>();
            params.loadedCallback = (AssetManager manager, String fileName, Class type) -> {
                onAtlasLoaded(lod);
            };
            assetManager.load(atlasPath, TextureAtlas.class, params);
        }
    }

    private void onAtlasLoaded(int lod) {
        TextureAtlas atlas = assetManager.get(getAtlasPath(lod), TextureAtlas.class);
        Map<Identifier, TextureRegion> lodMap = new HashMap<>();

        for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
            lodMap.put(Identifier.of(region.name), region);
        }

        lodCache.put(lod, lodMap);

        // Notify all listeners waiting for this LOD
        List<Consumer<TextureAtlas>> listeners = atlasListeners.remove(lod);
        if (listeners != null) {
            for (Consumer<TextureAtlas> cb : listeners) cb.accept(null);
        }
    }

    public TextureRegion getRegion(int lod, Identifier id) {
        Map<Identifier, TextureRegion> map = lodCache.get(lod);
        if (map != null) return map.get(id);
        return null;
    }

    public void registerListener(int lod, Consumer<TextureAtlas> callback) {
        atlasListeners.computeIfAbsent(lod, k -> new ArrayList<>()).add(callback);
    }

    private String getAtlasPath(int lod) {
        return "tiles_lod" + lod + ".atlas";
    }
}
