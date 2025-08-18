package io.github.hato1883.core.modloading;

import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.mod.load.IModDiscovery;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Default discovery implementation that reads <gameData>/mods */
public class FilesystemModDiscovery implements IModDiscovery {

    private static final Logger LOGGER = LogManager.getLogger("FilesystemModDiscovery");

    @Override
    public List<Path> discoverMods(Path modsDir) throws IOException {
        if (!Files.exists(modsDir)) {
            Files.createDirectories(modsDir);
        }

        List<Path> results = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(modsDir)) {
            for (Path p : ds) {
                if (Files.isDirectory(p) || p.toString().endsWith(".jar")) {
                    results.add(p);
                } else {
                    LOGGER.warn("Skipping unrecognized file in mods dir: {}", p);
                }
            }
        }
        return results;
    }
}
