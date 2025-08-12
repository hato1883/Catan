package io.github.hato1883.api.events.registry.resource;

import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.registries.IRegistry;

public class ResourceTypeReplaceEvent extends RegistryReplaceEvent<IResourceType> {
    public ResourceTypeReplaceEvent(IRegistry<IResourceType> registry, String id, IResourceType oldEntry, IResourceType newEntry) {
        super(registry, id, oldEntry, newEntry);
    }
}
