package io.github.hato1883.api.mod.load.asset;

import io.github.hato1883.api.mod.load.ILoadedMod;

import java.nio.file.Path;
import java.util.List;

/**
 * Responsible for building (or reusing) atlas files for a given mod/category/lod.
 * Implementation encapsulates caching checks and the actual TexturePacker invocation.
 */
public interface TextureAtlasBuilder {
    void ensureAtlas(ILoadedMod mod, AssetCategory category, int lod, List<TextureEntry> textures, Path modCacheDir) throws Exception;
}
