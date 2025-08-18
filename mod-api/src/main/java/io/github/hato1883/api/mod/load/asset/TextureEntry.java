package io.github.hato1883.api.mod.load.asset;

import io.github.hato1883.api.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents a discovered texture file, backed either by exploded FileHandle or
 * inside a mod JAR. Holds metadata: textureId, category, texture name (without extension),
 * and lod level (0 if unspecified).
 *
 * @param modArchivePath For reopening the source jar or folder
 * @param internalPath   path inside mod (relative)
 */
public record TextureEntry(AssetCategory category, int lod, Identifier identifier, Path modArchivePath,
                           String internalPath) {

    public InputStream openStream() {
        try {
            if (Files.isDirectory(modArchivePath)) {
                // Direct file system mod folder
                return Files.newInputStream(modArchivePath.resolve(internalPath));
            } else {
                // Packed jar - open FS on demand
                try (FileSystem fs = FileSystems.newFileSystem(modArchivePath, (ClassLoader) null)) {
                    Path p = fs.getPath(internalPath);
                    // Need to copy to memory since FS closes at end of try
                    byte[] bytes = Files.readAllBytes(p);
                    return new ByteArrayInputStream(bytes);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Identifier getId() {
        return identifier;
    }

    public String getFileName() {
        return identifier.getNamespace() + internalPath.substring(internalPath.lastIndexOf("/"));
    }

    @Override
    public @NotNull String toString() {
        return "TextureEntry{" + category + "/" + identifier.toString().replace(":", "_") + "@" + lod + "}";
    }
}
