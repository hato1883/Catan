package io.github.hato1883.api.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.ui.IBatchingJob;

/**
 * Registry for mod UI batching jobs. Each job reserves a batch for exclusive use during rendering.
 */
public interface IUIBatchingJobRegistry extends IRegistry<IBatchingJob> {
    // Registration is agnostic to job type; dispatch uses getBatchType().
}
