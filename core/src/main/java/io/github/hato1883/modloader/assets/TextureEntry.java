package io.github.hato1883.modloader.assets;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Represents a discovered texture file, backed either by exploded FileHandle or
 * inside a mod JAR. Holds metadata: textureId, category, texture name (without extension),
 * and lod level (0 if unspecified).
 */
public final class TextureEntry {
    public final AssetCategory category;       // "tiles"/"buildings"/"roads"/"ports" etc
    public final int lod;               // 0 if unspecified
    public final Identifier textureId;  // <modId>:<assetId>

    // Exactly one backing source is non-null:
    public final FileHandle explodedFile;  // preferred if mod is exploded
    public final Path jarPath;             // path to jar file if mod packed
    public final String jarEntryName;      // full path inside jar, e.g. assets/modid/textures/tiles/lod256/castle.png

    private TextureEntry(AssetCategory category, int lod, Identifier textureId,
                         FileHandle explodedFile, Path jarPath, String jarEntryName) {
        this.category = category;
        this.textureId = textureId;
        this.lod = lod;
        this.explodedFile = explodedFile;
        this.jarPath = jarPath;
        this.jarEntryName = jarEntryName;
    }

    /** Create a TextureEntry for exploded directory file */
    public static TextureEntry fromFileHandle(AssetCategory category, int lod, Identifier textureId, FileHandle file) {
        return new TextureEntry(category, lod, textureId, file, null, null);
    }

    /** Create a TextureEntry for a file inside a jar */
    public static TextureEntry fromJarEntry(AssetCategory category, int lod, Identifier textureId, Path jarPath, String jarEntryName) {
        return new TextureEntry(category, lod, textureId, null, jarPath, jarEntryName);
    }

    /**
     * Create a Pixmap from the backing file.
     * Uses FileHandle constructor if exploded,
     * or reads bytes from JAR entry and builds Pixmap from bytes.
     *
     * @throws IOException on file read error
     */
    public Pixmap toPixmap() throws IOException {
        if (explodedFile != null) {
            return new Pixmap(explodedFile);
        }
        Objects.requireNonNull(jarPath, "jarPath is null for jar-backed TextureEntry");
        Objects.requireNonNull(jarEntryName, "jarEntryName is null for jar-backed TextureEntry");

        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            JarEntry jarEntry = jarFile.getJarEntry(jarEntryName);
            if (jarEntry == null) throw new IOException("Jar entry not found: " + jarEntryName);
            try (InputStream is = jarFile.getInputStream(jarEntry)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(Math.max(4096, (int) jarEntry.getSize()));
                byte[] buffer = new byte[8192];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }
                byte[] bytes = baos.toByteArray();
                return new Pixmap(bytes, 0, bytes.length);
            }
        }
    }

    @Override
    public String toString() {
        return "TextureEntry{" + category + "/" + textureId + "@" + lod + "}";
    }
}
