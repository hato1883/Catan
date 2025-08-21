package io.github.hato1883.api.events.ui;

/**
 * Marker interface for batching jobs that require an isolated, persistent batch instance.
 * If a job implements this, the framework will provide a dedicated batch for it,
 * which is reused every frame and disposed with the screen.
 */
public interface IIsolatedBatchingJob extends IBatchingJob {
    // No methods needed; marker only.
}

