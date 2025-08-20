package io.github.hato1883.core.assets.management.textures;

import io.github.hato1883.api.Identifier;

public interface TextureUpgradeCallback<T extends TextureProvider<?>> {
    void onUpgrade(T provider, Identifier id, int lod);
}

