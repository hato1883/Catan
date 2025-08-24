package io.github.hato1883.core.modloading.assets.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import java.io.FileOutputStream;
import io.github.hato1883.api.mod.load.ILoadedMod;
import io.github.hato1883.api.mod.load.asset.AssetCategory;
import io.github.hato1883.api.mod.load.asset.AssetConfig;
import io.github.hato1883.api.mod.load.asset.TextureAtlasBuilder;
import io.github.hato1883.api.mod.load.asset.TextureEntry;
import io.github.hato1883.core.modloading.assets.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class DefaultTextureAtlasBuilder implements TextureAtlasBuilder {

    private static final String CACHE_SUFFIX = ".cache.json";
    private static final String ATLAS_SUFFIX = ".atlas";

    private final boolean forceRebuild;
    private final AssetConfig cfg;

    public DefaultTextureAtlasBuilder(AssetConfig cfg) {
        this.forceRebuild = false;
        this.cfg = cfg;
    }

    /**
     *
     * @param mod contains information regarding what mod id and mod version has been used to create the atlas
     * @param category What category of assets does this atlas handle
     * @param lod which Level of Detail this atlas covers
     * @param textures List of all textures that should be within the Atlas
     * @param atlasDirectory Directory where .atlas and cache.json should exist
     */
    @Override
    public void ensureAtlas(ILoadedMod mod, AssetCategory category, int lod,
                            List<TextureEntry> textures, Path atlasDirectory) throws IOException {

        if (textures.isEmpty()) {
            return; // Nothing to build
        }
        final String baseName = String.format("%s_%s_lod%d", mod.id(), category.getCategory(), lod);

        // Assumes that modCacheDir is the location where atlas and cache file resides
        Path cacheJSONPath = atlasDirectory.resolve(baseName + CACHE_SUFFIX);
        Path cacheAtlasPath = atlasDirectory.resolve(baseName + ATLAS_SUFFIX);

        AtlasCacheInfo cachedInfo;
        try {
            cachedInfo = AtlasCacheInfo.load(cacheJSONPath);
            if (!forceRebuild && cachedInfo != null && cachedInfo.matches(Map.of(mod.id(), mod.metadata().version()), textures)) {
                return; // Already built
            }
        } catch (IOException e) {
            // Cache file does not exist, recreate textures;
        }
        // Create directory for atlas and cache.
        Files.createDirectories(cacheJSONPath.getParent());
        Files.createDirectories(cacheAtlasPath.getParent());

        Path tempInputDir = Files.createTempDirectory("texturepack-input");
        try {
            // Copy textures into temp folder
            for (TextureEntry entry : textures) {
                String fileName = entry.getFileName(); // abstract method to get a name for TexturePacker
                Path targetFile = tempInputDir.resolve(fileName);
                Files.createDirectories(targetFile.getParent());

                try (InputStream in = entry.openStream()) {
                    Files.copy(in, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // Configure TexturePacker
            TexturePacker.Settings settings = new TexturePacker.Settings();
            settings.maxWidth = cfg.atlasPageSize();
            settings.maxHeight = cfg.atlasPageSize();
            settings.filterMag = cfg.magFilter();
            settings.filterMin = cfg.minFilter();
            settings.paddingX = cfg.padding();
            settings.paddingY = cfg.padding();
            settings.duplicatePadding = true;
            settings.pot = true;

            TexturePacker.process(
                settings,
                tempInputDir.toString(),
                atlasDirectory.toString(),
                baseName // Omit ".atlas" as it is implied
            );

            AtlasCacheInfo.save(cacheJSONPath, Map.of(mod.id(), mod.metadata().version()), textures, category.getCategory(), lod);

        } finally {
            // Cleanup temp folder
            try (var walk = Files.walk(tempInputDir)) {
                walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
            }
        }
    }

    /**
     * Incrementally builds an atlas from a precedence-resolved set of textures.
     * Only one image and the atlas are in memory at a time.
     */
    public void buildIncrementalAtlas(List<TextureEntry> resolvedTextures, Path atlasDirectory, String baseName) throws IOException {
        if (resolvedTextures.isEmpty()) return;
        Files.createDirectories(atlasDirectory);

        PixmapPacker packer = new PixmapPacker(cfg.atlasPageSize(), cfg.atlasPageSize(), Pixmap.Format.RGBA8888, cfg.padding(), false);
        try {
            for (TextureEntry entry : resolvedTextures) {
                try (InputStream in = entry.openStream()) {
                    Pixmap pixmap = new Pixmap(in.readAllBytes(), 0, in.available());
                    packer.pack(entry.getFileName(), pixmap);
                    pixmap.dispose();
                }
            }
            // Save atlas pages as PNGs
            int pageCount = packer.getPages().size;
            for (int i = 0; i < pageCount; i++) {
                Pixmap page = packer.getPages().get(i).getPixmap();
                Path pagePath = atlasDirectory.resolve(baseName + "_page" + i + ".png");
                FileHandle handel = Gdx.files.absolute(pagePath.toFile().getAbsoluteFile().toString());
                PixmapIO.writePNG(handel, page);
            }
            // Save basic metadata as JSON
            Path metaPath = atlasDirectory.resolve(baseName + ".atlas.json");
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            List<String> names = resolvedTextures.stream().map(TextureEntry::getFileName).toList();
            try (FileOutputStream out = new FileOutputStream(metaPath.toFile())) {
                out.write(json.toJson(names).getBytes());
            }
        } finally {
            packer.dispose();
        }
    }
}
