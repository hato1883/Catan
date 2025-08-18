package io.github.hato1883.core.modloading.assets.textures;

import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.mod.load.ILoadedMod;
import io.github.hato1883.api.mod.load.asset.AssetCategory;
import io.github.hato1883.api.mod.load.asset.TextureDiscoveryService;
import io.github.hato1883.api.mod.load.asset.TextureEntry;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CombinedTextureDiscoveryService implements TextureDiscoveryService {

    private final FileSystemTextureDiscovery discovery = new FileSystemTextureDiscovery();

    @Override
    public Map<ILoadedMod, Map<AssetCategory, Map<Integer, List<TextureEntry>>>> discover(List<ILoadedMod> mods) throws IOException {
        Map<ILoadedMod, Map<AssetCategory, Map<Integer, List<TextureEntry>>>> result = new HashMap<>();

        for (ILoadedMod mod : mods) {
            String modId = mod.id();
            Path modPath = mod.path();

            LogManager.getLogger("CombinedTextureDiscoveryService").info("Trying to load assets for: {} from path: \"{}\"", modId, modPath);

            try (FileSystem fs = openFileSystem(modPath)) {
                result.put(mod, discovery.discoverForMod(modId, fs, modPath));
            } catch (IOException e) {
                // cleanup on failure
                LogManager.getLogger("CombinedTextureDiscoveryService").warn("A exception has occurred! {}", e.getMessage());
            }

        }

        return result;
    }

    private FileSystem openFileSystem(Path modPath) throws IOException {
        if (Files.isDirectory(modPath)) {
            return FileSystems.getDefault();
        } else if (modPath.toString().endsWith(".jar")) {
            return FileSystems.newFileSystem(modPath, (ClassLoader) null);
        } else {
            throw new IOException("Unsupported mod path: " + modPath);
        }
    }
}


