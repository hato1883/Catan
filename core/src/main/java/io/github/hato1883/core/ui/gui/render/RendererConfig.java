package io.github.hato1883.core.ui.gui.render;

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
