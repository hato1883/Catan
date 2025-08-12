package io.github.hato1883.api.events.registry.resource;

import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.registries.IRegistry;

public class ResourceTypeRegisterEvent extends RegistryRegisterEvent<IResourceType> {
    public ResourceTypeRegisterEvent(IRegistry<IResourceType> registry, String id, IResourceType entry) {
        super(registry, id, entry);
    }
}
