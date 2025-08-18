package io.github.hato1883.api.mod.load.dependency;

import io.github.hato1883.api.mod.load.ModMetadata;

import java.nio.file.Path;
import java.util.Map;

public interface IDependencyResolver {
    Map<ModMetadata, Path> resolveLoadOrder(Map<ModMetadata, Path> mods) throws ModDependencyException;
}
