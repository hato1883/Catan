package io.github.hato1883.api.mod.load.dependency;

import io.github.hato1883.api.mod.load.ModMetadata;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface IDependencyResolver {
    List<ModWithPath> resolveLoadOrder(Map<ModMetadata, Path> mods) throws ModDependencyException;
}
