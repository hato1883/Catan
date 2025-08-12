package io.github.hato1883.api.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.game.IResourceType;

import java.util.Locale;

public interface IResourceIconProvider {
    TextureRegion getIcon(IResourceType resource);
    String getLocalizedName(IResourceType resource, Locale locale);
}
