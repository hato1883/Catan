package io.github.hato1883.api.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.Identifier;
// Add model/audio types as needed

public interface IAssetProvider {
    Texture getTexture(Identifier id);
    Texture getTexture(Identifier id, int lod);
    TextureRegion getTextureRegion(Identifier id);
    TextureRegion getTextureRegion(Identifier id, int lod);
    BitmapFont getFont(String id);
    TextureAtlas getAtlas(String id);
    // Object getModel(String id);
    // Object getAudio(String id);
}

