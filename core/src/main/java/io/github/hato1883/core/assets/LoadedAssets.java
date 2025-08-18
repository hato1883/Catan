package io.github.hato1883.core.assets;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.hato1883.core.assets.textures.TileTextureProvider;

public record LoadedAssets(
    TileTextureProvider tileTextures,
    BitmapFont numberTokenFont
) {}
