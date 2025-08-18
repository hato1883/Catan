package io.github.hato1883.api.mod.load;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/** Abstraction for locating mod files (dir or jars) */
public interface IModDiscovery {
    List<Path> discoverMods(Path modsDir) throws IOException;
}
