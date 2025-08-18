package io.github.hato1883.api.mod.load.dependency;

/**
 * Represents a dependency relationship between mods
 */
public record ModDependency(
    String modId,
    VersionConstraint versionConstraint,
    boolean optional
) {
    public ModDependency {
        if (modId == null || modId.isBlank()) {
            throw new IllegalArgumentException("Dependency mod id cannot be null or blank");
        }
        versionConstraint = versionConstraint != null ? versionConstraint : VersionConstraint.any();
    }

    public static ModDependency required(String modId, VersionConstraint version) {
        return new ModDependency(modId, version, false);
    }

    public static ModDependency optional(String modId, VersionConstraint version) {
        return new ModDependency(modId, version, true);
    }
}
