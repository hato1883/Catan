package io.github.hato1883.modloader.assets;

import io.github.hato1883.modloader.LoadedMod;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Scans all loaded mods for texture files inside
 * assets/<mod-id>/textures/<category>/lod<lod>/texture.png
 * and creates ModelEntry objects.
 */
public final class ModModelAssetManager {

    private ModModelAssetManager() {}

    /**
     * Discover all textures from all mods.
     *
     * @param mods List of LoadedMod instances to scan
     * @return List of ModelEntry found in all mods
     */
    public static List<ModelEntry> discoverModels(AssetConfig cfg, List<LoadedMod> mods) throws IOException {
        List<ModelEntry> entries = new ArrayList<>();
        for (LoadedMod mod : mods) {
            if (Files.isDirectory(mod.path())) {
                entries.addAll(scanExplodedMod(cfg, mod));
            } else {
                entries.addAll(scanJarMod(cfg, mod));
            }
        }
        return entries;
    }

    private static List<ModelEntry> scanExplodedMod(AssetConfig cfg, LoadedMod mod) {
        List<ModelEntry> entries = new ArrayList<>();
        // TODO: Add a way to find models from a mod directory
        return entries;
    }

    private static List<ModelEntry> scanJarMod(AssetConfig cfg, LoadedMod mod) throws IOException {
        List<ModelEntry> entries = new ArrayList<>();
        // TODO: Add a way to find models from a mod jar
        return entries;
    }

    private static Integer parseLodFolderName(String folderName) {
        if (folderName.toLowerCase().startsWith("lod")) {
            try {
                return Integer.parseInt(folderName.substring(3));
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    private static String stripExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        return idx > 0 ? filename.substring(0, idx) : filename;
    }
}
