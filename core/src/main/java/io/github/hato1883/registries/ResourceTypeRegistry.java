package io.github.hato1883.registries;

import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.resource.ResourceTypeRegisterEvent;
import io.github.hato1883.api.events.registry.resource.ResourceTypeReplaceEvent;
import io.github.hato1883.api.events.registry.resource.ResourceTypeUnregisterEvent;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.ITileType;

import java.util.HashSet;
import java.util.Set;

public class ResourceTypeRegistry extends Registry<IResourceType> {

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
    protected RegistryRegisterEvent<IResourceType> createRegistryRegisterEvent(String id, IResourceType element) {
        return new ResourceTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IResourceType> createRegistryReplaceEvent(String id, IResourceType oldElement, IResourceType newElement) {
        return new ResourceTypeReplaceEvent(this, id, oldElement, newElement) ;
    }

    @Override
    protected RegistryUnregisterEvent<IResourceType> createRegistryUnregisterEvent(String id, IResourceType element) {
        return new ResourceTypeUnregisterEvent(this, id, element) ;
    }
}
