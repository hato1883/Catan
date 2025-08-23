package io.github.hato1883.api.mod.load.dependency;

import io.github.hato1883.api.mod.load.ModMetadata;
import java.nio.file.Path;

public record ModWithPath(ModMetadata metadata, Path path) {}

