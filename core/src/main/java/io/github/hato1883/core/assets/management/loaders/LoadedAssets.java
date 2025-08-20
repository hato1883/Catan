package io.github.hato1883.core.assets.management.loaders;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.hato1883.core.assets.management.textures.TileTextureProvider;

public record LoadedAssets(
    TileTextureProvider tileTextures,
    BitmapFont numberTokenFont
) {}
