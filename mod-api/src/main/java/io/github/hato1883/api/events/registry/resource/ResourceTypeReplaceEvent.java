package io.github.hato1883.api.events.registry.resource;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.registries.IRegistry;

public class ResourceTypeReplaceEvent extends RegistryReplaceEvent<IResourceType> {
    public ResourceTypeReplaceEvent(IRegistry<IResourceType> registry, Identifier id, IResourceType oldEntry, IResourceType newEntry) {
        super(registry, id, oldEntry, newEntry);
    }
}
