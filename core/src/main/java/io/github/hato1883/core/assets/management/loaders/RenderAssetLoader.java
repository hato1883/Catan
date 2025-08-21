package io.github.hato1883.core.assets.management.loaders;

import io.github.hato1883.api.assets.TextureUpgradeNotifier;

/**
 * ISP/DIP: renderer asks for high-level factories/providers instead of concrete libs.
 */
public interface RenderAssetLoader {
    void queueAssets();
    boolean update();                 // returns true when finished
    LoadedAssets getAssets();         // stable bundle/DTO
    void renderLoading();
    void dispose();
    TextureUpgradeNotifier getTextureUpgradeNotifier();
}
