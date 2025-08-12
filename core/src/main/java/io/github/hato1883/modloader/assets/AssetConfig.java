package io.github.hato1883.modloader.assets;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import java.util.Arrays;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_MAX_TEXTURE_SIZE;

/**
 * Configuration for the asset pipeline: LOD sizes, atlas page size,
 * padding, and mipmap / texture filter options.
 *
 * @param lodSizes      LOD sizes in pixels (e.g. [64,128,256,512]). Order matters for nearest-Lod heuristics.
 * @param atlasPageSize Max atlas page width/height (square). Keep <= device max texture size (e.g. 4096).
 * @param padding       Padding between packed regions in pixels.
 * @param useMipMaps    Whether to generate mipmaps for atlas textures.
 * @param minFilter     Texture filters used when generating atlas textures.
 */
public record AssetConfig(List<Integer> lodSizes, int atlasPageSize, int padding, boolean useMipMaps,
                          TextureFilter minFilter, TextureFilter magFilter) {

    public static AssetConfig defaultConfig() {
        return new AssetConfig(Arrays.asList(64, 128, 256, 512), Math.min(4096, GL_MAX_TEXTURE_SIZE), 4,
            true, TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
    }
}
