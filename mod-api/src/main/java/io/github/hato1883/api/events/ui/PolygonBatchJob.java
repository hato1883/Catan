package io.github.hato1883.api.events.ui;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

/**
 * Type-safe batching job for PolygonSpriteBatch. Mods implement this to render with PolygonSpriteBatch without casting.
 */
public interface PolygonBatchJob extends IBatchingJob {
    void render(PolygonSpriteBatch batch, Object renderContext);
    // Optionally override getBatchOptions() if needed
}

