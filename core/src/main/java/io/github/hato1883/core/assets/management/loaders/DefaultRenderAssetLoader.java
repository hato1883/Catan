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
import io.github.hato1883.core.assets.management.textures.LodAwareTileTextureProvider;
import io.github.hato1883.core.assets.management.textures.TileTextureProvider;
import io.github.hato1883.api.mod.load.asset.AssetCategory;
import io.github.hato1883.core.common.util.PathResolver;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

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
        // Iterate trough lod -> categories, this will result in all atlases with low resolution images getting loaded first.
        // which is good as it minimizes time needed before we can display something.
        // Iterate through all lods, starting with lod of lowest resolution
        for (String lod : LODS) {
            // For each lod load all different categories
            for (AssetCategory cat : AssetCategory.values()) {
                Path path = PathResolver.getGameDataDir().resolve(TEXTURE_ASSET_ROOT).resolve(cat.getCategory()).resolve(lod).resolve(String.format("combined_%s_%s.atlas", cat.getCategory(), lod));
                if (Files.exists(path))
                    assetManager.load(path.toString(), TextureAtlas.class);
            }
        }
        numberFont = createBaseTokenFont(fontPath);

        // create provider once
        textureProvider = new LodAwareTileTextureProvider(
            assetManager,
            PathResolver.getGameDataDir().resolve(TEXTURE_ASSET_ROOT)
        );
    }

    @Override
    public boolean update() {
        boolean done = assetManager.update();
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
}
