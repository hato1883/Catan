package io.github.hato1883.api.assets;

import io.github.hato1883.api.Identifier;

public interface TextureUpgradeCallback<T> {
    void onUpgrade(T provider, Identifier id, int lod);
}

