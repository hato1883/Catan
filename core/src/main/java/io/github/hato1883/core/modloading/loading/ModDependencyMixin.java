package io.github.hato1883.core.modloading.loading;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;

/**
 * Jackson Mixin for ModDependency to map 'id' and 'version' fields from JSON.
 */
public abstract class ModDependencyMixin {
    @JsonCreator
    public ModDependencyMixin(
        @JsonProperty("id") String modId,
        @JsonProperty("version") VersionConstraint versionConstraint,
        @JsonProperty("optional") Boolean optional
    ) {}
}

