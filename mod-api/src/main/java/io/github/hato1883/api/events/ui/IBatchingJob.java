package io.github.hato1883.api.events.ui;

/**
 * Interface for a mod's batching job. Implementations will be called with exclusive access to a batch of the requested type.
 */
public interface IBatchingJob {
    /**
     * Called during the render phase with exclusive access to the batch.
     * @param ctx The batching context (contains batch and render context)
     */
    void render(BatchingContext ctx);

    /**
     * The type of batch this job requires (e.g., SPRITE, POLYGON, SHAPE).
     */
    BatchType getBatchType();

    /**
     * Optional per-batch options (e.g., blend function cleanup).
     * Can return null for defaults.
     */
    default BatchOptions getBatchOptions() { return null; }
}

