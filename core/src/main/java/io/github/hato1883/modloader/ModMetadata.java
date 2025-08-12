package io.github.hato1883.modloader;

import io.github.hato1883.modloader.dependency.LoadPriority;
import io.github.hato1883.modloader.dependency.ModDependency;

import java.util.List;

/**
 * Immutable representation of mod metadata (the object produced by parsing
 * {@code catan.mod.json}).
 *
 * <p>Fields:
 * <ul>
 *   <li>{@code id} — unique mod identifier (required)</li>
 *   <li>{@code name} — human-readable name (optional)</li>
 *   <li>{@code version} — mod version string (optional)</li>
 *   <li>{@code mainClass} — fully-qualified entrypoint class (required)</li>
 *   <li>{@code dependencies} — list of mod IDs this mod depends on (optional, defaults to empty list)</li>
 *   <li>{@code loadPriority} — load priority for the mod (optional, defaults to NORMAL)</li>
 * </ul>
 *
 * <h3>Example {@code catan.mod.json}</h3>
 * <pre>{@code
 * {
 *   "id": "examplemod",
 *   "name": "Example Mod",
 *   "version": "1.2.3",
 *   "description": "Adds example features",
 *   "loadPriority": "HIGH",
 *   "dependencies": [
 *     { "modId": "basegame", "version": "^1.2.0", "optional": false },
 *     { "modId": "coollib", "version": "~2.5.1", "optional": true }
 *   ]
 * }
 * }</pre>
 *
 * <h3>See also</h3>
 * <ul>
 *   <li>{@link ModMetadataReader}</li>
 *   <li>{@link ModLoader#loadAll()}</li>
 * </ul>
 */
public record ModMetadata(
    String id,
    String name,
    String version,
    String mainClass,
    List<ModDependency> dependencies,
    LoadPriority loadPriority
) {
    public ModMetadata {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Mod ID cannot be null or blank");
        if (mainClass == null || mainClass.isBlank())
            throw new IllegalArgumentException("Main class cannot be null or blank");

        if (dependencies == null)
            dependencies = List.of();
        else {
            for (ModDependency dep : dependencies) {
                if (dep == null || dep.modId() == null || dep.modId().isBlank()) {
                    throw new IllegalArgumentException("Dependency IDs cannot be null or blank");
                }
            }
        }

        if (loadPriority == null)
            loadPriority = LoadPriority.NORMAL;
    }
}
