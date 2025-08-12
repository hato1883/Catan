package io.github.hato1883.modloader.assets;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Represents a discovered texture file, backed either by exploded FileHandle or
 * inside a mod JAR. Holds metadata: modId, category, texture name (without extension),
 * and lod level (0 if unspecified).
 */
public final class ModelEntry {
    public final String modId;
    public final String category;
    public final String name; // without extension
    public final int lod;     // 0 if unspecified

    // Exactly one backing source is non-null:
    public final FileHandle explodedFile;  // preferred if mod is exploded
    public final Path jarPath;             // path to jar file if mod packed
    public final String jarEntryName;      // full path inside jar, e.g. assets/modid/textures/tiles/lod256/castle.png

    private ModelEntry(String modId, String category, String name, int lod,
                       FileHandle explodedFile, Path jarPath, String jarEntryName) {
        this.modId = modId;
        this.category = category;
        this.name = name;
        this.lod = lod;
        this.explodedFile = explodedFile;
        this.jarPath = jarPath;
        this.jarEntryName = jarEntryName;
    }

    /** Create a TextureEntry for exploded directory file */
    public static ModelEntry fromFileHandle(String modId, String category, String name, int lod, FileHandle file) {
        return new ModelEntry(modId, category, name, lod, file, null, null);
    }

    /** Create a TextureEntry for a file inside a jar */
    public static ModelEntry fromJarEntry(String modId, String category, String name, int lod, Path jarPath, String jarEntryName) {
        return new ModelEntry(modId, category, name, lod, null, jarPath, jarEntryName);
    }

    /**
     * Create a Pixmap from the backing file.
     * Uses FileHandle constructor if exploded,
     * or reads bytes from JAR entry and builds Pixmap from bytes.
     *
     * @throws IOException on file read error
     */
    // TODO: add a toModel or similar method instead
    public Pixmap toPixmap() throws IOException {
        if (explodedFile != null) {
            return null;
        }
        return null;
    }

    @Override
    public String toString() {
        return "ModelEntry{" + modId + ":" + category + "/" + name + "@" + lod + "}";
    }
}
