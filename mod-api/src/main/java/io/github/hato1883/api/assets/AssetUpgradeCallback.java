package io.github.hato1883.api.assets;

import io.github.hato1883.api.Identifier;

@FunctionalInterface
public interface AssetUpgradeCallback<T> {
    void onUpgrade(T asset, Identifier id, int lod);
}
