package io.github.hato1883.modloader.dependency;

public record ModDependency(String modId, VersionConstraint constraint, boolean optional) {}
