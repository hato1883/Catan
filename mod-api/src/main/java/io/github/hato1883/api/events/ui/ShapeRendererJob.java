package io.github.hato1883.api.events.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Type-safe batching job for ShapeRenderer. Mods implement this to render with ShapeRenderer without casting.
 */
public interface ShapeRendererJob extends IBatchingJob {
    void render(ShapeRenderer batch, Object renderContext);
    // Optionally override getBatchOptions() if needed
}

