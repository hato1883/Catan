package io.github.hato1883.core.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.assets.IAssetProvider;
import io.github.hato1883.core.assets.management.loaders.RenderAssetLoader;
import io.github.hato1883.core.assets.management.textures.TileTextureProvider;

public class AssetProvider implements IAssetProvider {
    private final RenderAssetLoader assetLoader;

    public AssetProvider(RenderAssetLoader assetLoader) {
        this.assetLoader = assetLoader;
    }

    @Override
    public Texture getTexture(Identifier id) {
        return getTexture(id, 0);
    }

    @Override
    public Texture getTexture(Identifier id, int lod) {
        return assetLoader.getAssets().tileTextures().getTileTexture(lod, id).getTexture();
    }

    @Override
    public BitmapFont getFont(String id) {
        return assetLoader.getAssets().numberTokenFont();
    }

    @Override
    public TextureRegion getTextureRegion(Identifier id) {
        return getTextureRegion(id, 0);
    }

    @Override
    public TextureRegion getTextureRegion(Identifier id, int lod) {
        return assetLoader.getAssets().tileTextures().getTileTexture(lod, id);
    }

    @Override
    public TextureAtlas getAtlas(String id) {
        //assetLoader.getAssets().tileTextures(id);
        return null;
    }
}
