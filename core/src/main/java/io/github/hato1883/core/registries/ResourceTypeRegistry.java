package io.github.hato1883.core.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.resource.ResourceTypeRegisterEvent;
import io.github.hato1883.api.events.registry.resource.ResourceTypeReplaceEvent;
import io.github.hato1883.api.events.registry.resource.ResourceTypeUnregisterEvent;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.ITileType;
import io.github.hato1883.api.registries.IResourceTypeRegistry;

import java.util.HashSet;
import java.util.Set;

public class ResourceTypeRegistry extends Registry<IResourceType> implements IResourceTypeRegistry {

    public ResourceTypeRegistry(io.github.hato1883.api.events.IEventBusService eventBus) {
        super(eventBus);
    }

    /**
     * Returns a set of a resources the given tile can produce
     * @param tile tile to check production type
     * @return set of all resources the tile is capable of producing
     * @see ITileType#getBaseProduction()
     */
    public Set<IResourceType> getProductsFrom(ITileType tile) {
        Set<IResourceType> products = new HashSet<>();
        tile.getBaseProduction().forEach((k, v) -> products.add(k));
        return products;
    }

    @Override
    protected RegistryRegisterEvent<IResourceType> createRegistryRegisterEvent(Identifier id, IResourceType element) {
        return new ResourceTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IResourceType> createRegistryReplaceEvent(Identifier id, IResourceType oldElement, IResourceType newElement) {
        return new ResourceTypeReplaceEvent(this, id, oldElement, newElement) ;
    }

    @Override
    protected RegistryUnregisterEvent<IResourceType> createRegistryUnregisterEvent(Identifier id, IResourceType element) {
        return new ResourceTypeUnregisterEvent(this, id, element) ;
    }
}
