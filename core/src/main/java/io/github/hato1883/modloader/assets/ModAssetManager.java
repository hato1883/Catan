package io.github.hato1883.modloader.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.IntMap;
import io.github.hato1883.modloader.LoadedMod;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages discovery, packing, and retrieval of textures from mods.
 * Supports multiple LODs and categories.
 */
public class ModAssetManager {

    private static final ModTextureAssetManager TEXTURE_ASSET_MANAGER = new ModTextureAssetManager(AssetConfig.defaultConfig());

    private  ModAssetManager() {}

    public static ModTextureAssetManager getTextureAssetManager() {
        return TEXTURE_ASSET_MANAGER;
    }

    public static void loadAllAssets(List<LoadedMod> mods) throws IOException {
        TEXTURE_ASSET_MANAGER.loadMods(mods);
    }
}
