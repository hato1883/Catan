package io.github.hato1883.api.ui.model;

/**
 * Rendering configuration for board and tile rendering.
 * Can be used by modders and the renderer without core dependency.
 */
public record RendererConfig(
    float tileRadius,
    float tileGap,
    float minZoom,
    float maxZoom
) {
    public static RendererConfig defaultConfig() {
        return new RendererConfig(50f, 10f, 0.05f, 2.0f);
    }
}

