package io.github.hato1883.registries;

import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.tile.TileTypeRegisterEvent;
import io.github.hato1883.api.events.registry.tile.TileTypeReplaceEvent;
import io.github.hato1883.api.events.registry.tile.TileTypeUnregisterEvent;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.ITileType;

import java.util.*;

public class TileTypeRegistry extends Registry<ITileType> {

    /* Example method */
    public ITileType getDefaultTile() {
        return get("catan:forest").orElseThrow(() ->
            new IllegalStateException("Default tile not registered"));
    }

    /**
     * Returns a set of a tile types that can produce the given resource
     * @param type resource that tile must be able to produce
     * @return set of all tiles capable of producing the resource
     */
    public Set<ITileType> getProducerOf(IResourceType type) {
        Set<ITileType> producers = new HashSet<>();
        for (ITileType tile : getAll()) {
            if (tile.getBaseProduction().containsKey(type)) {
                producers.add(tile);
            }
        }
        return producers;
    }

    @Override
    protected RegistryRegisterEvent<ITileType> createRegistryRegisterEvent(String id, ITileType element) {
        return new TileTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<ITileType> createRegistryReplaceEvent(String id, ITileType oldElement, ITileType newElement) {
        return new TileTypeReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<ITileType> createRegistryUnregisterEvent(String id, ITileType element) {
        return new TileTypeUnregisterEvent(this, id, element);
    }
}
