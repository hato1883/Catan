package io.github.hato1883.api.events.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Type-safe batching job for SpriteBatch. Mods implement this to render with SpriteBatch without casting.
 */
public interface SpriteBatchJob extends IBatchingJob {
    void render(SpriteBatch batch, Object renderContext);
    // Optionally override getBatchOptions() if needed
}

