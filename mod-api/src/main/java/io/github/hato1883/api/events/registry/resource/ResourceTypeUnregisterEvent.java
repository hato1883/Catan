package io.github.hato1883.api.events.registry.resource;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.registries.IRegistry;

public class ResourceTypeUnregisterEvent extends RegistryUnregisterEvent<IResourceType> {
    public ResourceTypeUnregisterEvent(IRegistry<IResourceType> registry, Identifier id, IResourceType entry) {
        super(registry, id, entry);
    }
}
