package io.github.hato1883.modloader.assets;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Builds TextureAtlases from a list of TextureEntry for a single category and LOD.
 * Packs textures into pages using LibGDX PixmapPacker and returns a TextureAtlas.
 */
public class AtlasBuilder {

    private AtlasBuilder() {}

    /**
     * Packs the given TextureEntry list into a TextureAtlas.
     *
     * @param cfg the asset configuration which includes texture packing settings
     * @param textureEntries list of TextureEntry belonging to the same category and lod
     * @return a TextureAtlas with all textures packed
     * @throws IOException if any Pixmap cannot be loaded
     */
    public static TextureAtlas buildAtlas(AssetConfig cfg, List<TextureEntry> textureEntries) throws IOException {
        Objects.requireNonNull(cfg);
        if (textureEntries.isEmpty()) {
            throw new IllegalArgumentException("No texture entries to pack");
        }

        PixmapPacker packer = new PixmapPacker(
            cfg.atlasPageSize(),
            cfg.atlasPageSize(),
            Pixmap.Format.RGBA8888,
            cfg.padding(),
            false // duplicateBorder not needed for now
        );

        // Add all textures to the packer
        for (TextureEntry entry : textureEntries) {
            Pixmap pixmap = entry.toPixmap();
            try {
                packer.pack(entry.textureId.toString(), pixmap);
            } finally {
                pixmap.dispose(); // free pixmap memory ASAP
            }
        }

        // Generate atlas pages and create TextureAtlas
        TextureAtlas atlas = packer.generateTextureAtlas(
            cfg.minFilter(),
            cfg.magFilter(),
            cfg.useMipMaps()
        );

        packer.dispose(); // Dispose packer pixmaps (now on GPU)

        return atlas;
    }
}
