package io.github.hato1883.api.mod.load;

import io.github.hato1883.api.mod.CatanMod;

import java.nio.file.Path;

public interface ILoadedMod {
    Path path();

    ModMetadata metadata();

    String id();

    String version();

    String mainClass();

    CatanMod instance();

    ClassLoader classLoader();

    @Override
    String toString();
}
