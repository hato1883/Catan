package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.mod.load.ILoadedMod;
import io.github.hato1883.api.mod.load.ModMetadata;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Immutable container representing a loaded mod.
 * This class purposely exposes only simple accessors to avoid Law of Demeter violations.
 */
public record LoadedMod(Path path, ModMetadata metadata, CatanMod instance,
                        ClassLoader classLoader) implements ILoadedMod {

    public LoadedMod(Path path, ModMetadata metadata, CatanMod instance, ClassLoader classLoader) {
        this.path = Objects.requireNonNull(path);
        this.metadata = Objects.requireNonNull(metadata);
        this.instance = Objects.requireNonNull(instance);
        this.classLoader = Objects.requireNonNull(classLoader);
    }

    @Override
    public String id() {
        return metadata.id();
    }

    @Override
    public String version() {
        return metadata.version();
    }

    @Override
    public String mainClass() {
        return metadata.entrypoint();
    }

    @Override
    public String toString() {
        return "LoadedMod{" +
            "modid:" + metadata.id() +
            ", name:" + metadata.name() +
            '}';
    }
}
