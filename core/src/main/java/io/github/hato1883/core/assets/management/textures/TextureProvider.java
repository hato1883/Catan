package io.github.hato1883.core.assets.management.textures;

public interface TextureProvider<T extends TextureProvider<T>> {

    /**
     * Queries the texture provider for any possible texture upgrades.
     */
    void update(); // if needed
}
