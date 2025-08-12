package io.github.hato1883.modloader;

import io.github.hato1883.api.modding.CatanMod;

import java.nio.file.Path;

/**
 * Simple value holder that couples {@link ModMetadata} with its loaded
 * {@link CatanMod} instance. Kept intentionally tiny: this class only
 * exposes convenience methods required by the loader.
 *
 * <p>See {@link ModLoader} for lifecycle usage.
 */
public record LoadedMod(Path path, ModMetadata metadata, CatanMod instance) {
    /**
     * Convenience accessor for the mod id.
     */
    public String id() {
        return metadata.id();
    }

    /**
     * Convenience accessor for the mod main class.
     */
    public String mainClass() {
        return metadata.mainClass();
    }
}

