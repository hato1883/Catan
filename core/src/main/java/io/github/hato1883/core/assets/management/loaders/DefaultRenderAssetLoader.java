package io.github.hato1883.core.assets.management.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.assets.TextureUpgradeNotifier;
import io.github.hato1883.api.mod.load.asset.AssetCategory;
import io.github.hato1883.core.assets.management.textures.LodAwareTileTextureProvider;
import io.github.hato1883.core.assets.management.textures.TileTextureProvider;
import io.github.hato1883.core.common.util.PathResolver;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static io.github.hato1883.core.common.util.DelayedFormatter.format;

public final class DefaultRenderAssetLoader implements RenderAssetLoader {
    private static final Logger LOGGER = LogManager.getLogger("GameAssets");
    /**
     * Relative path from game data dir to the texture atlases
     * Now we only need to decide which lod to use
     */
    private static final String TEXTURE_ASSET_ROOT = "assets/textures/combined";
    private static final String LOD_O = "lod0";
    private static final String LOD_1 = "lod1";
    private static final String LOD_2 = "lod2";
    private static final String LOD_3 = "lod3";

    /**
     * List of lods (Level of Detail) from the lowest texture size to largest
     */
    private static final String[] LODS = {LOD_3, LOD_2, LOD_1, LOD_O};

    private final AssetManager assetManager = new AssetManager(new AbsoluteFileHandleResolver());
    private final String fontPath;
    private float lastProgress = -1f;

    private BitmapFont numberFont;
    private TileTextureProvider textureProvider;

    private final Set<String> pendingLodAtlases = new HashSet<>();

    public DefaultRenderAssetLoader(String fontPath) {
        this.fontPath = fontPath; // e.g. "fonts/Roboto-Regular.ttf"
    }

    private static BitmapFont createBaseTokenFont(String ttfPath) {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(ttfPath));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 144;
        param.magFilter = Texture.TextureFilter.Linear;
        param.minFilter = Texture.TextureFilter.Linear;
        param.borderColor = Color.WHITE;
        param.borderWidth = 3;
        param.borderStraight = true;
        BitmapFont font = gen.generateFont(param);
        gen.dispose();
        font.getData().setScale(0.25f);
        font.setColor(Color.BLACK);
        return font;
    }

    @Override
    public void queueAssets() {
        for (String lod : LODS) {
            for (AssetCategory cat : AssetCategory.values()) {
                Path path = PathResolver.getGameDataDir().resolve(TEXTURE_ASSET_ROOT).resolve(cat.getCategory()).resolve(lod).resolve(String.format("combined_%s_%s.atlas", cat.getCategory(), lod));
                if (Files.exists(path)) {
                    assetManager.load(path.toString(), TextureAtlas.class);
                    pendingLodAtlases.add(path.toString());
                }
            }
        }
        numberFont = createBaseTokenFont(fontPath);
        textureProvider = new LodAwareTileTextureProvider(
            assetManager,
            PathResolver.getGameDataDir().resolve(TEXTURE_ASSET_ROOT)
        );
    }

    @Override
    public boolean update() {
        boolean done = assetManager.update();
        // Check for newly loaded LOD atlases and notify provider
        java.util.Iterator<String> it = pendingLodAtlases.iterator();
        while (it.hasNext()) {
            String atlasPath = it.next();
            if (assetManager.isLoaded(atlasPath, TextureAtlas.class)) {
                // Extract LOD from path string (e.g., .../lod2/combined_tile_lod2.atlas)
                String lodStr = atlasPath.contains("lod0") ? "lod0" : atlasPath.contains("lod1") ? "lod1" : atlasPath.contains("lod2") ? "lod2" : "lod3";
                int lod = lodStr.equals("lod0") ? 0 : lodStr.equals("lod1") ? 1 : lodStr.equals("lod2") ? 2 : 3;
                if (textureProvider instanceof io.github.hato1883.core.assets.management.textures.LodAwareTileTextureProvider lodProvider) {
                    lodProvider.onLodAtlasLoaded(lod);
                }
                it.remove();
            }
        }
        if (done && numberFont == null) {
            numberFont = createBaseTokenFont(fontPath);
        }
        return done && numberFont != null;
    }

    @Override
    public LoadedAssets getAssets() {
        return new LoadedAssets(textureProvider, numberFont);
    }

    @Override
    public void renderLoading() {
        float p = assetManager.getProgress();
        if (p > lastProgress) {
            lastProgress = p;
            LOGGER.info("Loading: {}%", format(Locale.US, "%2.0f", p * 100f));
        }
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        if (numberFont != null) numberFont.dispose();
    }

    @Override
    public TextureUpgradeNotifier getTextureUpgradeNotifier() {
        return (TextureUpgradeNotifier) textureProvider;
    }
}
