package io.github.hato1883.api.ui.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.game.IResourceType;

import java.util.Locale;
import java.util.Map;

public class ResourceUiMetadata {
    private final IResourceType resourceType;
    private final TextureRegion icon;
    private final Map<Locale, String> localizedNames;

    public ResourceUiMetadata(IResourceType resourceType, TextureRegion icon, Map<Locale, String> localizedNames) {
        this.resourceType = resourceType;
        this.icon = icon;
        this.localizedNames = localizedNames;
    }
    // TODO: constructor, getters
}
