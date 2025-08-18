package io.github.hato1883.api.mod.load;

import io.github.hato1883.api.mod.load.dependency.ModDependency;

import java.util.List;

/**
 * Immutable metadata record representing a mod's configuration.
 * This is a pure data class with no logic - only data and validation.
 */
public record ModMetadata(
    String id,
    String name,
    String version,
    String entrypoint,
    String description,
    List<ModDependency> dependencies,
    LoadPriority loadPriority
) {

    public ModMetadata {
        // Defensive validation - fail fast principle
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Mod id cannot be null or blank");
        }
        if (entrypoint == null || entrypoint.isBlank()) {
            throw new IllegalArgumentException("Mod entrypoint cannot be null or blank");
        }
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Mod version cannot be null or blank");
        }

        // Default values for optional fields
        name = name != null ? name : id;
        dependencies = dependencies != null ? List.copyOf(dependencies) : List.of();
        loadPriority = loadPriority != null ? loadPriority : LoadPriority.NORMAL;
    }

    /**
     * Builder pattern for flexible construction while maintaining immutability
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String version;
        private String entrypoint;
        private String description;
        private List<ModDependency> dependencies;
        private LoadPriority loadPriority = LoadPriority.NORMAL;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder version(String version) { this.version = version; return this; }
        public Builder entrypoint(String entrypoint) { this.entrypoint = entrypoint; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder dependencies(List<ModDependency> dependencies) { this.dependencies = dependencies; return this; }
        public Builder loadPriority(LoadPriority loadPriority) { this.loadPriority = loadPriority; return this; }

        public ModMetadata build() {
            return new ModMetadata(id, name, version, entrypoint, description, dependencies, loadPriority);
        }
    }
}

