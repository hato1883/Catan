package io.github.hato1883.core.assets.management.textures;

public interface TextureProvider<T extends TextureProvider<T>> {

    /**
     * Register a callback that fires when a higher LOD texture is loaded
     * (so the renderer can rebuild sprites).
     */
    void onTextureUpgrade(TextureUpgradeCallback<T> callback);

    /**
     * Queries the texture provider for any possible texture upgrades.
     */
    void update(); // if needed
}
