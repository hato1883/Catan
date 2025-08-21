package io.github.hato1883.api.events.ui;

/**
 * Context passed to each IBatchingJob during rendering. Contains the batch instance and render context.
 * The actual types should be replaced with your engine's types (e.g., SpriteBatch, PolygonBatch, etc).
 */
public class BatchingContext {
    private final Object batch; // Replace with actual batch type as needed
    private final Object renderContext; // Replace with actual context type as needed

    public BatchingContext(Object batch, Object renderContext) {
        this.batch = batch;
        this.renderContext = renderContext;
    }

    public Object getBatch() {
        return batch;
    }

    public Object getRenderContext() {
        return renderContext;
    }
}

