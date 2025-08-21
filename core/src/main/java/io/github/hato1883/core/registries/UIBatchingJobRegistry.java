package io.github.hato1883.core.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.ui.IBatchingJob;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.registries.IUIBatchingJobRegistry;

/**
 * Concrete registry for mod UI batching jobs. Each job reserves a batch for exclusive use during rendering.
 */
public class UIBatchingJobRegistry extends Registry<IBatchingJob> implements IUIBatchingJobRegistry {
    @Override
    protected RegistryRegisterEvent<IBatchingJob> createRegistryRegisterEvent(Identifier id, IBatchingJob element) {
        return new RegistryRegisterEvent<>(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IBatchingJob> createRegistryReplaceEvent(Identifier id, IBatchingJob oldElement, IBatchingJob newElement) {
        return new RegistryReplaceEvent<>(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IBatchingJob> createRegistryUnregisterEvent(Identifier id, IBatchingJob element) {
        return new RegistryUnregisterEvent<>(this, id, element);
    }

    @Override
    public IBatchingJob register(Identifier id, IBatchingJob job) {
        return super.register(id, job);
    }
}
