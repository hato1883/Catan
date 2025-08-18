package io.github.hato1883.core.assets.textures;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.Identifier;

public interface TileTextureProvider extends TextureProvider<TileTextureProvider> {
    /**
     * @param lod        requested level of detail
     * @param tileTypeId logical tile type identifier
     * @return a texture region, possibly a lower-res fallback
     */
    TextureRegion getTileTexture(int lod, Identifier tileTypeId);
}

