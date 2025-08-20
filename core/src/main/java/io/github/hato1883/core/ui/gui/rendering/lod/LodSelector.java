package io.github.hato1883.core.ui.gui.rendering.lod;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Small, testable policy object for LOD selection. Open for extension.
 */
public class LodSelector {
    private final float tileRadius; // world units

    public LodSelector(float tileRadius) {
        this.tileRadius = tileRadius;
    }

    public int selectFor(OrthographicCamera camera) {
        float hexWorldHeight = 2f * tileRadius;
        float screenScale = Gdx.graphics.getDensity() / camera.zoom;
        float tilePixelHeight = screenScale * hexWorldHeight;
        if (tilePixelHeight > 256) return 0;
        if (tilePixelHeight > 128) return 1;
        if (tilePixelHeight > 64)  return 2;
        return 3;
    }
}
