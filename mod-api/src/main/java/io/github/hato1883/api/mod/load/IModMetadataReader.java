package io.github.hato1883.api.mod.load;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface for reading mod metadata from various sources
 */
public interface IModMetadataReader {
    /**
     * Read metadata from a mod path (can be directory or jar file)
     */
    ModMetadata readMetadata(Path modPath) throws IOException;
}
