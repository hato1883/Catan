package io.github.hato1883.modloader.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.IntMap;
import io.github.hato1883.modloader.LoadedMod;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Scans all loaded mods for texture files inside
 * assets/<mod-id>/textures/<category>/lod<lod>/texture.png
 * and creates TextureEntry objects.
 */
public final class ModTextureAssetManager {

    private final AssetConfig cfg;
    private final Map<AssetCategory, IntMap<TextureAtlas>> texturesByCategory;

    public ModTextureAssetManager(AssetConfig cfg) {
        this.cfg = cfg;
        this.texturesByCategory = new HashMap<>();
    }

    /**
     * Discovers textures in given mods, builds atlases per mod/category/lod.
     *
     * @param mods list of loaded mods
     * @throws IOException if texture loading fails
     */
    public void loadMods(List<LoadedMod> mods) throws IOException {
        // Clear old storage
        texturesByCategory.clear();

        // Find all textures
        List<TextureEntry> textures = discoverTextures(mods);

        // Group textures by category and lod (using IntMap for lod)
        Map<AssetCategory, Map<Integer, List<TextureEntry>>> grouped =
            textures.stream()
                .collect(Collectors.groupingBy(
                    tex -> tex.category,
                    Collectors.groupingBy(tex -> tex.lod)
                ));

        for (Map.Entry<AssetCategory, Map<Integer, List<TextureEntry>>> catEntry : grouped.entrySet()) {
            AssetCategory category = catEntry.getKey();
            Map<Integer, List<TextureEntry>> lodMap = catEntry.getValue();

            IntMap<TextureAtlas> lodAtlases = new IntMap<>();

            for (Map.Entry<Integer, List<TextureEntry>> lodEntry : lodMap.entrySet()) {
                int lod = lodEntry.getKey();
                List<TextureEntry> texEntries = lodEntry.getValue();

                // Build atlas for this category + LOD
                TextureAtlas atlas = AtlasBuilder.buildAtlas(cfg, texEntries);
                lodAtlases.put(lod, atlas);
            }

            texturesByCategory.put(category, lodAtlases);
        }
    }

    /**
     * Discover all textures from all mods.
     *
     * @param mods List of LoadedMod instances to scan
     * @return List of TextureEntry found in all mods
     */
    private List<TextureEntry> discoverTextures(List<LoadedMod> mods) throws IOException {
        List<TextureEntry> entries = new ArrayList<>();
        for (LoadedMod mod : mods) {
            if (Files.isDirectory(mod.path())) {
                entries.addAll(scanExplodedMod(mod));
            } else {
                entries.addAll(scanJarMod(mod));
            }
        }
        return entries;
    }

    private List<TextureEntry> scanExplodedMod(LoadedMod mod) {
        List<TextureEntry> entries = new ArrayList<>();
        FileHandle baseDir = Gdx.files.absolute(mod.path().toString()).child("assets").child(mod.id()).child("textures");
        if (!baseDir.exists() || !baseDir.isDirectory()) return entries;

        for (FileHandle categoryDir : baseDir.list()) {
            if (!categoryDir.isDirectory()) continue;
            String category = categoryDir.name();
            for (FileHandle lodDir : categoryDir.list()) {
                if (!lodDir.isDirectory()) continue;
                Integer lod = parseLodFolderName(lodDir.name());
                if (lod == null || !cfg.lodSizes().contains(lod)) continue;
                for (FileHandle textureFile : lodDir.list()) {
                    if (textureFile.isDirectory()) continue;
                    String name = stripExtension(textureFile.name());
                    entries.add(
                        TextureEntry.fromFileHandle(
                            AssetCategory.of(category),
                            lod,
                            Identifier.of(mod.id(), name),
                            textureFile
                        )
                    );
                }
            }
        }
        return entries;
    }

    private List<TextureEntry> scanJarMod(LoadedMod mod) throws IOException {
        List<TextureEntry> entries = new ArrayList<>();
        try (JarFile jarFile = new JarFile(mod.path().toFile())) {
            String prefix = "assets/" + mod.id() + "/textures/";
            var jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry entry = jarEntries.nextElement();
                if (entry.isDirectory()) continue;
                String name = entry.getName();
                if (!name.startsWith(prefix)) continue;

                // Parse category, lod, texture name
                String relative = name.substring(prefix.length()); // e.g. "tiles/lod256/castle.png"
                String[] parts = relative.split("/");
                if (parts.length < 3) continue; // must have category/lod/filename

                String category = parts[0];
                Integer lod = parseLodFolderName(parts[1]);
                if (lod == null || !cfg.lodSizes().contains(lod)) continue;

                String filename = parts[2];
                String textureName = stripExtension(filename);

                entries.add(
                    TextureEntry.fromJarEntry(
                        AssetCategory.of(category),
                        lod,
                        Identifier.of(mod.id(), textureName),
                        mod.path(),
                        name
                    )
                );
            }
        }
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

    /**
     * Retrieves the TextureAtlas for a given mod, category, and LOD.
     * Returns null if none exists.
     */
    public TextureAtlas getAtlas(AssetCategory category, int lod, Identifier modId) {
        return texturesByCategory.getOrDefault(category, new IntMap<>()).get(lod, null);
    }
}
